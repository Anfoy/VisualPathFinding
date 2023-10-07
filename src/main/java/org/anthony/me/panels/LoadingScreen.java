package org.anthony.me.panels;


import java.awt.*;
import java.awt.event.ActionEvent;

import org.anthony.me.Frame;
import org.anthony.me.PanelManager;
import org.anthony.me.PanelSwitches;

public class LoadingScreen extends BasePanel {

  public LoadingScreen(
      Dimension panelDimension,
      PanelManager panelManager,
      LayoutManager layoutManager,
      int id,
      Frame frame) {
    super(panelDimension, panelManager, layoutManager, id, frame);
    this.uploadGrid(false, 0, false);
  }

    @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() instanceof PanelSwitches buttonPressed) {
        FramedAbstraction framedPanel = this.getPanelManager().findMatchingPanelViaID(buttonPressed.getId());
      this.getFrame().getContentPane().removeAll();
      this.getFrame().getContentPane().add(framedPanel);
        this.getFrame().pack();
      framedPanel.setVisible(true);
      framedPanel.uploadGrid();
      framedPanel.displayAlgorithm();
    }
      }
        }
