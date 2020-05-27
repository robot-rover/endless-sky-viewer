package rr.industries;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rr.industries.structures.Config;
import rr.industries.structures.Ship;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Base class for Endless-Sky-Viewer
 * Everything else is run from here
 */
public class EndlessSkyViewer extends Application {

    public static final String APP_NAME = "endless-sky-viewer";
    public static final String APP_VERSION = "0.0.2";
    public static final String APP_AUTHOR = "robot_rover";

    public static final Logger LOG = LoggerFactory.getLogger(EndlessSkyViewer.class);

    private static final String stylesheet = "styling.css";

    private Stage primaryStage;
    private Scene primaryScene;
    private TabPane tabPane;
    private BorderPane root;
    private ShipPickerRoot shipPickerRoot;
    private Tab shipPicker;

    private Config config;
    private GameData gameData;

    private static Path getConfigFile() {
        AppDirs appDirs = AppDirsFactory.getInstance();
        return Path.of(appDirs.getUserConfigDir(APP_NAME, APP_VERSION, APP_AUTHOR), "config.txt");
    }

    private static Config readConfig() {
        Path configPath = getConfigFile();
        if(Files.exists(configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)){
                LOG.debug("Reading config from {}", configPath);
                Config config = new Gson().fromJson(reader, Config.class);
                if(config != null) {
                    LOG.debug("Read Gamedata Location {}", config.gameDataPath);
                    return config;
                } else {
                    LOG.warn("Error reading config, Gson returned null");
                }
            } catch (IOException | JsonSyntaxException | JsonIOException ex) {
                LOG.warn("Unable to read config file: " + configPath, ex);
            }
        }
        return new Config();
    }

    private static void writeConfig(Config config) {
        Path configPath = getConfigFile();
        try {
            Files.createDirectories(configPath.getParent());
            LOG.debug("Writing Config to {}", configPath);
            try (BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
                new Gson().toJson(config, writer);
            }
        } catch (IOException ex) {
            LOG.warn("Unable to write config file " + configPath, ex);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        primaryScene = new Scene(root);
        primaryScene.getStylesheets().add(stylesheet);
        primaryStage.setScene(primaryScene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Endless Sky Viewer");
        primaryStage.show();

        this.primaryStage = primaryStage;

        tabPane = new TabPane();
        tabPane.setMinWidth(240);

        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);

        shipPicker = new Tab("Ship Picker");
        shipPicker.setClosable(false);
        shipPickerRoot = new ShipPickerRoot(this::openVariants, this::editShip);
        shipPicker.setContent(shipPickerRoot);
        tabPane.getTabs().add(shipPicker);

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem openGameDir = new MenuItem("Change GameData");
        openGameDir.setOnAction((ActionEvent ae) -> queryGameDataLocation().ifPresent(this::loadGameData));
        MenuItem darkMode = new MenuItem("Dark Mode");
        darkMode.setOnAction((ActionEvent ae) -> darkMode(!config.darkMode));
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((ActionEvent ae) -> System.exit(0));
        fileMenu.getItems().addAll(openGameDir, darkMode, exit);

        Menu shipMenu = new Menu("Ship");
        MenuItem todo = new MenuItem("Coming Soon...");
        shipMenu.getItems().addAll(todo);

        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");
        MenuItem updates = new MenuItem("Check for Updates");
        helpMenu.getItems().addAll(about, updates);

        menuBar.getMenus().addAll(fileMenu, shipMenu, helpMenu);
        root.setTop(menuBar);

        config = readConfig();
        darkMode(config.darkMode);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> writeConfig(config)));
        Optional.ofNullable(config.gameDataPath).map(File::new).map(GameData::new).ifPresent(this::loadGameData);
    }

    private static final String darkModeStylesheet = "dark_mode.css";
    private void darkMode(boolean enable) {
        config.darkMode = enable;
        if(enable) {
            primaryScene.getStylesheets().add(darkModeStylesheet);
        } else {
            primaryScene.getStylesheets().remove(darkModeStylesheet);
        }
    }

    private Optional<GameData> queryGameDataLocation() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Endless Sky's install directory");
        File gameDataDirectory = directoryChooser.showDialog(primaryStage);

        if (gameDataDirectory != null) {
            config.gameDataPath = gameDataDirectory.getAbsolutePath();
            writeConfig(config);
        }

        return Optional.ofNullable(gameDataDirectory).map(GameData::new);
    }

    private void loadGameData(GameData gameData) {
        Label loadingFile = new Label("Loading GameData");
        ProgressBar progressBar = new ProgressBar(0.0);
        VBox popupRoot = new VBox(loadingFile, progressBar);
        popupRoot.setSpacing(10);
        popupRoot.setPadding(new Insets(10));
        popupRoot.setAlignment(Pos.CENTER);
        root.setCenter(popupRoot);
        Consumer<Double> progressCallback = d -> Platform.runLater(() -> {
            progressBar.setProgress(d);
            if (d == 1) initWithGameData(gameData);
        });
        Runnable backgroundTask = () -> {
            try {
                gameData.loadGameData(progressCallback);
            } catch (IOException ex) {
                LOG.warn("Failed to load gamedata from " + gameData.gameDataDirectory.getAbsolutePath(), ex);
                showAlert("Failed to load Game Data",
                        "Could not load Game Data from folder " + gameData.gameDataDirectory,
                        ex.getMessage());
                Platform.runLater(() -> root.setCenter(null));
            }
        };
        new Thread(backgroundTask).start();
    }

    /**
     * Setup the initial tab
     * This is the ShipPicker tab
     */
    void initWithGameData(GameData gameData) {
        this.gameData = gameData;
        tabPane.getTabs().removeIf(tab -> tab != shipPicker);
        root.setCenter(tabPane);
        shipPickerRoot.load(gameData);
    }

    /**
     * Callback to open a new tab to select variants
     *
     * @param ship The base ship
     */
    void openVariants(ShipListItem ship) {
        Tab variant = new Tab(ship.getBaseShip().getModelName() + " Variants");
        variant.setClosable(true);
        VariantPickerRoot variantPickerRoot = new VariantPickerRoot(ship, this::editShip, gameData);
        variant.setContent(variantPickerRoot);
        tabPane.getTabs().add(variant);
        tabPane.getSelectionModel().select(variant);
    }

    /**
     * Callback to open a new tab to edit a ship
     *
     * @param ship The base ship
     */
    void editShip(Ship ship) {
        Tab editor = new Tab(ship.getName() + " Editor");
        editor.setClosable(true);
        OutfitEditorRoot outfitEditorRoot = new OutfitEditorRoot(ship, gameData);
        editor.setContent(outfitEditorRoot);
        tabPane.getTabs().add(editor);
        tabPane.getSelectionModel().select(editor);
    }

    void showAlert(String title, String header, String message) {
        if(!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showAlert(title, header, message));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (title != null) {
                alert.setTitle(title);
            }
            if (header != null) {
                alert.setHeaderText(header);
            }
            if (message != null) {
                alert.setContentText(message);
            }

            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
