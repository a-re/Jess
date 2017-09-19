package co.restifo.chess.gui;

import java.awt.Color;
import java.awt.Graphics;

public class CheckboardPanel extends BasePanel {
    @Override
    public void paintComponent(Graphics gr) {
        // Paint a checkerboard
        Color curColor = Color.WHITE;
        int w = getRectWidth(), h = getRectHeight();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                gr.setColor(curColor);
                gr.fillRect(x * w, y * h, w, h);
                curColor = curColor == Color.WHITE ? Color.DARK_GRAY : Color.WHITE;
            }
            curColor = curColor == Color.WHITE ? Color.DARK_GRAY : Color.WHITE;
        }
    }
}
