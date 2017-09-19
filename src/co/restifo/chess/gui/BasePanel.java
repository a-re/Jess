package co.restifo.chess.gui;


import co.restifo.chess.engine.Board;

import javax.swing.JPanel;
import java.awt.Dimension;

public class BasePanel extends JPanel {

//    private Board mBoard;
    private static final Dimension mWindowSize = new Dimension(600, 600);
    private static final int mRectWidth = mWindowSize.width / 8;
    private static final int mRectHeight = mWindowSize.height / 8;

    public BasePanel() {
//        mBoard = new Board();
    }

    // Getters
//    public Board getBoard() { return mBoard; }
    public static Dimension getWindowSize() { return mWindowSize; }
    public static int getRectWidth() { return mRectWidth; }
    public static int getRectHeight() { return mRectHeight; }
}
