package co.restifo.chess.engine;

public class Board {
    // Pieces, represented as a 2-member array. Index 0 is white pieces and index 1 is black
    private long[] mPawns   = {0b0000000011111111000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000001111111100000000L};
    private long[] mRooks   = {0b1000000100000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000010000001L};
    private long[] mKnights = {0b0100001000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000001000010L};
    private long[] mBishops = {0b0010010000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000000100100L};
    private long[] mQueen   = {0b0001000000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000000010000L};
    private long[] mKing    = {0b0000100000000000000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000000000000001000L};
    private long[] mPieces = {mPawns[0] | mRooks[0] | mKnights[0] | mBishops[0] | mQueen[0] | mKing[0],
                                 mPawns[1] | mRooks[1] | mKnights[1] | mBishops[1] | mQueen[1] | mKing[1]};
    private int mSide = 0;

    // Blocker masks
    private long[] mBlockerMaskRook = new long[64];
    private long[] mBlockerMaskBishop = new long[64];
    private long[] mBlockerMaskQueen = new long[64];
    private long[] mBlockerMaskKing = new long[64];
    private long[] mBlockerMaskKnight = new long[64];
    private long[] mBlockerMaskWhitePawn = new long[64];
    private long[] mBlockerMaskBlackPawn = new long[64];

    // Directions for each piece type
    private final Direction[] mRookDirections = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private final Direction[] mBishopDirections = {Direction.NORTHEAST, Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.NORTHWEST};
    private final Direction[] mWhitePawnDirections = {Direction.NORTHEAST, Direction.NORTHWEST};
    private final Direction[] mBlackPawnDirections = {Direction.SOUTHEAST, Direction.SOUTHWEST};
    private final Direction[] mKnightDirections = {Direction.N_1, Direction.N_2, Direction.N_3, Direction.N_4,
            Direction.N_5, Direction.N_6, Direction.N_7, Direction.N_8};
    private final Direction[] mKingDirections = {Direction.NORTH, Direction.NORTHEAST, Direction.EAST, Direction.SOUTHEAST,
            Direction.SOUTH, Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST};
    private final Direction[] mQueenDirections = {Direction.NORTH, Direction.NORTHEAST, Direction.EAST, Direction.SOUTHEAST,
            Direction.SOUTH, Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST};

    public Board() {
        // Generate our occupancy masks
        long m = System.currentTimeMillis();
        mBlockerMaskRook = generateBlockerMask(mRookDirections, true);
        mBlockerMaskBishop = generateBlockerMask(mBishopDirections, true);
        mBlockerMaskQueen = generateBlockerMask(mQueenDirections, true);
        mBlockerMaskKing = generateBlockerMask(mKingDirections, false);
        mBlockerMaskKnight = generateBlockerMask(mKnightDirections, false);
        mBlockerMaskWhitePawn = generateBlockerMask(mWhitePawnDirections, false);
        mBlockerMaskBlackPawn = generateBlockerMask(mBlackPawnDirections, false);
        System.out.printf("Took %dms\n", System.currentTimeMillis() - m);
    }
    
    //<editor-fold desc="Getters/setters">
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

    public long getPieces() { return mPieces[mSide]; }

    public int getSide() { return mSide; }

    public void switchSide() { mSide ^= 1; }
    public void switchSide(int side) { mSide = side; }
    //</editor-fold>
    //<editor-fold desc="Occupancy mask initialization">
    private long[] generateBlockerMask(Direction[] directions, boolean isSliding) {
        long[] ret = new long[64];
        for (int i = 0; i < 64; i++) {
            if (isSliding) {
                for (Direction d : directions) {
                    int nextIndex = d.nextIndex(i);
                    if (nextIndex != -1) {
                        ret[i] |= 1L << nextIndex;
                    }
                }
                ret[i] &= ~0xff818181818181ffL; // clear the edges because they're always blockers
            } else {
                for (Direction d : directions) {
                    for (int nI = d.nextIndex(i); nI != -1; nI = d.nextIndex(nI)) {
                        ret[i] |= 1L << nI;
                    }
                }
            }
        }
        return ret;
    }
    //</editor-fold>
}
