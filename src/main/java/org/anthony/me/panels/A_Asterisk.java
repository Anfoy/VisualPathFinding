package org.anthony.me.panels;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.anthony.me.Frame;
import org.anthony.me.PanelManager;
import org.anthony.me.tiles.CriticalTile;
import org.anthony.me.tiles.Tile;

public class A_Asterisk extends BasePanel {

    public A_Asterisk(Dimension panelDimension, PanelManager panelManager, LayoutManager layoutManager, int id, Frame frame) {
        super(panelDimension, panelManager, layoutManager, id, frame);
    }

    @Override
    protected void displayAlgorithm() {
        this.setAlgorithmStarted(true);
        setShouldContinue(true);
        this.setEndTile((CriticalTile) findEndTile(getTiles()));
        this.setStartTile((CriticalTile) findStartTile(getTiles()));
findPath(getStartTile(), getEndTile());
    }

    @Override
    protected void uploadGrid(){
        this.uploadGrid(true, 200, true);
    }

    public void findPath(Tile start, Tile goal) {
    Thread algorithmThread =
        new Thread(
            () -> {
              Map<Tile, Tile> cameFrom = new HashMap<>();
              Map<Tile, Double> gScore = new HashMap<>();
              Map<Tile, Double> fScore = new HashMap<>();
              PriorityQueue<Tile> openSet =
                  new PriorityQueue<>(Comparator.comparingDouble(fScore::get));

              gScore.put(start, 0.0);
              fScore.put(start, heuristicEstimate(start, goal));
              openSet.add(start);

              while (!openSet.isEmpty()) {
                  if (!isShouldContinue() || Thread.currentThread().isInterrupted()) {
                      return; // Exit if start or end tile not found
                  }
                Tile current = openSet.poll();
                  if (current == null) return;
                if (current.equals(goal)) {
                  reconstructPath(cameFrom, current);
                  return;
                }

                for (Tile neighbor : findNeighboringTiles(current, this.getTiles(), true)) {
                  double tentativeGScore =
                      gScore.getOrDefault(current, Double.MAX_VALUE)
                          + distanceBetween(current, neighbor);
                  if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, gScore.get(neighbor) + heuristicEstimate(neighbor, goal));
                    if (!openSet.contains(neighbor)) {
                        // Inside findPath method, before adding the neighbor to openSet
                        float hue = (float) Math.random();
                        int rgb = Color.HSBtoRGB(hue, 0.3f, 0.5f);
                        neighbor.setTileColor(new Color(rgb));
                        SwingUtilities.invokeLater(this::repaint);
                        try {
                            Thread.sleep(10); // Sleep for a short duration to visualize the process
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            setShouldContinue(false);
                        }

                        openSet.add(neighbor);
                    }
                  }
                }
              }
                this.getBackButton().setVisible(true);
                setShouldContinue(false);
                Thread.currentThread().interrupt();
            });

                            algorithmThread.start(); // Start the algorithm thread
    }

    private void reconstructPath(Map<Tile, Tile> cameFrom, Tile current) {
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            current.setTileColor(Color.GREEN);
            SwingUtilities.invokeLater(this::repaint);
        }
        this.getBackButton().setVisible(true);
        this.getEndTile().setTileColor(Color.RED);
    }

    private double heuristicEstimate(Tile node, Tile goal) {
        return Math.sqrt(Math.pow(node.getxCoord() - goal.getxCoord(), 2) + Math.pow(node.getyCoord() - goal.getyCoord(), 2));
    }

    private double distanceBetween(Tile tile1, Tile tile2) {
        return Math.sqrt(Math.pow(tile1.getxCoord() - tile2.getxCoord(), 2) + Math.pow(tile1.getyCoord() - tile2.getyCoord(), 2));
    }

}
