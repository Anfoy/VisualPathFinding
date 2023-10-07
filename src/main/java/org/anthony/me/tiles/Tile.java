package org.anthony.me.tiles;

import javax.swing.*;
import java.awt.*;

/**
 * Tile class for on screen tiles to show how algorithm thinks
 */
public class Tile extends JComponent {

    private final int xCoord;

    private final int yCoord;

    private final Dimension dimension;

    private final int width;

    private final int height;

    private  Color  tileColor;


    public Tile(int xPos, int yPos, Dimension d, Color color){
        this.xCoord = xPos;
        this.yCoord = yPos;
        this.dimension = d;
        this.tileColor = color;
        this.width = dimension.width;
        this.height = dimension.height;
    }


    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getTileColor());
        g2.fillRect(getxCoord(),getyCoord(), getWidth(),getHeight());
        g2.drawRect(getxCoord(), getyCoord(), getWidth(), getHeight());
    }


    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setTileColor(Color color){
        this.tileColor = color;
    }

    public Color getTileColor() {
        return tileColor;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
