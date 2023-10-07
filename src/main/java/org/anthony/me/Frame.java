package org.anthony.me;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.anthony.me.panels.FramedAbstraction;

public class Frame extends JFrame implements ActionListener {


    //Width of screen 816
    public static final int SCREEN_WIDTH = 816;

    //Height of screen 839
    public static final int SCREEN_HEIGHT = 839;

    private final PanelManager panelManager;

  public Frame() {
    this.panelManager = new PanelManager(this);

    this.setTitle("PathFinding");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
      this.setUndecorated(false);

    this.getContentPane().add(panelManager.findMatchingPanelViaID(1));
      this.pack();

    this.setResizable(false);
    this.setVisible(true);

    // Sets location of frame in middle of screen
    this.setLocationRelativeTo(null);
    this.validate();
        }


    @Override
    public void actionPerformed(ActionEvent e) {
      for (FramedAbstraction customPanel : panelManager.getPanels()){
          customPanel.actionPerformed(e);

      }
    }
}
