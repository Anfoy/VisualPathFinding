package org.anthony.me;
import java.awt.*;

public class PanelSwitches extends Button {

    private final int id;

    private final String displayText;
    public PanelSwitches(String displayText, int id, Dimension size){
        this.setPreferredSize(size);
        this.setLabel(displayText);
        this.id = id;
        this.displayText = displayText;
        this.setFocusable(false);
        this.setForeground(Color.WHITE);
        this.setBackground(Color.GRAY);
    this.setFont(new Font("Comic Sans", Font.BOLD,25));
    }

    public int getId() {
        return id;
    }

    public String getDisplayText() {
        return displayText;
    }
}
