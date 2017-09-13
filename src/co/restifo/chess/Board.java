package co.restifo.chess;

public class Board {
    // Pieces, represented as a 2-member array. Index 0 is white pieces and index 1 is black
    private long[] mPawns   = {0b0000000011111111000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000001111111100000000L};
    private long[] mRooks   = {0b1000000100000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000010000001L};
    private long[] mKnights = {0b0100001000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000001000010L};
    private long[] mBishops = {0b0010010000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000000100100L};
    private long[] mQueen   = {0b0001000000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000000010000L};
    private long[] mKing    = {0b0000100000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000000001000L};
    private int mSide = 0;
    
    public long getPawns() {
        return mPawns[mSide];
    }

    public long getRooks() {
        return mRooks[mSide];
    }

    public long getKnights() {
        return mKnights[mSide];
    }

    public long getBishops() {
        return mBishops[mSide];
    }

    public long getQueens() {
        return mQueen[mSide];
    }

    public long getKings() {
        return mKing[mSide];
    }

    public long getWhitePieces() { return mPawns[0] | mRooks[0] | mKnights[0] | mBishops[0] | mQueen[0] | mKing[0]; }

    public long getBlackPieces() { return mPawns[1] | mRooks[1] | mKnights[1] | mBishops[1] | mQueen[1] | mKing[1]; }
    
    public void switchSide() { mSide ^= 1; }
    public void switchSide(int side) { mSide = side; }
}
