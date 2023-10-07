package org.anthony.me.panels;


import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.anthony.me.Frame;
import org.anthony.me.PanelSwitches;
import org.anthony.me.tiles.CriticalTile;
import org.anthony.me.tiles.Tile;
import org.anthony.me.tiles.WallTile;

import static org.anthony.me.Frame.SCREEN_HEIGHT;
import static org.anthony.me.Frame.SCREEN_WIDTH;
import static org.anthony.me.PanelManager.RECT_DIMENSION;

/**
 * extension of interface so that way all panels can be accessed through this. It also extends JPanel, allowing for
 * better access of the panels.
 */
public abstract class FramedAbstraction extends JPanel implements FramedInterface {

    public abstract int getAccesserID();
    
    public abstract void paintComponent(Graphics g);


    public Tile findStartTile(ArrayList<Tile> tiles){
        for (Tile tile : tiles){
            if (tile instanceof CriticalTile){
            if (!((CriticalTile) tile).isEnd()){
                return tile;
            }
            }
        }
        return null;
    }

    protected abstract void displayAlgorithm();

    protected abstract void uploadGrid();

    public Tile findEndTile(ArrayList<Tile> tiles){
        for (Tile tile : tiles){
            if (tile instanceof CriticalTile){
                if (((CriticalTile) tile).isEnd()){
                    return tile;
                }
            }
        }
        return null;
    }

    public ArrayList<Tile> findNeighboringTiles(Tile tileToCheck, ArrayList<Tile> tiles, boolean avoidWalls) {
        ArrayList<Tile> neighborTiles = new ArrayList<>();
        int xToCheck = tileToCheck.getxCoord();
        int yToCheck = tileToCheck.getyCoord();

        for (Tile tile : tiles) {
            if (avoidWalls && tile instanceof WallTile) continue;
            int xCoord = tile.getxCoord();
            int yCoord = tile.getyCoord();

            int xDiff = Math.abs(xCoord - xToCheck);
            int yDiff = Math.abs(yCoord - yToCheck);

            if ((xDiff == 25 && yDiff == 0) || (xDiff == 0 && yDiff == 25)) {
                neighborTiles.add(tile);
            }
        }
        return neighborTiles;
    }


    public PanelSwitches createButton(String displayText, int id, Dimension dimension, int xPos, int yPos, Frame frame){
        PanelSwitches panelSwitches = new PanelSwitches(displayText, id, dimension);
        panelSwitches.addActionListener(frame);
        panelSwitches.setBounds(xPos, yPos, dimension.width, dimension.height);
        this.add(panelSwitches);
        return panelSwitches;
    }

    public void placeWalls(ArrayList<Tile> tiles, int amount){
        int i = 0;
        int randomIndex;
        while (i < amount){

            randomIndex = findIndexToReplace(tiles);
        if (randomIndex == -1){
            return;
        }
        Tile tile = tiles.get(randomIndex);
        WallTile wallTile = new WallTile(tile.getxCoord(), tile.getyCoord(), tile.getDimension(), Color.DARK_GRAY);
            tiles.set(randomIndex, wallTile);
            i++;
                }
    }

    public int findIndexToReplace(ArrayList<Tile> tiles) {
        Random random = new Random();
        int randomIndex = random.nextInt(tiles.size());

        if (tiles.get(randomIndex) instanceof CriticalTile || tiles.get(randomIndex) instanceof WallTile) {
            // Recursive call, return the result.
            return findIndexToReplace(tiles);
        } else {
            return randomIndex;
        }
    }


    //Random critical tile placement method
    private void addCriticalTile(boolean isEnd, Color color, ArrayList<Tile> tiles){
        int randomIndex = findIndexToReplace(tiles);

        if (randomIndex == -1){
            return;
        }
        Tile tile = tiles.get(randomIndex);
        if (tile.getxCoord() == 800 && tile.getyCoord() == 800){
            tile = findTileAtCoordinate(775, 775, tiles);
        } else if (tile.getxCoord() == 800){
            tile = findTileAtCoordinate(775, tile.getyCoord(), tiles);
        }else if (tile.getyCoord() == 800){
            tile = findTileAtCoordinate(tile.getxCoord(), 775, tiles);
        }
        CriticalTile newTile = new CriticalTile(tile.getxCoord(), tile.getyCoord(), RECT_DIMENSION, color, isEnd);
        tiles.set(randomIndex, newTile);
    }

    /**
     * Spaces out the rectangles based on their width/height, stopping at screen borders.
     * It creates the tile, and adds it to the tiles arraylist for the panel
     */
    public void uploadTileGrid(ArrayList<Tile> tiles, boolean placeWalls, int amountOfWalls, boolean addCriticals){
        tiles.clear();
        for (int i = 0; i < SCREEN_HEIGHT; i++) {

            if (i % RECT_DIMENSION.height != 0 || i == 800) continue;

            for (int j = 0; j < SCREEN_WIDTH; j++) {
                if (j % RECT_DIMENSION.width == 0 && j != 800) {
                    tiles.add(new Tile(j, i, RECT_DIMENSION, Color.PINK));
                }
            }
        }
    if (addCriticals) {
      addCriticalTile(true, Color.RED, tiles);
      addCriticalTile(false, Color.GREEN, tiles);
        }
    if (placeWalls) {
      placeWalls(tiles, amountOfWalls);
        }
    }

    public Tile getAndRemoveFirstTile(LinkedHashMap<Tile, Boolean> queue) {
        Iterator<Map.Entry<Tile, Boolean>> iterator = queue.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<Tile, Boolean> firstEntry = iterator.next();
            iterator.remove();  // Remove the first entry
            return firstEntry.getKey();
        }
        return null;
    }

    private Tile findTileAtCoordinate(int x, int y, ArrayList<Tile> tiles){
        for (Tile tile : tiles){
            if (tile.getxCoord() != x) continue;
            if (tile.getyCoord() != y) continue;
            return tile;
        }
        return null;
    }







}
