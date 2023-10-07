package org.anthony.me.tiles;

import java.awt.*;

/**
 * Tile used to mark start and end points for algorithm
 */
public class CriticalTile extends Tile {


    private final boolean isEnd;

    public CriticalTile(int xPos, int yPos, Dimension d, Color color, boolean isEnd) {
        super(xPos, yPos, d, color);
        this.isEnd = isEnd;
    }

    public boolean isEnd() {
        return isEnd;
    }


}
