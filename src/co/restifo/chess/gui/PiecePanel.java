package co.restifo.chess.gui;

import co.restifo.chess.engine.Board;
import co.restifo.chess.engine.ChessGame;
import co.restifo.chess.engine.NormalMove;
import co.restifo.chess.engine.PieceType;

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
    private Map<List<Integer>, BufferedImage> mPieceImages;
    private List<ColoredRectangle> mMoveRectangles;
    private Rectangle mSelectionRectangle;
    private int mSelectedSquare;
    private ChessGame game; // The main game board, this variable might change location later

    public PiecePanel() {
        // Set up our current game and the move rectangle array
        game = new ChessGame("rnbq1rk1/ppp1b1pp/4pn2/5p2/2Pp4/5NP1/PP2PPBP/RNBQ1RK1 w - -");
//        game = new ChessGame();
        mMoveRectangles = new ArrayList<>();

        // Read all of our piece images in
        mPieceImages = new HashMap<>();
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

                mMoveRectangles.clear();
                mSelectedSquare = row * 8 + col;
                Board b = game.getBoard();
                PieceType pieceOnSquare = b.getPieceOnSquare(mSelectedSquare);
                System.out.println(pieceOnSquare);
                for (NormalMove m : b.getPseudolegalMoves(b.getAllPieces(), b.getPieces(), mSelectedSquare, pieceOnSquare)) {
                    Color pieceColor = Color.GREEN;
                    int moveRow = m.getToSquare() / 8;
                    int moveCol = m.getToSquare() % 8;
                    mMoveRectangles.add(new ColoredRectangle(moveCol * mRectWidth + 1, moveRow * mRectHeight + 1, mRectWidth - 1, mRectHeight - 1, pieceColor));
                }
                mSelectionRectangle = new Rectangle(col * mRectWidth, row * mRectHeight, mRectWidth, mRectHeight);
                repaint();
            }
        });

        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("paintComponent() in PiecePanel called");
        Graphics2D gr = (Graphics2D) g;
        Board board = game.getBoard();

        int oldSide = board.getSide();
        board.switchSide(0); // This makes sure we draw white pieces first (is this necessary?)
        mPieceImages.put(board.getBitIdxs(board.getRooks()), mWR);
        mPieceImages.put(board.getBitIdxs(board.getKnights()), mWN);
        mPieceImages.put(board.getBitIdxs(board.getBishops()), mWB);
        mPieceImages.put(board.getBitIdxs(board.getQueens()), mWQ);
        mPieceImages.put(board.getBitIdxs(board.getKings()), mWK);
        mPieceImages.put(board.getBitIdxs(board.getPawns()), mWP);
        board.switchSide(1); // Now switch to black pieces
        mPieceImages.put(board.getBitIdxs(board.getRooks()), mBR);
        mPieceImages.put(board.getBitIdxs(board.getKnights()), mBN);
        mPieceImages.put(board.getBitIdxs(board.getBishops()), mBB);
        mPieceImages.put(board.getBitIdxs(board.getQueens()), mBQ);
        mPieceImages.put(board.getBitIdxs(board.getKings()), mBK);
        mPieceImages.put(board.getBitIdxs(board.getPawns()), mBP);
        board.switchSide(oldSide); // restore the old side

        // Draw the pieces
        Iterator it = mPieceImages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            BufferedImage pieceImage = (BufferedImage) entry.getValue();
            List<Integer> piecePos = (List<Integer>) entry.getKey();

            for (int piece : piecePos) {
                if (piece != -1) {
                    int[] rc = getRowCol(piece);
                    gr.drawImage(pieceImage, rc[1] * mRectWidth + (mRectWidth - pieceImage.getWidth()) / 2,
                                             rc[0] * mRectHeight + (mRectHeight - pieceImage.getHeight()) / 2, this);
                }
            }
        }

        // Draw the selection rectangle surrounding the selected square if we need to
        if (mSelectionRectangle != null) {
            Stroke oldStroke = gr.getStroke(); // First save our old stroke so it's not permanently thiccccc ;)
            gr.setStroke(new BasicStroke(2)); // Set the stroke to be thicker
            gr.setColor(Color.RED);
            gr.drawRect(mSelectionRectangle.x, mSelectionRectangle.y, mSelectionRectangle.width, mSelectionRectangle.height);
            gr.setStroke(oldStroke);
            mSelectionRectangle = null;
        }

        if (mMoveRectangles.size() > 0) {
            Stroke oldStroke = gr.getStroke(); // First save our old stroke so it's not permanently thiccccc ;)
            gr.setStroke(new BasicStroke(2)); // Set the stroke to be thicker
            for (ColoredRectangle moveRect : mMoveRectangles) {
                gr.setColor(moveRect.color);
                gr.drawRect(moveRect.x, moveRect.y, moveRect.width, moveRect.height);
            }
            gr.setStroke(oldStroke);
        }
    }

    private int[] getRowCol(int index) {
        return new int[] {index / 8, index % 8};
    }

    // So we can specify the color of a rectangle with one class
    class ColoredRectangle extends Rectangle {
        Color color;
        ColoredRectangle(int x, int y, int width, int height, Color color) {
            super(x, y, width, height);
            this.color = color;
        }
    }
}
