package rr.industries;

import javafx.scene.image.Image;
import rr.industries.parser.DataFile;
import rr.industries.parser.DataNode;
import rr.industries.structures.Outfit;
import rr.industries.structures.OutfitPlaceholder;
import rr.industries.structures.Ship;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static rr.industries.EndlessSkyViewer.LOG;

/**
 * This class handles loading game assets
 * This includes sprites, images, and data files
 */
public class GameData {
    List<Outfit> outfits = new ArrayList<>();
    List<Ship> ships = new ArrayList<>();
    HashMap<String, Image> sprites = new HashMap<>();
    File gameDataDirectory;

    public GameData(File gameDataDirectory) {
        this.gameDataDirectory = gameDataDirectory;
    }

    /**
     * Get the base directory for Endless Sky
     *
     * @return a File pointing to the directory
     */
    public File getGameDirectory() {
        return gameDataDirectory;
    }

    /**
     * Gets a sprite
     *
     * @param path the location of the sprite
     * @return the sprite
     */
    public Image getSprite(String path) {
        return sprites.get(path);
    }

    /**
     * Gets a game subdirectory
     *
     * @param path the path relative to the base directory
     * @return a File pointing to the directory
     */
    public File getGameDirectory(String path) {
        return new File(gameDataDirectory, path);
    }

    /**
     * Gets an outfit instance or a pointer
     * Used during initial loading, before every outfit is loaded
     *
     * @param name name of the outfit
     * @return the outfit instance or a placeholder
     */
    public Outfit getOutfitOrPointer(String name) {
        return outfits.stream().filter(outfit -> outfit.getName().equals(name)).findAny().orElse(new OutfitPlaceholder(name, this));
    }

    /**
     * Gets an outfit instance or null if it can't be found
     *
     * @param name the name of the outfit
     * @return the outfit instance or null
     */
    public Outfit getOutfit(String name) {
        return outfits.stream().filter(outfit -> outfit.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Gets a base ship
     *
     * @param name the name of the ship
     * @return a Ship instance
     */
    public Ship getShipBase(String name) {
        return ships.stream().filter(ship -> ship.getModelName().equals(name) && ship.getVariantName() == null).findAny().orElse(null);
    }

    /**
     * Gets variants of a base ship
     *
     * @param name the name of the base ship
     * @return a list of Ship variants
     */
    public List<Ship> getShipVariants(String name) {
        return ships.stream().filter(ship -> ship.getModelName().equals(name) && ship.getVariantName() != null).collect(Collectors.toList());
    }

    /**
     * Gets a list of all loaded ships
     *
     * @return a List
     */
    public List<Ship> getShips() {
        return ships;
    }

    /**
     * Gets a list of all loaded outfits
     *
     * @return a List
     */
    public List<Outfit> getOutfits() {
        return outfits;
    }

    private List<File> recursivelyListFiles(File directory) {
        List<File> list = new ArrayList<>();
        recursivelyListFilesInternal(list, directory);
        return list;
    }

    private void recursivelyListFilesInternal(List<File> list, File dir) {
        for(File file : dir.listFiles()) {
            if(file.isFile()) {
                list.add(file);
            } else if(file.isDirectory()) {
                recursivelyListFilesInternal(list, file);
            }
        }
    }

    /**
     * Begins loading all data from the game
     *
     * @param progressCallback a callback to monitor loading progress
     */
    public void loadGameData(Consumer<Double> progressCallback) throws IOException {
        if (!getGameDirectory().exists() || !gameDataDirectory.isDirectory()) {
            LOG.error("Game Directory {} is Not Valid!", getGameDirectory().getAbsolutePath());
            System.exit(1);
        }
        // Load Ship Sprites
        File baseSpriteDir = getGameDirectory("images");
        recursiveLoadSprite(getGameDirectory("images/outfit"), baseSpriteDir, progressCallback, 0, 0.3);
        recursiveLoadSprite(getGameDirectory("images/ship"), baseSpriteDir, progressCallback, 0.3, 0.6);
        try {
            recursiveLoadSprite(getGameDirectory("images/thumbnail"), baseSpriteDir, progressCallback, 0.6, 0.9);
        } catch (IOException ex) {
            LOG.warn("No Thumbnails detected, proceeding because they are unnecessary", ex);
        }

        // Load Game Textual Data Files
        for (File file : recursivelyListFiles(getGameDirectory("data"))) {
            if (file.getName().equals("deprecated outfits.txt"))
                continue;
            try {
                DataFile dataStruct = new DataFile(file);
                for (DataNode child : dataStruct.root.children) {
                    String key = child.token(0);
                    int size = child.tokens.size();
                    if (size >= 2 && key.equals("ship")) {
                        ships.add(new Ship(child, this));
                    } else if (size >= 2 && key.equals("outfit")) {
                        Outfit outfit = new Outfit(child, this);
                        if (outfit.getCategory() != null)
                            outfits.add(outfit);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Ship ship : ships) {
            ship.finalLoad();
        }
        progressCallback.accept(1.);
        LOG.debug("GameData loaded");
    }

    private static final Pattern blendingModes = Pattern.compile("(^.*)([-+~=])([0-9]+)");

    private void recursiveLoadSprite(File dir, File baseSpriteDir, Consumer<Double> progressCallback, double startProg, double endProg) throws IOException {
        File[] spriteFiles = dir.listFiles(file -> !file.isDirectory());
        if (spriteFiles == null) {
            LOG.error("Cannot load {}, is game directory valid?", dir.getAbsolutePath());
            throw new IOException("Cannot load " + dir.getAbsolutePath());
        }
        double step = (endProg - startProg) / spriteFiles.length;
        for (int i = 0; i < spriteFiles.length; i++) {
            progressCallback.accept(startProg + i * step);
            try {
                Image sprite = new Image(new FileInputStream(spriteFiles[i]));
                String subPath = baseSpriteDir.toPath().relativize(spriteFiles[i].toPath()).toString();
                subPath = subPath.substring(0, subPath.lastIndexOf('.'));
                Matcher mat = blendingModes.matcher(subPath);
                if (mat.find()) {
                    if (Integer.parseInt(mat.group(3)) == 0) {
                        //System.out.println(mat.group(1) + " " + mat.group(2) + " " + mat.group(3));
                        subPath = mat.group(1);
                    } else {
                        continue;
                    }
                }
                // For windows compatibility
                subPath = subPath.replace(File.separatorChar, '/');
                sprites.put(subPath, sprite);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //todo: not needed at this time
        /*File[] directories = dir.listFiles(File::isDirectory);
        double step = (endProg - startProg) / directories.length;
        for(int i = 0; i < directories.length; i++) {
            progressCallback.accept(startProg + step * i);
            recursiveLoadSprite(directories[i], baseSpriteDir, progressCallback, startProg + step * i, startProg + endProg * (i+1));
        }*/
    }

    private void recursiveLoadSprite(File dir, File baseSpriteDir, Consumer<Double> progressCallback) throws IOException {
        recursiveLoadSprite(dir, baseSpriteDir, progressCallback, 0, 1);
    }
}
