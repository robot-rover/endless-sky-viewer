package rr.industries;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rr.industries.structures.Ship;

import java.io.File;
import java.io.IOException;
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

    private Stage primaryStage;
    private Stage progressNotifier;
    private TabPane tabPane;
    private BorderPane root;

    private static Path getConfigFile() {
        AppDirs appDirs = AppDirsFactory.getInstance();
        return Path.of(appDirs.getUserConfigDir(APP_NAME, APP_VERSION, APP_AUTHOR), "config.txt");
    }

    private static Optional<String> readConfig() {
        Path config = getConfigFile();
        if(Files.exists(config)) {
            try {
                String gameDataPath = Files.readString(config);
                LOG.debug("Read Gamedata Location from {} -> {}", config, gameDataPath);
                return Optional.of(gameDataPath);
            } catch (IOException ex) {
                LOG.warn("Unable to read config file: " + config, ex);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static void writeConfig(String dataDir) {
        Path config = getConfigFile();
        try {
            Files.createDirectories(config.getParent());
            Files.writeString(config, dataDir, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            LOG.debug("Wrote Gamedata Location to {} -> {}", config, dataDir);
        } catch (IOException ex) {
            LOG.warn("Unable to write config file " + config, ex);
        }
    }


    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Endless Sky Viewer");
        primaryStage.show();

        this.primaryStage = primaryStage;

        GameData.gameDataDirectory = readConfig().map(File::new).orElse(null);

        if(GameData.gameDataDirectory == null) {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Endless Sky's install directory");
            GameData.gameDataDirectory = directoryChooser.showDialog(primaryStage);

            if (GameData.gameDataDirectory == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No GameData Folder");
                alert.setHeaderText("You did not select a GameData folder");
                alert.setContentText("Please launch endless-sky-viewer again and select the install directory of the game. It should have 3 folders in it named 'images', 'sounds' and 'data'.");

                alert.showAndWait();
                System.exit(-1);
            } else {
                writeConfig(GameData.getGameDirectory().getPath());
            }
        }


        progressNotifier = new Stage();
        Label loadingFile = new Label("Loading GameData");
        ProgressBar progressBar = new ProgressBar(0.0);
        VBox popupRoot = new VBox(loadingFile, progressBar);
        popupRoot.setSpacing(10);
        popupRoot.setPadding(new Insets(10));
        popupRoot.setAlignment(Pos.CENTER);
        Scene progressScene = new Scene(popupRoot);
        progressNotifier.setScene(progressScene);
        progressNotifier.initStyle(StageStyle.UNDECORATED);
        progressNotifier.show();
        Consumer<Double> progressCallback = d -> Platform.runLater(() -> {
            progressBar.setProgress(d);
            if (d == 1) initWindow();
        });
        new Thread(() -> GameData.loadGameData(progressCallback)).start();
    }

    /**
     * Setup the initial tab
     * This is the ShipPicker tab
     */
    void initWindow() {
        tabPane = new TabPane();
        tabPane.setMinWidth(240);

        Tab shipPicker = new Tab("Ship Picker");
        shipPicker.setClosable(false);
        ShipPickerRoot shipPickerRoot = new ShipPickerRoot(this::openVariants, this::editShip);
        shipPicker.setContent(shipPickerRoot);
        tabPane.getTabs().add(shipPicker);


        root.setCenter(tabPane);



        progressNotifier.close();
    }

    /**
     * Callback to open a new tab to select variants
     *
     * @param ship The base ship
     */
    void openVariants(ShipListItem ship) {
        Tab variant = new Tab(ship.getBaseShip().getModelName() + " Variants");
        variant.setClosable(true);
        VariantPickerRoot variantPickerRoot = new VariantPickerRoot(ship, this::editShip);
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
        OutfitEditorRoot outfitEditorRoot = new OutfitEditorRoot(ship);
        editor.setContent(outfitEditorRoot);
        tabPane.getTabs().add(editor);
        tabPane.getSelectionModel().select(editor);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
