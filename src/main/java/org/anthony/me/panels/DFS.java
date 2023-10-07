package org.anthony.me.panels;


import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.anthony.me.Frame;
import org.anthony.me.PanelManager;
import org.anthony.me.tiles.CriticalTile;
import org.anthony.me.tiles.Tile;

public class DFS extends BasePanel {


    public DFS(Dimension panelDimension, PanelManager panelManager, LayoutManager layoutManager, int id, Frame frame) {
        super(panelDimension, panelManager, layoutManager, id, frame);

    }
    


    @Override
    protected void displayAlgorithm() {
        this.setAlgorithmStarted(true);
        setShouldContinue(true);

        LinkedHashMap<Tile, Boolean> visitedTiles = new LinkedHashMap<>();
        LinkedHashMap<Tile, Tile> parentMap = new LinkedHashMap<>();

        this.setEndTile((CriticalTile) findEndTile(this.getTiles()));
        this.setStartTile((CriticalTile) findStartTile(this.getTiles()));


        explorePath(findStartTile(this.getTiles()), visitedTiles, parentMap);
    }

    @Override
    protected void uploadGrid(){
        this.uploadGrid(true, 200, true);
    }


    private void explorePath(Tile tile, LinkedHashMap<Tile, Boolean> visited, LinkedHashMap<Tile, Tile> parentMap) {
    Thread algorithmThread =
        new Thread(
            () -> {
              if (!isShouldContinue()
                  || visited.containsKey(tile)
                  || Thread.currentThread().isInterrupted()) {
                return;
              }

              visited.put(tile, true);
              if (tile != this.getStartTile() && tile != this.getEndTile()) {
                float hue = (float) Math.random();
                int rgb = Color.HSBtoRGB(hue, 0.3f, 0.5f);
                tile.setTileColor(new Color(rgb));
              }
              SwingUtilities.invokeLater(this::repaint);

              if (tile == this.getEndTile()) {
                setShouldContinue(false);
                reconstructAndHighlightPath(parentMap, this.getEndTile());
                SwingUtilities.invokeLater(this::repaint);
                return;
              }

              if (findNeighboringTiles(tile, this.getTiles(), true).isEmpty()) {
                this.getBackButton().setVisible(true);
                Thread.currentThread().interrupt();
                return;
              }
              for (Tile neighbor : findNeighboringTiles(tile, this.getTiles(), true)) {
                if (!isShouldContinue()) {
                  return;
                }

                parentMap.putIfAbsent(neighbor, tile);

                // Throttle UI updates and algorithm speed
                try {
                  Thread.sleep(100);
                } catch (InterruptedException e) {
                  this.getBackButton().setVisible(true);
                  setShouldContinue(false);
                  break;
                }

                explorePath(neighbor, visited, parentMap);
              }
            });
        algorithmThread.start();
    }



    private void reconstructAndHighlightPath(Map<Tile, Tile> parentMap, Tile currentTile) {
        this.getBackButton().setVisible(true);
        while (currentTile != null) {
      if (currentTile != this.getEndTile()) {
        currentTile.setTileColor(Color.GREEN); // Set tile color to highlight the path
                }
            currentTile = parentMap.get(currentTile);
        }
    }
    
    
    }

