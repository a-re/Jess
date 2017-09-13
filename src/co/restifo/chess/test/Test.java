package co.restifo.chess.test;


public class Test {
    private long[] mOccupancyMaskRook = new long[64];
    private long[] mOccupancyMaskBishop = new long[64];

    private boolean mTestDirectionality = false;
    private boolean mTestRookOccupancyMasksInitialization = false;
    private boolean mTestBishopOccupancyMasksInitialization = false;

    private final Direction[] mRookDirections = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private final Direction[] mBishopDirections = {Direction.NORTHEAST, Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.NORTHWEST};

    private Test() {
        mOccupancyMaskRook = generateOccupancyMasks(mRookDirections);
        mOccupancyMaskBishop = generateOccupancyMasks(mBishopDirections);

        if (mTestDirectionality) testDirectionality();
        if (mTestRookOccupancyMasksInitialization) {
            for (long l : mOccupancyMaskRook) {
                printBitboard(l, "R");
                System.out.println();
            }
        }
        if (mTestBishopOccupancyMasksInitialization) {
            for (long l : mOccupancyMaskBishop) {
                printBitboard(l, "B");
                System.out.println();
            }
        }
    }

    private long[] generateOccupancyMasks(Direction[] directions) {
        long[] ret = new long[64];
        for (int i = 0; i < 64; i++) {
            for (Direction d : directions) {
                for (int nextDirIndex = d.nextIndex(i); nextDirIndex != -1; nextDirIndex = d.nextIndex(nextDirIndex)) {
                    ret[i] |= 1L << nextDirIndex;
                }
            }
        }
        return ret;
    }

    //<editor-fold desc="Tests">
    private void testDirectionality() {
        long testbb = 0b0000000000000000000000000000100000000000000000000000000000000000L;
        printBitboard(testbb, "R");
        System.out.println();

        long northbb = testbb | (1L << Direction.NORTH.nextIndex(getBitIdxs(testbb)[0]));
        long northeastbb = testbb | (1L << Direction.NORTHEAST.nextIndex(getBitIdxs(testbb)[0]));
        long eastbb = testbb | (1L << Direction.EAST.nextIndex(getBitIdxs(testbb)[0]));
        long southeastbb = testbb | (1L << Direction.SOUTHEAST.nextIndex(getBitIdxs(testbb)[0]));
        long southbb = testbb | (1L << Direction.SOUTH.nextIndex(getBitIdxs(testbb)[0]));
        long southwestbb = testbb | (1L << Direction.SOUTHWEST.nextIndex(getBitIdxs(testbb)[0]));
        long westbb = testbb | (1L << Direction.WEST.nextIndex(getBitIdxs(testbb)[0]));
        long northwestbb = testbb | (1L << Direction.NORTHWEST.nextIndex(getBitIdxs(testbb)[0]));

        printBitboard(northbb, "R");
        System.out.println();

        printBitboard(northeastbb, "R");
        System.out.println();

        printBitboard(eastbb, "R");
        System.out.println();

        printBitboard(southeastbb, "R");
        System.out.println();

        printBitboard(southbb, "R");
        System.out.println();

        printBitboard(southwestbb, "R");
        System.out.println();

        printBitboard(westbb, "R");
        System.out.println();

        printBitboard(northwestbb, "R");
        System.out.println();
    }
    //</editor-fold>

    public void printBitboard(long bitboard, String piece) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++, bitboard >>>= 1) System.out.print((bitboard & 1) == 1 ? piece + " " : ". ");
            System.out.println();
        }
    }

    private enum Direction {
        NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

        private int mRowShift;
        private int mColShift;
        static {
            NORTH.mRowShift = -1;
            NORTH.mColShift = 0;
            NORTHEAST.mRowShift = -1;
            NORTHEAST.mColShift = 1;
            EAST.mRowShift = 0;
            EAST.mColShift = 1;
            SOUTHEAST.mRowShift = 1;
            SOUTHEAST.mColShift = 1;
            SOUTH.mRowShift = 1;
            SOUTH.mColShift = 0;
            SOUTHWEST.mRowShift = 1;
            SOUTHWEST.mColShift = -1;
            WEST.mRowShift = 0;
            WEST.mColShift = -1;
            NORTHWEST.mRowShift = -1;
            NORTHWEST.mColShift = -1;
        }

        public int nextIndex(int index) {
            int newRow = getRow(index) + mRowShift;
            int newCol = getCol(index) + mColShift;
            if (newRow > 7 || newRow < 0 || newCol > 7 || newCol < 0) return -1;
            return 8 * newRow + newCol;
        }

        private int getRow(int index) { return index / 8; }
        private int getCol(int index) { return index % 8; }
    }

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

    public static void main(String[] args) {
        new Test();
    }
}
