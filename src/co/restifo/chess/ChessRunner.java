package co.restifo.chess;

import co.restifo.chess.gui.BasePanel;
import co.restifo.chess.gui.CheckboardPanel;
import co.restifo.chess.gui.PiecePanel;

import javax.swing.*;
import java.awt.*;

public class ChessRunner {
    public static void main(String[] args) {
        JFrame window = new JFrame("Chess");
        Dimension dim = BasePanel.getWindowSize();

        // Make our panels
        JLayeredPane layeredPane = new JLayeredPane();
        CheckboardPanel checkboardPanel = new CheckboardPanel();
        PiecePanel piecePanel = new PiecePanel();

        layeredPane.setPreferredSize(dim);
        layeredPane.setBounds(0, 0, dim.width, dim.height);
        checkboardPanel.setBounds(0, 0, dim.width, dim.height);
        checkboardPanel.setOpaque(false);
        piecePanel.setBounds(0, 0, dim.width, dim.height);
        piecePanel.setOpaque(false);

        layeredPane.add(checkboardPanel, 1);
        layeredPane.add(piecePanel, 0);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setContentPane(layeredPane);
        window.setSize(dim);
        window.pack();

        window.setVisible(true);
//        new Thread(() -> {
//            try {
//                panel.repaint();
//                Thread.sleep(1000 / 30);
//            } catch (InterruptedException e) {
//                // Do nothing
//            }
//        }).run();
//    }
    }
}