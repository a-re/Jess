package co.restifo.chess.gui;

import co.restifo.chess.engine.Board;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PiecePanel extends BasePanel {
    // Piece images are here
    private BufferedImage mBP, mWP;
    private BufferedImage mBR, mWR;
    private BufferedImage mBN, mWN;
    private BufferedImage mBB, mWB;
    private BufferedImage mBQ, mWQ;
    private BufferedImage mBK, mWK;

    private final int mRectHeight = BasePanel.getRectHeight(), mRectWidth = BasePanel.getRectWidth();
    private Map<int[], BufferedImage> mPieceImages;
    private List<Rectangle> mRectsToDraw;
    private Board board; // The main game board, this variable might change location later

    public PiecePanel() {
        // Initialize our game board
        board = new Board();

        // Read all of our piece images in
        mPieceImages = new HashMap<>();
        mRectsToDraw = new ArrayList<>();
        try {
            mBP = ImageIO.read(new File("res/bp.png"));
            mWP = ImageIO.read(new File("res/wp.png"));
            mBR = ImageIO.read(new File("res/br.png"));
            mWR = ImageIO.read(new File("res/wr.png"));
            mBN = ImageIO.read(new File("res/bn.png"));
            mWN = ImageIO.read(new File("res/wn.png"));
            mBB = ImageIO.read(new File("res/bb.png"));
            mWB = ImageIO.read(new File("res/wb.png"));
            mBQ = ImageIO.read(new File("res/bq.png"));
            mWQ = ImageIO.read(new File("res/wq.png"));
            mBK = ImageIO.read(new File("res/bk.png"));
            mWK = ImageIO.read(new File("res/wk.png"));
        } catch (IOException e) {
            System.err.println("Error loading images");
            System.exit(1);
        }
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int col = e.getX() / mRectWidth;
                int row = e.getY() / mRectHeight;
                mRectsToDraw.add(new Rectangle(col * mRectWidth, row * mRectHeight, mRectWidth, mRectHeight));
                repaint();
            }
        });
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("paintComponent() in PiecePanel called");
        Graphics2D gr = (Graphics2D) g;

        int oldSide = board.getSide();
        board.switchSide(0); // This makes sure we draw white pieces first (is this necessary?)
        mPieceImages.put(getBitIdxs(board.getRooks()), mWR);
        mPieceImages.put(getBitIdxs(board.getKnights()), mWN);
        mPieceImages.put(getBitIdxs(board.getBishops()), mWB);
        mPieceImages.put(getBitIdxs(board.getQueens()), mWQ);
        mPieceImages.put(getBitIdxs(board.getKings()), mWK);
        mPieceImages.put(getBitIdxs(board.getPawns()), mWP);
        board.switchSide(1); // Now switch to black pieces
        mPieceImages.put(getBitIdxs(board.getRooks()), mBR);
        mPieceImages.put(getBitIdxs(board.getKnights()), mBN);
        mPieceImages.put(getBitIdxs(board.getBishops()), mBB);
        mPieceImages.put(getBitIdxs(board.getQueens()), mBQ);
        mPieceImages.put(getBitIdxs(board.getKings()), mBK);
        mPieceImages.put(getBitIdxs(board.getPawns()), mBP);
        board.switchSide(oldSide); // restore the old side

        // Draw the pieces
        Iterator it = mPieceImages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            BufferedImage pieceImage = (BufferedImage) entry.getValue();
            int[] piecePos = (int[]) entry.getKey();

            for (int piece : piecePos) {
                if (piece != -1) {
                    int[] rc = getRowCol(piece);
                    gr.drawImage(pieceImage, rc[1] * mRectWidth + (mRectWidth - pieceImage.getWidth()) / 2,
                                             rc[0] * mRectHeight + (mRectHeight - pieceImage.getHeight()) / 2, this);
                }
            }
        }

        // Draw the selection rectangles surrounding the pieces if we need to
        if (mRectsToDraw.size() > 0) {
            Stroke oldStroke = gr.getStroke(); // First save our old stroke so it's not permanently thiccccc ;)
            gr.setStroke(new BasicStroke(2)); // Set the stroke to be thicker
            gr.setColor(Color.RED);
            for (int i = 0; i < mRectsToDraw.size(); i++) {
                Rectangle r = mRectsToDraw.remove(i);
                gr.drawRect(r.x, r.y, r.width, r.height);
            }
            gr.setStroke(oldStroke);
        }
    }

    // TODO: This is really terrible, we don't need to check each bit
    private int[] getBitIdxs(long bitboard) {
        int[] ret = {-1, -1, -1, -1, -1, -1, -1, -1};
        int count = 0;
        for (int i = 0; bitboard != 0; i++) {
            if ((bitboard & 1) == 1) {
                ret[count] = i;
                count++;
            }
            bitboard >>>= 1;
        }
        return ret;
    }

    private int[] getRowCol(int index) {
        return new int[] {index / 8, index % 8};
    }
}
