package org.anthony.me.panels;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.anthony.me.Frame;
import org.anthony.me.PanelManager;
import org.anthony.me.tiles.CriticalTile;
import org.anthony.me.tiles.Tile;

public class BFS extends BasePanel {

    
    public BFS(Dimension panelDimension, PanelManager panelManager, LayoutManager layoutManager, int id, Frame frame) {
        super(panelDimension, panelManager, layoutManager, id, frame);
    }


    @Override
    protected void displayAlgorithm() {
        this.startTimer();
        this.setAlgorithmStarted(true);
        setShouldContinue(true);
        explorePath();
    }

    @Override
    protected void uploadGrid(){
        this.uploadGrid(true, 200, true);
    }


    private void explorePath(){
    Thread algorithmThread =
        new Thread(
            () -> {
              // The parent map is used to save the pathway from start to finish
              LinkedHashMap<Tile, Tile> parentMap =
                  new LinkedHashMap<>(); // Keep track of parent tiles
              Tile startTile = findStartTile(this.getTiles());
              Tile endTile = findEndTile(this.getTiles());
              this.setEndTile((CriticalTile) endTile);

              // Check if start or end tile not found
              if (startTile == null || endTile == null || !isShouldContinue()) {
                this.getBackButton().setVisible(true);
                return; // Exit if start or end tile not found
              }

              LinkedList<Tile> queue = new LinkedList<>(); // Queue of tiles to search
              queue.add(startTile); // Add starting tile to the queue

              parentMap.put(startTile, null); // Starting tile has no parent

              while (!queue.isEmpty()) {
                if (stopTimer()) {
                  Thread.currentThread().interrupt();
                  this.getBackButton().setVisible(true);
                  return;
                }
                Tile currentTile = queue.poll(); // Get the next tile from the queue
                  if (currentTile == null )return;
                // Check if current tile is the end tile
                if (currentTile == endTile) {
                  // Reconstruct and highlight the path
                  reconstructAndHighlightPath(parentMap, endTile);

                  // Repaint the UI after highlighting the path
                  SwingUtilities.invokeLater(this::repaint);

                  setShouldContinue(false);
                  // Interrupt the thread to stop it
                  Thread.currentThread().interrupt();
                  break; // Exit the loop if end tile is reached
                }

                for (Tile neighbor : findNeighboringTiles(currentTile, this.getTiles(), true)) {
                  // if the neighboring Tile isn't linked to the currentTile, it will link it
                  if (!parentMap.containsKey(neighbor)) {
                    // Set tile color and update parent map
                    float hue = (float) Math.random();
                    int rgb = Color.HSBtoRGB(hue, 0.3f, 0.5f);
                    neighbor.setTileColor(new Color(rgb));
                    parentMap.put(neighbor, currentTile);

                    // Add neighbor to the queue
                    queue.add(neighbor);
                  }
                }

                // Repaint the UI after processing each iteration
                SwingUtilities.invokeLater(this::repaint);

                try {
                  Thread.sleep(10); // Sleep for 10 milliseconds
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  setShouldContinue(false);
                }
              }
              this.getBackButton().setVisible(true);
                setShouldContinue(false);
              Thread.currentThread().interrupt();
            });

        algorithmThread.start(); // Start the algorithm thread
    }


    private void reconstructAndHighlightPath(Map<Tile, Tile> parentMap, Tile endTile) {
              Tile currentTile = endTile;
              this.getBackButton().setVisible(true);
              while (currentTile != null) {
                currentTile.setTileColor(Color.GREEN); // Set tile color to highlight the path
                currentTile = parentMap.get(currentTile); // Move to the parent tile
              }
              this.getEndTile().setTileColor(Color.RED);
    }

    



}
