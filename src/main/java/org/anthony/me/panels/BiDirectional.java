package org.anthony.me.panels;

import org.anthony.me.Frame;
import org.anthony.me.PanelManager;
import org.anthony.me.tiles.CriticalTile;
import org.anthony.me.tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BiDirectional extends  BasePanel{


    private Tile meetingTile;

    public BiDirectional(Dimension panelDimension, PanelManager panelManager, LayoutManager layoutManager, int id, Frame frame) {
        super(panelDimension, panelManager, layoutManager, id, frame);
    }

    @Override
    protected void displayAlgorithm() {
        this.startTimer();
        this.setAlgorithmStarted(true);
        setShouldContinue(true);
        this.setEndTile((CriticalTile) findEndTile(getTiles()));
        this.setStartTile((CriticalTile) findStartTile(getTiles()));
        explorePath();
    }



    private void explorePath(){
    Thread algorithmThread =
        new Thread(
            () -> {
              // Initialize data structures for both directions
              Map<Tile, Tile> cameFromForward = new HashMap<>();
              Map<Tile, Double> gScoreForward = new HashMap<>();
              Map<Tile, Double> fScoreForward = new HashMap<>();
              PriorityQueue<Tile> openSetForward =
                  new PriorityQueue<>(Comparator.comparingDouble(fScoreForward::get));

              Map<Tile, Tile> cameFromBackward = new HashMap<>();
              Map<Tile, Double> gScoreBackward = new HashMap<>();
              Map<Tile, Double> fScoreBackward = new HashMap<>();
              PriorityQueue<Tile> openSetBackward =
                  new PriorityQueue<>(Comparator.comparingDouble(fScoreBackward::get));

              // Set initial values for both directions
              gScoreForward.put(getStartTile(), 0.0);
              fScoreForward.put(getStartTile(), heuristicEstimate(getStartTile(), getEndTile()));
              openSetForward.add(getStartTile());

              gScoreBackward.put(getEndTile(), 0.0);
              fScoreBackward.put(getEndTile(), heuristicEstimate(getEndTile(), getStartTile()));
              openSetBackward.add(getEndTile());
              // Main loop for bidirectional search
              while (!openSetForward.isEmpty() && !openSetBackward.isEmpty()) {
                // Check if start or end tile not found
                if (!isShouldContinue() || Thread.currentThread().isInterrupted()) {
                  return; // Exit if start or end tile not found
                }
                if (stopTimer()) {
                  Thread.currentThread().interrupt();
                  this.getBackButton().setVisible(true);
                  return;
                }
                // Forward search
                Tile currentForward = openSetForward.poll();
                if (currentForward == null) return;

                if (openSetBackward.contains(currentForward)) {
                  // The two searches have met
                  this.meetingTile = currentForward;
                  Thread.currentThread().interrupt();
                  break;
                }

                // Update forward search data structures and evaluate neighbors
                for (Tile neighbor : findNeighboringTiles(currentForward, getTiles(), true)) {
                  double tentativeGScore =
                      gScoreForward.getOrDefault(currentForward, Double.MAX_VALUE)
                          + distanceBetween(currentForward, neighbor);
                  if (tentativeGScore < gScoreForward.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFromForward.put(neighbor, currentForward);
                    gScoreForward.put(neighbor, tentativeGScore);
                    fScoreForward.put(
                        neighbor,
                        gScoreForward.get(neighbor) + heuristicEstimate(neighbor, getEndTile()));
                    if (!openSetForward.contains(neighbor)) {
                      float hue = (float) Math.random();
                      int rgb = Color.HSBtoRGB(hue, 0.3f, 0.5f);
                      neighbor.setTileColor(new Color(rgb));
                      SwingUtilities.invokeLater(this::repaint);
                      openSetForward.add(neighbor);
                    }
                  }
                }
                // Backward search
                Tile currentBackward = openSetBackward.poll();
                if (currentBackward == null) return;
                if (openSetForward.contains(currentBackward)) {
                  // The two searches have met
                  this.meetingTile = currentBackward;
                  Thread.currentThread().interrupt();
                  break;
                }

                // Update backward search data structures and evaluate neighbors
                for (Tile neighbor : findNeighboringTiles(currentBackward, getTiles(), true)) {
                  double tentativeGScore =
                      gScoreBackward.getOrDefault(currentBackward, Double.MAX_VALUE)
                          + distanceBetween(currentBackward, neighbor);
                  if (tentativeGScore < gScoreBackward.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFromBackward.put(neighbor, currentBackward);
                    gScoreBackward.put(neighbor, tentativeGScore);
                    fScoreBackward.put(
                        neighbor,
                        gScoreBackward.get(neighbor) + heuristicEstimate(neighbor, getStartTile()));
                    if (!openSetBackward.contains(neighbor)) {
                      // Inside findPath method, before adding the neighbor to openSet
                      float hue = (float) Math.random();
                      int rgb = Color.HSBtoRGB(hue, 0.3f, 0.5f);
                      neighbor.setTileColor(new Color(rgb));
                      SwingUtilities.invokeLater(this::repaint);
                      try {
                        Thread.sleep(10); // Sleep for a short duration to visualize the process
                      } catch (InterruptedException e) {
                        setShouldContinue(false);
                      }
                      openSetBackward.add(neighbor);
                    }
                  }
                }
              }
              if (this.meetingTile != null) {
                List<Tile> path =
                    reconstructBidirectionalPath(
                        this.meetingTile, cameFromForward, cameFromBackward);
                displayPath(path);
              }else{
                  this.getBackButton().setVisible(true);
              }

            });
        algorithmThread.start();
    }

    @Override
    protected void uploadGrid(){
        this.uploadGrid(true, 200, true);
    }



    // Inside the displayPath method
    private void displayPath(List<Tile> path) {
        for (Tile tile : path){
            if (tile.getxCoord() == getEndTile().getxCoord() && tile.getyCoord() == getEndTile().getyCoord()) continue;

            SwingUtilities.invokeLater(() -> tile.setTileColor(Color.GREEN)); // Update tile color on EDT
        }
        this.meetingTile.setTileColor(Color.GREEN);
        SwingUtilities.invokeLater(this::repaint);
        this.getBackButton().setVisible(true);
    }

    private List<Tile> reconstructBidirectionalPath(Tile meetingTile, Map<Tile, Tile> cameFromForward, Map<Tile, Tile> cameFromBackward) {
        List<Tile> path = new ArrayList<>();
        Tile current = meetingTile;

        // Reconstruct the path from start to meeting point
        while (cameFromForward.containsKey(current)) {
            current = cameFromForward.get(current);
            path.add(current);
        }

        Collections.reverse(path); // Reverse to get the correct order
        current = meetingTile;

        // Reconstruct the path from meeting point to goal
        while (cameFromBackward.containsKey(current)) {
            current = cameFromBackward.get(current);
            path.add(current);
        }

        return path;
    }
    private double heuristicEstimate(Tile node, Tile goal) {
        return Math.sqrt(Math.pow(node.getxCoord() - goal.getxCoord(), 2) + Math.pow(node.getyCoord() - goal.getyCoord(), 2));
    }

    private double distanceBetween(Tile tile1, Tile tile2) {
        return Math.sqrt(Math.pow(tile1.getxCoord() - tile2.getxCoord(), 2) + Math.pow(tile1.getyCoord() - tile2.getyCoord(), 2));
    }
}
