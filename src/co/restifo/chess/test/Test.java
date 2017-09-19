package co.restifo.chess.test;


import co.restifo.chess.engine.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.System.out;

public class Test {
    private long[] mOccupancyMaskRook = new long[64];
    private long[] mOccupancyMaskBishop = new long[64];
    private long[] mOccupancyMaskQueen = new long[64];
    private long[] mOccupancyMaskKing = new long[64];
    private long[] mOccupancyMaskKnight = new long[64];
    private long[] mOccupancyMaskWhitePawn = new long[64];
    private long[] mOccupancyMaskBlackPawn = new long[64];

    // Number of bits set for each square in our sliding piece occupancy masks
    private long[] mRookBitsSet = new long[64];
    private long[] mBishopBitsSet = new long[64];

    private long[][] mRookBlockerBoards = new long[64][];
    private long[][] mRookMoveBoards = new long[64][];

    private long[][] mBishopBlockerBoards = new long[64][];
    private long[][] mBishopMoveBoards = new long[64][];

    private long[][] mRookMagicMoves = new long[64][];
    private long[][] mBishopMagicMoves = new long[64][];

    // Magic numbers (generated with generateMagic() function)
    private long[] mRookMagics = {0x4080011024400080L, 0xa40042000300040L, 0x2080100048806000L, 0x80100008000c82L,
            0x600020020284510L, 0x1700088a14000100L, 0x400009102040850L, 0x200002280c20401L, 0x8000801120804004L,
            0x8440100a406000L, 0x1204801000200080L, 0x26000840220030L, 0x2012001200200408L, 0xa011000400030008L,
            0x801000300020004L, 0x4082000a00409104L, 0x4180084000402000L, 0x10004004c02000L, 0x431130060010040L,
            0x7110818030000801L, 0x1081010004080011L, 0x4008002004480L, 0x4090c0005508208L, 0x9410200004d0584L,
            0x45188a380004000L, 0x20100040002040L, 0xc500430900102000L, 0x2120100100082104L, 0x4003841100180100L,
            0x201000300040008L, 0x4000011400021008L, 0x200018a00140041L, 0x240028020800140L, 0x1010402004401000L,
            0x801000802001L, 0xc0180080801000L, 0x2124240080803801L, 0x1002000400800280L, 0x22000182000804L,
            0x8804204a000089L, 0x608844010208000L, 0x390104020004000L, 0x908820030620040L, 0x19001000250008L,
            0x8008000400088080L, 0x41401020080104L, 0x80061008040001L, 0xb2208400420001L, 0x210442008200L,
            0x1410a104080a200L, 0x450014600100L, 0x8081804800100280L, 0x300402081001002L, 0x2404c0022008080L,
            0x821098350400L, 0x80800100004080L, 0xa000201100884202L, 0x10802040090011L, 0x40020210400a8022L,
            0x24050020087001L, 0x108a002420981102L, 0x41000400020881L, 0x20088101114L, 0x840241022aL};
    private long[] mBishopMagics = {0x4818100108250030L, 0x9914014805190700L, 0x228009102040108L, 0x210c041880040422L,
            0x244104440080024L, 0xa0882080c0011L, 0x8401009220202c00L, 0x9000490803900c08L, 0x200410524040L,
            0x1500020288010100L, 0x4a00100100410000L, 0xd000111042000000L, 0x8400040420104008L, 0x20001182a0200001L,
            0x4000604108084004L, 0x2810c09010844L, 0x41800528060400L, 0x8082041040191L, 0x10000245020321L,
            0x8014114110001L, 0x882100403200800L, 0x800410108800L, 0x48400094c460808L, 0xe044501050104L,
            0x404104084303004L, 0x2081090612845L, 0x800820010040031L, 0x80900400004010e0L, 0x815010000104004L,
            0x470082048200L, 0x200800200d000L, 0x201d083000440400L, 0x4890030800200820L, 0x4124100880024a41L,
            0x8024002800040040L, 0x50200800110810L, 0x85100082202a0200L, 0x1010011060804aL, 0x9044810440240400L,
            0x3400840090450482L, 0x1480884204028L, 0x1004a082010a480L, 0xc2084410081200L, 0x8010c02058000102L,
            0x400080100411400L, 0x6801811001020280L, 0x4884081000400L, 0x22100082008a2041L, 0x8081016802400080L,
            0x40104008c048800L, 0x401046b100082L, 0x1084050020880000L, 0x5002124405040802L, 0x804002240110c3L,
            0x81a20802128c0418L, 0x68810210220a040L, 0xd8a024048045000L, 0x220042084440L, 0x140200840400L,
            0x8400100011058801L, 0x9000045208e00L, 0x2113084008450309L, 0x10a81041880100L, 0x400802580d470a00L};

    private boolean mTestDirectionality = false;
    private boolean mTestRookOccupancyMasksInitialization = false;
    private boolean mTestBishopOccupancyMasksInitialization = false;
    private boolean mTestQueenOccupancyMasksInitialization = false;
    private boolean mTestWhitePawnOccupancyMasksInitialization = false;
    private boolean mTestBlackPawnOccupancyMasksInitialization = false;
    private boolean mTestKnightOccupancyMasksInitialization = false;
    private boolean mTestKingOccupancyMasksInitialization = false;

    private boolean mTestRookBlockerAndMoveBoardInitialization = false;
    private boolean mTestBishopBlockerAndMoveBoardInitialization = false;

    private boolean mTestRookMagicsGeneration = false;
    private boolean mTestBishopMagicsGeneration = true;

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

    private Random rand;

    private Test() {
        mOccupancyMaskRook = generateSlidingOccupancyMasks(mRookDirections);
        mOccupancyMaskBishop = generateSlidingOccupancyMasks(mBishopDirections);
        mOccupancyMaskQueen = generateSlidingOccupancyMasks(mQueenDirections);
        mOccupancyMaskKing = generateOccupancyMasks(mKingDirections);
        mOccupancyMaskKnight = generateOccupancyMasks(mKnightDirections);
        mOccupancyMaskWhitePawn = generateOccupancyMasks(mWhitePawnDirections);
        mOccupancyMaskBlackPawn = generateOccupancyMasks(mBlackPawnDirections);

        rand = new Random();

        // Generate the number of bits set at each position
        for (int i = 0; i < 64; i++) mRookBitsSet[i] = countBits(getBitIdxs(mOccupancyMaskRook[i]));
        for (int i = 0; i < 64; i++) mBishopBitsSet[i] = countBits(getBitIdxs(mOccupancyMaskBishop[i]));

        generateRookBlockerAndMoveBoards();
        generateBishopBlockerAndMoveBoards();

//        long rms = System.currentTimeMillis();
//        mRookMagics = generateMagics(mRookBlockerBoards, mRookMoveBoards, mRookBitsSet);
//        String rookString = "{";
//        for (long l : mRookMagics) {
//            rookString += "0x" + Long.toHexString(l) + "L, ";
//        }
//        rookString += "}";
//        out.println(rookString);
//        out.printf("Rook magic init took %dms\n", System.currentTimeMillis() - rms);

//        long bms = System.currentTimeMillis();
//        mBishopMagics = generateMagics(mBishopBlockerBoards, mBishopMoveBoards, mBishopBitsSet);
//        String bishopString = "{";
//        for (long l : mBishopMagics) {
//            bishopString += "0x" + Long.toHexString(l) + "L, ";
//        }
//        bishopString += "}";
//        out.println(bishopString);
//        out.printf("Bishop magic init took %dms\n", System.currentTimeMillis() - bms);

        generateRookMagicMoves();
        generateBishopMagicMoves();

        //<editor-fold desc="If statements to run tests">
        if (mTestDirectionality) testDirectionality();
        if (mTestRookOccupancyMasksInitialization) {
            for (int i = 0; i < mOccupancyMaskRook.length; i++) {
                printBitboard(mOccupancyMaskRook[i], "R");
                out.println(mRookBitsSet[i]);
                out.println();
            }
        }
        if (mTestBishopOccupancyMasksInitialization) {
            for (int i = 0; i < mOccupancyMaskBishop.length; i++) {
                printBitboard(mOccupancyMaskBishop[i], "B");
                out.println(mBishopBitsSet[i]);
                out.println();
            }
        }
        if (mTestQueenOccupancyMasksInitialization) {
            for (long l : mOccupancyMaskQueen) {
                printBitboard(l, "Q");
                out.println();
            }
        }
        if (mTestKnightOccupancyMasksInitialization) {
            for (long l : mOccupancyMaskKnight) {
                printBitboard(l, "N");
                out.println();
            }
        }
        if (mTestKingOccupancyMasksInitialization) {
            for (long l : mOccupancyMaskKing) {
                printBitboard(l, "K");
                out.println();
            }
        }
        if (mTestWhitePawnOccupancyMasksInitialization) {
            for (long l : mOccupancyMaskWhitePawn) {
                printBitboard(l, "W");
                out.println();
            }
        }
        if (mTestBlackPawnOccupancyMasksInitialization) {
            for (long l : mOccupancyMaskBlackPawn) {
                printBitboard(l, "L");
                out.println();
            }
        }
        if (mTestRookBlockerAndMoveBoardInitialization) {
            for (int x = 20; x < 21; x++) {
                for (int y = 0; y < mRookBlockerBoards[x].length; y++) {
                    out.println("BLOCKER BOARD: ");
                    printBitboard(mRookBlockerBoards[x][y], "R");
                    out.println("MOVE BOARD: ");
                    printBitboard(mRookMoveBoards[x][y], "M");
                    out.println();
                }
            }
        }
        if (mTestBishopBlockerAndMoveBoardInitialization) {
            for (int x = 20; x < 21; x++) {
                for (int y = 0; y < mBishopBlockerBoards[x].length; y++) {
                    out.println("BLOCKER BOARD: ");
                    printBitboard(mBishopBlockerBoards[x][y], "B");
                    out.println("MOVE BOARD: ");
                    printBitboard(mBishopMoveBoards[x][y], "M");
                    out.println();
                }
            }
        }
        if (mTestRookMagicsGeneration) {
            // subset a random sample of blocker boards to see if our magics work
            for (int i = 0; i < 20; i++) {
                int rookSq = rand.nextInt(64);
                long randomOccupancy = rand.nextLong() & rand.nextLong();
                randomOccupancy |= 1L << rookSq; // add our rook

                out.println("OCCUPANCY:");
                printBitboard(randomOccupancy, "R");

                out.printf("MOVE BOARD FOR ROOK @ SQ=%d :\n", rookSq);
                long movebb = getRookMove(randomOccupancy, rookSq);
                printBitboard(movebb, "R");

                out.println();
                out.println();
            }
        }
        if (mTestBishopMagicsGeneration) {
            // subset a random sample of blocker boards to see if our magics work
            for (int i = 0; i < 20; i++) {
                int bishopSq = rand.nextInt(64);
                long randomOccupancy = rand.nextLong() & rand.nextLong();
                randomOccupancy |= 1L << bishopSq; // add our bishop

                out.println("OCCUPANCY:");
                printBitboard(randomOccupancy, "B");

                out.printf("MOVE BOARD FOR BISHOP @ SQ=%d :\n", bishopSq);
                long movebb = getBishopMove(randomOccupancy, bishopSq);
                printBitboard(movebb, "B");

                out.println();
                out.println();
            }
        }
        //</editor-fold>
    }

    //<editor-fold desc="Sliding piece move getters">
    // Uses our precalculated magic lookup table to get a sliding piece move
    private long getRookMove(long occupancy, int square) {
        // Remove all pieces not in the rook's attack ray
        occupancy &= mRookBlockerBoards[square][mRookBlockerBoards[square].length - 1]; // this shitty code will be removed later
        int index = (int) ((occupancy * mRookMagics[square]) >>> (64 - mRookBitsSet[square]));
        return mRookMagicMoves[square][index];
    }

    private long getBishopMove(long occupancy, int square) {
        // Remove all pieces not in the bishop's attack ray
        occupancy &= mBishopBlockerBoards[square][mBishopBlockerBoards[square].length - 1]; // this shitty code will be removed later
        int index = (int) ((occupancy * mBishopMagics[square]) >>> (64 - mBishopBitsSet[square]));
        return mBishopMagicMoves[square][index];
    }
    //</editor-fold>

    //<editor-fold desc="Magic number generation">
    private long[] generateMagics(long[][] blockerBoards, long[][] moveBoards, long[] bitsSet) {
        // Brute-force method of generating magics
        // Thanks to: https://stackoverflow.com/questions/16925204/sliding-move-generation-using-magic-bitboard
        long[] ret = new long[64];
        for (int i = 0; i < 64; i++) {
            long magic = 0L;
            while (true) {
                magic = generateSparseRandomLong();
                Map<Integer, long[]> collisions = new HashMap<>(); // long[0] = blocker board, long[1] = move board
                int blockerLength = blockerBoards[i].length;
                boolean fail = false;
                for (int x = 0; x < blockerLength; x++) {
                    long blockerBoard = blockerBoards[i][x];
                    int index = (int) ((blockerBoard * magic) >>> (64 - bitsSet[i]));
                    if (collisions.containsKey(index)) {
                        long[] collision = collisions.get(index);
                        if (moveBoards[i][x] != collision[1]) {
                            fail = true;
                            break;
                        }
                    } else {
                        collisions.put(index, new long[]{blockerBoards[i][x], moveBoards[i][x]});
                    }
                }
                if (!fail) {
                    ret[i] = magic;
                    break;
                }
            }
        }
        return ret;
    }

    private void generateRookMagicMoves() {
        for (int x = 0; x < mRookBlockerBoards.length; x++) {
            mRookMagicMoves[x] = new long[mRookBlockerBoards[x].length];
            for (int y = 0; y < mRookBlockerBoards[x].length; y++) {
                int magicIndex = (int) ((mRookBlockerBoards[x][y] * mRookMagics[x]) >>> (64 - mRookBitsSet[x]));
                mRookMagicMoves[x][magicIndex] = mRookMoveBoards[x][y];
            }
        }
    }

    private void generateBishopMagicMoves() {
        for (int x = 0; x < mBishopBlockerBoards.length; x++) {
            mBishopMagicMoves[x] = new long[mBishopBlockerBoards[x].length];
            for (int y = 0; y < mBishopBlockerBoards[x].length; y++) {
                int magicIndex = (int) ((mBishopBlockerBoards[x][y] * mBishopMagics[x]) >>> (64 - mBishopBitsSet[x]));
                mBishopMagicMoves[x][magicIndex] = mBishopMoveBoards[x][y];
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Occupancy mask generation">
    private long[] generateSlidingOccupancyMasks(Direction[] directions) {
        long edgeMask = 0xff818181818181ffL;
        long[] cornerMasks = {0x7eL, 0x7e00000000000000L, 0x80808080808000L, 0x1010101010100L}; // north, south, east, west
        long[] sideMasks = {0xffL, 0xff00000000000000L, 0x8080808080808080L, 0x101010101010101L}; // same here
        long[] ret = new long[64];
        for (int i = 0; i < 64; i++) {
            boolean isEdgePiece = ((1L << i) & edgeMask) != 0;
            for (Direction d : directions) {
                if (!isEdgePiece) {
                    int nextIndex = d.nextIndex(i);
                    for (int nextDirIndex = nextIndex; nextDirIndex != -1; nextDirIndex = d.nextIndex(nextDirIndex)) {
                        ret[i] |= 1L << nextDirIndex;
                    }
                    ret[i] &= ~edgeMask; // Clear the edge bits
                } else {
                    // Find out what side(s) of the bitboard we're on
                    List<Integer> sides = new ArrayList<>();
                    for (int s = 0; s < sideMasks.length; s++) {
                        if (((1L << i) & sideMasks[s]) != 0) sides.add(s);
                    }

                    // Create our new edge mask, masking away all the side we occupy
                    long newEdgeMask = edgeMask;
                    for (int side : sides) newEdgeMask &= ~cornerMasks[side];

                    int nextIndex = d.nextIndex(i);
                    for (int nextDirIndex = nextIndex; nextDirIndex != -1; nextDirIndex = d.nextIndex(nextDirIndex)) {
                        ret[i] |= 1L << nextDirIndex;
                    }
                    ret[i] &= ~newEdgeMask; // Clear the edge bits
                }
            }
        }
        return ret;
    }

    private long[] generateOccupancyMasks(Direction[] directions) {
        long[] ret = new long[64];
        for (int i = 0; i < 64; i++) {
            for (Direction d : directions) {
                int nextIndex = d.nextIndex(i);
                if (nextIndex != -1) {
                    ret[i] |= 1L << nextIndex;
                }
            }
        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold desc="Blocker/move board generation">
    private void generateRookBlockerAndMoveBoards() {
        for (int i = 0; i < 64; i++) {
            int[] bitIdxs = getBitIdxs(mOccupancyMaskRook[i]);
            int possibleBoards = 1 << countBits(bitIdxs);

            // Initialize our blocker board array with maximum number of board permutations
            mRookBlockerBoards[i] = new long[possibleBoards];
            mRookMoveBoards[i] = new long[possibleBoards];
            for (int x = 0; x < possibleBoards; x++) {
                long blockerBoard = 0L;
                for (int y = 0; y < bitIdxs.length; y++) {
                    int curIdx = bitIdxs[y];
                    if (curIdx != -1) {
                        blockerBoard |= ((x >>> y) & 1L) << curIdx;
                    }
                }
                mRookBlockerBoards[i][x] = blockerBoard;

                // Generate our move board from our blocker board
                long moveBoard = 0L;
                for (Direction d : mRookDirections) {
                    int nI = d.nextIndex(i);
                    while (nI != -1) {
                        long shiftedIdx = 1L << nI;
                        moveBoard |= shiftedIdx;

                        // TODO: This if statement condition could probably be more efficient but who really cares
                        if ((((blockerBoard >>> nI) & 1L) == 1L) && (((shiftedIdx >>> nI) & 1L) == 1L)) {
                            moveBoard |= shiftedIdx;
                            break;
                        }

                        nI = d.nextIndex(nI);
                    }
                }
                mRookMoveBoards[i][x] = moveBoard;
            }
        }
    }

    private void generateBishopBlockerAndMoveBoards() {
        for (int i = 0; i < 64; i++) {
            int[] bitIdxs = getBitIdxs(mOccupancyMaskBishop[i]);
            int possibleBoards = 1 << countBits(bitIdxs);

            // Initialize our blocker board array with maximum number of board permutations
            mBishopBlockerBoards[i] = new long[possibleBoards];
            mBishopMoveBoards[i] = new long[possibleBoards];
            for (int x = 0; x < possibleBoards; x++) {
                long blockerBoard = 0L;
                for (int y = 0; y < bitIdxs.length; y++) {
                    int curIdx = bitIdxs[y];
                    if (curIdx != -1) {
                        blockerBoard |= ((x >>> y) & 1L) << curIdx;
                    }
                }
                mBishopBlockerBoards[i][x] = blockerBoard;

                // Generate our move board from our blocker board
                long moveBoard = 0L;
                for (Direction d : mBishopDirections) {
                    int nI = d.nextIndex(i);
                    while (nI != -1) {
                        long shiftedIdx = 1L << nI;
                        moveBoard |= shiftedIdx;


                        // TODO: This if statement condition could probably be more efficient but who really cares
                        if ((((blockerBoard >>> nI) & 1L) == 1L) && (((shiftedIdx >>> nI) & 1L) == 1L)) {
                            moveBoard |= shiftedIdx;
                            break;
                        }

                        nI = d.nextIndex(nI);
                    }
                }
                mBishopMoveBoards[i][x] = moveBoard;
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Tests">
    private void testDirectionality() {
        long testbb = 0b0000000000000000000000000000100000000000000000000000000000000000L;
        printBitboard(testbb, "R");
        out.println();

        long northbb = testbb | (1L << Direction.NORTH.nextIndex(getBitIdxs(testbb)[0]));
        long northeastbb = testbb | (1L << Direction.NORTHEAST.nextIndex(getBitIdxs(testbb)[0]));
        long eastbb = testbb | (1L << Direction.EAST.nextIndex(getBitIdxs(testbb)[0]));
        long southeastbb = testbb | (1L << Direction.SOUTHEAST.nextIndex(getBitIdxs(testbb)[0]));
        long southbb = testbb | (1L << Direction.SOUTH.nextIndex(getBitIdxs(testbb)[0]));
        long southwestbb = testbb | (1L << Direction.SOUTHWEST.nextIndex(getBitIdxs(testbb)[0]));
        long westbb = testbb | (1L << Direction.WEST.nextIndex(getBitIdxs(testbb)[0]));
        long northwestbb = testbb | (1L << Direction.NORTHWEST.nextIndex(getBitIdxs(testbb)[0]));

        printBitboard(northbb, "R");
        out.println();

        printBitboard(northeastbb, "R");
        out.println();

        printBitboard(eastbb, "R");
        out.println();

        printBitboard(southeastbb, "R");
        out.println();

        printBitboard(southbb, "R");
        out.println();

        printBitboard(southwestbb, "R");
        out.println();

        printBitboard(westbb, "R");
        out.println();

        printBitboard(northwestbb, "R");
        out.println();
    }
    //</editor-fold>

    //<editor-fold desc="Utility functions">
    private void printBitboard(long bitboard, String piece) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++, bitboard >>>= 1) out.print((bitboard & 1L) == 1L ? piece + " " : ". ");
            out.println();
        }
    }

    private int countBits(int[] bitIdxs) {
        int ret = 0;
        for (int i = 0; i < bitIdxs.length; i++) if (bitIdxs[i] != -1) ret++;
        return ret;
    }

    // TODO: Replace with a faster de-Bruijin bitscan algorithm
    private int[] getBitIdxs(long bitboard) {
        int[] ret = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int count = 0;
        for (int i = 0; bitboard != 0; i++) {
            if ((bitboard & 1L) == 1L) {
                ret[count] = i;
                count++;
            }
            bitboard >>>= 1;
        }
        return ret;
    }

    private long generateSparseRandomLong() {
        return rand.nextLong() & rand.nextLong() & rand.nextLong(); // the successive bitwise ANDs will bias the result towards one with a lot of 0's
    }
    //</editor-fold>

    public static void main(String[] args) {
        new Test();
    }
}
