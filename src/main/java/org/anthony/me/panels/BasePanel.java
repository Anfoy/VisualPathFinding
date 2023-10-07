package org.anthony.me.panels;


import static org.anthony.me.PanelManager.BUTTON_DIMENSION;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.Timer;
import org.anthony.me.Frame;
import org.anthony.me.PanelManager;
import org.anthony.me.PanelSwitches;
import org.anthony.me.tiles.CriticalTile;
import org.anthony.me.tiles.Tile;

public class BasePanel extends FramedAbstraction {

  // Arraylist of tiles that make up the panel
  private final ArrayList<Tile> tiles;

  // id for panel(used to know which panel should be displayed)

  private final Frame frame;

    private boolean algorithmStarted;

  private final PanelManager panelManager;

  private CriticalTile endTile;

  private CriticalTile startTile;

  private final PanelSwitches backButton;

  private boolean shouldContinue = true;

  private final int id;

  private final Timer timer;
  private int timerVar;

  public BasePanel(
      Dimension panelDimension,
      PanelManager panelManager,
      LayoutManager layoutManager,
      int id,
      Frame frame) {
    timer = new Timer(1000, this);
    timerVar =0;
    tiles = new ArrayList<>();
      this.id = id;
    this.algorithmStarted = false;
    this.panelManager = panelManager;
    this.backButton = createButton("Back", 1, BUTTON_DIMENSION, 10, 10, frame);
    this.backButton.setVisible(false);
    this.frame = frame;
    setPreferredSize(panelDimension);
    this.setBackground(Color.PINK);
    this.setLayout(layoutManager);
    this.validate();
    panelManager.addToPanelList(this);
  }

  public void uploadGrid(boolean walls, int amount, boolean addCriticals) {
    this.uploadTileGrid(tiles, walls, amount, addCriticals);
    this.revalidate();
  }

  @Override
  public int getAccesserID() {
    return id;
  }

  @Override
  public void paintComponent(Graphics g) {
    for (Tile tile : tiles) {
      tile.paint(g);
    }
  }

  @Override
  protected void displayAlgorithm() {}

  @Override
  protected void uploadGrid() {}

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer){
      timerVar++;
    }
      if (e.getSource() instanceof PanelSwitches buttonPressed) {
      if (buttonPressed.getId() == 1) {
        if (isShouldContinue()) {
          setShouldContinue(false);
        }
        this.setAlgorithmStarted(false);
        this.getBackButton().setVisible(false);
      }
      FramedAbstraction framedPanel =
          this.getPanelManager().findMatchingPanelViaID(buttonPressed.getId());
      this.getFrame().getContentPane().removeAll();
      this.getFrame().getContentPane().add(framedPanel);
      framedPanel.repaint();
      this.getFrame().pack();
      framedPanel.setVisible(true);
    }
  }

  public void startTimer(){
    timerVar = 0;
    timer.start();
  }

  public boolean stopTimer(){
    if (timerVar >= 15){
      timer.stop();
      return true;
    }
    return false;
  }



  public ArrayList<Tile> getTiles() {
    return tiles;
  }

  public Frame getFrame() {
    return frame;
  }

  public boolean isAlgorithmStarted() {
    return algorithmStarted;
  }

  public void setAlgorithmStarted(boolean algorithmStarted) {
    this.algorithmStarted = algorithmStarted;
  }

  public PanelManager getPanelManager() {
    return panelManager;
  }

  public CriticalTile getEndTile() {
    return endTile;
  }

  public void setEndTile(CriticalTile endTile) {
    this.endTile = endTile;
  }

  public CriticalTile getStartTile() {
    return startTile;
  }

  public void setStartTile(CriticalTile startTile) {
    this.startTile = startTile;
  }

  public PanelSwitches getBackButton() {
    return backButton;
  }

  public boolean isShouldContinue() {
    return shouldContinue;
  }

  public void setShouldContinue(boolean shouldContinue) {
    this.shouldContinue = shouldContinue;
  }

    }

