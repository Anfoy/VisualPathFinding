package org.anthony.me;

import static org.anthony.me.Frame.SCREEN_HEIGHT;
import static org.anthony.me.Frame.SCREEN_WIDTH;

import java.awt.*;
import java.util.ArrayList;

import org.anthony.me.panels.*;

public class PanelManager {

  // Arraylist of panels that can be used
  public final ArrayList<FramedAbstraction> panels;

    //Dimension object for Panels
    public  static final Dimension PANEL_DIMENSION = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);

    //Dimension object for rectangles
    public static final Dimension RECT_DIMENSION = new Dimension(25, 25);

    public static final Dimension BUTTON_DIMENSION = new Dimension(100, 50);

    public PanelManager(Frame frame){
        panels = new ArrayList<>();
    LoadingScreen LoadingScreen =
        new LoadingScreen(PANEL_DIMENSION, this, new FlowLayout(), 1, frame);
    BFS BFS = new BFS(PANEL_DIMENSION, this, null, 2, frame);
        DFS MAG = new DFS(PANEL_DIMENSION, this, null, 3, frame);
        A_Asterisk AAS = new A_Asterisk(PANEL_DIMENSION, this, null, 4, frame);
        BiDirectional BID = new BiDirectional(PANEL_DIMENSION, this, null, 5, frame);
       LoadingScreen.createButton("Breadth First Search", 2, new Dimension(250, 50), 10, 10, frame);
        LoadingScreen.createButton("Depth First Search", 3, new Dimension(250, 50), 10, 60, frame);
        LoadingScreen.createButton("A* Search", 4, new Dimension(250, 50), 10, 60, frame);
        LoadingScreen.createButton("BiDirectional Search", 5, new Dimension(250, 50), 10, 60, frame);
    }

    public void addToPanelList(FramedAbstraction panel){
        panels.add(panel);
    }

    public ArrayList<FramedAbstraction> getPanels() {
        return panels;
    }

    public FramedAbstraction findMatchingPanelViaID(int id){
        for (FramedAbstraction panel : panels){
            if (panel.getAccesserID() != id) continue;
            return panel;
        }
        return null;
    }


}
