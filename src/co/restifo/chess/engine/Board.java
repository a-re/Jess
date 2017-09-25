package co.restifo.chess.engine;

import java.util.ArrayList;
import java.util.List;

public class Board {
    //<editor-fold desc="Declarations">
    // Pieces, represented as a 2-member array. Index 0 is white pieces and index 1 is black
    private long[] mPawns = new long[2];
    private long[] mRooks = new long[2];
    private long[] mKnights = new long[2];
    private long[] mBishops = new long[2];
    private long[] mQueens = new long[2];
    private long[] mKings = new long[2];
    private long[] mPieces = new long[2];

    // We also have a PieceType array because the one thing bitboards aren't good at is answering the question
    // "which piece is on square x". This 64-sized array will be able to answer that question, at the cost of slightly
    // longer update time
    private PieceType[] mSquarePieces = new PieceType[64];

    // Variables for the game state
    private int mSide;
    private int mHalfMoves = 0;
    private int mFullMoves = 1;

    private final int KINGSIDE_CASTLE = 0;
    private final int QUEENSIDE_CASTLE = 1;
    // The first array within this array is white castling rights. For example, mCanCastle[0][KINGSIDE_CASTLE] is a
    // boolean expressing whether white (side 0) can castle kingside.
    private boolean[][] mCanCastle = new boolean[2][2];
    
    // Magic numbers (Generated with our own magic number function in Test.java)
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

    // Occupancy masks
    private long[] mOccupancyMaskRook = new long[64];
    private long[] mOccupancyMaskBishop = new long[64];
    private long[] mOccupancyMaskQueen = new long[64];
    private long[] mOccupancyMaskKing = new long[64];
    private long[] mOccupancyMaskKnight = new long[64];
    private long[] mOccupancyMaskWhitePawnCapture = new long[64];
    private long[] mOccupancyMaskBlackPawnCapture = new long[64];
    private long[] mOccupancyMaskWhitePawn = new long[64];
    private long[] mOccupancyMaskBlackPawn = new long[64];

    // Blocker boards
    private long[][] mRookBlockerBoards = new long[64][];
    private long[][] mBishopBlockerBoards = new long[64][];

    // Number of bits set at each square
    private int[] mRookBitsSet = new int[64];
    private int[] mBishopBitsSet = new int[64];

    // Our lookup table for rook and bishop moves
    // Using the magic bitboard technique
    private long[][] mRookMoves = new long[64][];
    private long[][] mBishopMoves = new long[64][];

    // Directions for each piece type
    private final Direction[] mRookDirections = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private final Direction[] mBishopDirections = {Direction.NORTHEAST, Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.NORTHWEST};
    private final Direction[] mWhitePawnCaptureDirections = {Direction.NORTHEAST, Direction.NORTHWEST};
    private final Direction[] mBlackPawnCaptureDirections = {Direction.SOUTHEAST, Direction.SOUTHWEST};
    private final Direction[] mWhitePawnDirections = {Direction.NORTH};
    private final Direction[] mBlackPawnDirections = {Direction.SOUTH};
    private final Direction[] mKnightDirections = {Direction.N_1, Direction.N_2, Direction.N_3, Direction.N_4,
            Direction.N_5, Direction.N_6, Direction.N_7, Direction.N_8};
    private final Direction[] mKingDirections = {Direction.NORTH, Direction.NORTHEAST, Direction.EAST, Direction.SOUTHEAST,
            Direction.SOUTH, Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST};
    private final Direction[] mQueenDirections = {Direction.NORTH, Direction.NORTHEAST, Direction.EAST, Direction.SOUTHEAST,
            Direction.SOUTH, Direction.SOUTHWEST, Direction.WEST, Direction.NORTHWEST};
    //</editor-fold>
    //<editor-fold desc="Constructors">
    public Board() {
        init();
        mPawns = new long[] {0xff000000000000L, 0xff00L};
        mRooks = new long[] {0x8100000000000000L, 0x81L};
        mKnights = new long[] {0x4200000000000000L, 0x42L};
        mBishops = new long[] {0x2400000000000000L, 0x24L};
        mQueens = new long[] {0x800000000000000L, 0x8L};
        mKings = new long[] {0x1000000000000000L, 0x10L};
        mPieces = new long[] {mPawns[0] | mRooks[0] | mKnights[0] | mBishops[0] | mQueens[0] | mKings[0],
                              mPawns[1] | mRooks[1] | mKnights[1] | mBishops[1] | mQueens[1] | mKings[1]};
        mCanCastle = new boolean[][] {{true, true}, {true, true}};
        mSide = 0; // White moves first

        // Update the piece-on-square array
        updateSquarePieces();
    }

    // Initialize a board from a given fen position
    public Board(String fen) {
        init();

        int curPos = 0;
        String[] fenFields = fen.split(" ");
        String[] ranks = fenFields[0].split("/");
        for (String rank : ranks) {
            char[] rankPieces = rank.toCharArray();
            // Find if the piece is white or black by the characters case
            for (char rankPiece : rankPieces) {
                // Is the current character a number? If so, those represent empty squares so we just increment the
                // current position by that number of squares and move on
                if (Character.isDigit(rankPiece)) {
                    curPos += Character.getNumericValue(rankPiece);
                } else {
                    // If the current char is not a digit, there's a piece at the current position.
                    char curPiece = Character.toLowerCase(rankPiece);

                    // If the current piece is lowercase, then it's a black piece, else a white piece
                    int side = curPiece == rankPiece ? 1 : 0;
                    switch (curPiece) {
                        case 'p': mPawns[side] |= 1L << curPos; break;
                        case 'n': mKnights[side] |= 1L << curPos; break;
                        case 'b': mBishops[side] |= 1L << curPos; break;
                        case 'r': mRooks[side] |= 1L << curPos; break;
                        case 'q': mQueens[side] |= 1L << curPos; break;
                        case 'k': mKings[side] |= 1L << curPos; break;
                    }

                    curPos++;
                }
            }
        }
        mPieces = new long[] {mPawns[0] | mRooks[0] | mKnights[0] | mBishops[0] | mQueens[0] | mKings[0],
                              mPawns[1] | mRooks[1] | mKnights[1] | mBishops[1] | mQueens[1] | mKings[1]};

        // Process all the other fen fields
        mSide = fenFields[1].equals("w") ? 0 : 1;

        String canCastle = fenFields[2];
        if (!canCastle.equals("-")) {
            char[] whoCanCastle = canCastle.toCharArray();
            for (char c : whoCanCastle) {
                switch (c) {
                    case 'K':
                        mCanCastle[0][KINGSIDE_CASTLE] = true; break;
                    case 'Q':
                        mCanCastle[0][QUEENSIDE_CASTLE] = true; break;
                    case 'k':
                        mCanCastle[1][KINGSIDE_CASTLE] = true; break;
                    case 'q':
                        mCanCastle[1][QUEENSIDE_CASTLE] = true; break;
                }
            }
        }

        // Update the piece-on-square array
        updateSquarePieces();
    }

    private void init() {
        mOccupancyMaskRook = generateOccupancyMask(mRookDirections, true);
        mOccupancyMaskBishop = generateOccupancyMask(mBishopDirections, true);
        mOccupancyMaskQueen = generateOccupancyMask(mQueenDirections, true);
        mOccupancyMaskKing = generateOccupancyMask(mKingDirections, false);
        mOccupancyMaskKnight = generateOccupancyMask(mKnightDirections, false);
        mOccupancyMaskWhitePawnCapture = generateOccupancyMask(mWhitePawnCaptureDirections, false);
        mOccupancyMaskBlackPawnCapture = generateOccupancyMask(mBlackPawnCaptureDirections, false);
        mOccupancyMaskWhitePawn = generateOccupancyMask(mWhitePawnDirections, false);
        mOccupancyMaskBlackPawn = generateOccupancyMask(mBlackPawnDirections, false);

        // TODO: Our bad design means we have to have a separate function for adding the initial rank pawn moves
        // (the move where the pawns can jump two squares ahead)
        mOccupancyMaskWhitePawn = addInitialRankPawnMoves(mOccupancyMaskWhitePawn, true);
        mOccupancyMaskBlackPawn = addInitialRankPawnMoves(mOccupancyMaskBlackPawn, false);

        // Generate array of number of bits at each square
        for (int i = 0; i < 64; i++) mRookBitsSet[i] = countBits(mOccupancyMaskRook[i]);
        for (int i = 0; i < 64; i++) mBishopBitsSet[i] = countBits(mOccupancyMaskBishop[i]);

        // Generate our blocker boards
        mRookBlockerBoards = generateBlockerBoards(true);
        mBishopBlockerBoards = generateBlockerBoards(false);

        // Generate our sliding piece lookup table
        mRookMoves = generateMagicMoves(true);
        mBishopMoves = generateMagicMoves(false);

        // First, initialize the pieces on square array so that all squares have EMPTY pieces
        // We will set the correct values at the last step of initialization
        for (int i = 0; i < 64; i++) mSquarePieces[i] = PieceType.EMPTY;
    }
    //</editor-fold>
    //<editor-fold desc="Getters/setters">
    public long getPawns() { return mPawns[mSide]; }

    public long getRooks() { return mRooks[mSide]; }

    public long getKnights() { return mKnights[mSide]; }

    public long getBishops() { return mBishops[mSide]; }

    public long getQueens() { return mQueens[mSide]; }

    public long getKings() { return mKings[mSide]; }

    public long getPieces() { return mPieces[mSide]; }

    public long getAllPieces() { return mPieces[0] | mPieces[1]; }

    public int getSide() { return mSide; }

    public PieceType getPieceOnSquare(int square) { return mSquarePieces[square]; }

    public void switchSide() { mSide ^= 1; }
    public void switchSide(int side) { mSide = side; }
    //</editor-fold>
    //<editor-fold desc="Occupancy mask initialization">
    private long[] generateOccupancyMask(Direction[] directions, boolean isSliding) {
        final long edgeMask = 0xff818181818181ffL;
        final long[] cornerMasks = {0x7eL, 0x7e00000000000000L, 0x80808080808000L, 0x1010101010100L}; // north, south, east, west
        final long[] sideMasks = {0xffL, 0xff00000000000000L, 0x8080808080808080L, 0x101010101010101L}; // same here
        long[] ret = new long[64];
        for (int i = 0; i < 64; i++) {
            if (isSliding) {
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
            } else {
                for (Direction d : directions) {
                    int nextIndex = d.nextIndex(i);
                    if (nextIndex != -1) {
                        ret[i] |= 1L << nextIndex;
                    }
                }
            }
        }
        return ret;
    }

    // This function modifies a pawn occupancy mask so it includes the moves of pawns being able to jump two squares
    // when they're on their initial ranks
    private long[] addInitialRankPawnMoves(long[] pawnOccupancy, boolean isWhite) {
        if (isWhite) {
            for (int i = 48; i < 56; i++) {
                int northIndex = Direction.NORTH.nextIndex(i);
                // Two nextIndex() calls represents the pawn jumping forward two squares
                pawnOccupancy[i] |= 1L << Direction.NORTH.nextIndex(northIndex);
            }
        } else {
            for (int i = 8; i < 16; i++) {
                int southIndex = Direction.SOUTH.nextIndex(i);
                pawnOccupancy[i] |= 1L << Direction.SOUTH.nextIndex(southIndex);
            }
        }
        return pawnOccupancy;
    }
    //</editor-fold>
    //<editor-fold desc="Sliding piece lookup table generation">
    private long[][] generateBlockerBoards(boolean isRook) {
        int[] bitsSet = isRook ? mRookBitsSet : mBishopBitsSet;
        long[] occupancyMask = isRook ? mOccupancyMaskRook : mOccupancyMaskBishop;

        long[][] blockerBoards = new long[64][];
        for (int i = 0; i < 64; i++) {
            List<Integer> bitIdxs = getBitIdxs(occupancyMask[i]);
            int possibleBoards = 1 << countBits(occupancyMask[i]);

            blockerBoards[i] = new long[possibleBoards];
            for (int x = 0; x < possibleBoards; x++) {
                long blockerBoard = 0L;
                for (int y = 0; y < bitIdxs.size(); y++) {
                    int curIdx = bitIdxs.get(y);
                    if (curIdx != -1) {
                        blockerBoard |= ((x >>> y) & 1L) << curIdx;
                    }
                }
                blockerBoards[i][x] = blockerBoard;
            }
        }
        return blockerBoards;
    }

    private long[][] generateMagicMoves(boolean isRook) {
        int[] bitsSet = isRook ? mRookBitsSet : mBishopBitsSet;
        long[] occupancyMask = isRook ? mOccupancyMaskRook : mOccupancyMaskBishop;
        long[] magics = isRook ? mRookMagics : mBishopMagics;
        long[][] blockerBoards = isRook ? mRookBlockerBoards : mBishopBlockerBoards;
        Direction[] directions = isRook ? mRookDirections : mBishopDirections;

        // First, generate all possible move boards given our occupancy masks
        long[][] moveBoards = new long[64][];
        for (int i = 0; i < 64; i++) {
            List<Integer> bitIdxs = getBitIdxs(occupancyMask[i]);
            int possibleBoards = 1 << countBits(occupancyMask[i]);

            moveBoards[i] = new long[possibleBoards];
            for (int x = 0; x < possibleBoards; x++) {
                // Generate our move boards
                long blockerBoard = blockerBoards[i][x];
                long moveBoard = 0L;
                for (Direction d : directions) {
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
                moveBoards[i][x] = moveBoard;
            }
        }

        // Now that we have our blocker and move boards, we can use this to generate our magic move lookup table
        long[][] magicMoves = new long[64][];
        for (int x = 0; x < blockerBoards.length; x++) {
            int numBoards = blockerBoards[x].length;
            magicMoves[x] = new long[numBoards];
            for (int y = 0; y < numBoards; y++) {
                int magicIndex = (int) ((blockerBoards[x][y] * magics[x]) >>> (64 - bitsSet[x]));
                magicMoves[x][magicIndex] = moveBoards[x][y];
            }
        }
        return magicMoves;
    }
    //</editor-fold>
    //<editor-fold desc="Move generation">
    public List<Move> getLegalMoves() {
        List<Move> moves = new ArrayList<>();
        int oppSide = mSide == 0 ? 1 : 0;
        long allPieces = mPieces[mSide] | mPieces[oppSide];

        // Step 1: First, generate all our pseudolegal moves, then check if they are legal in the next step
        // Get our rook moves
        List<Integer> rookIdxs = getBitIdxs(mRooks[mSide]);
        for (int pieceSq : rookIdxs) if (pieceSq != -1) { moves.addAll(getPseudolegalMoves(allPieces, mPieces[mSide], pieceSq, PieceType.ROOK)); }

        // Bishop moves
        List<Integer> bishopIdxs = getBitIdxs(mBishops[mSide]);
        for (int pieceSq : bishopIdxs) if (pieceSq != -1) { moves.addAll(getPseudolegalMoves(allPieces, mPieces[mSide], pieceSq, PieceType.BISHOP)); }

        // Knight moves
        List<Integer> knightIdxs = getBitIdxs(mKnights[mSide]);
        for (int pieceSq : knightIdxs) if (pieceSq != -1) { moves.addAll(getPseudolegalMoves(allPieces, mPieces[mSide], pieceSq, PieceType.KNIGHT)); }

        // Queen moves
        List<Integer> queenIdxs = getBitIdxs(mQueens[mSide]);
        for (int pieceSq : queenIdxs) if (pieceSq != -1) { moves.addAll(getPseudolegalMoves(allPieces, mPieces[mSide], pieceSq, PieceType.QUEEN)); }

        // King moves
        List<Integer> kingIdxs = getBitIdxs(mKings[mSide]);
        for (int pieceSq : kingIdxs) if (pieceSq != -1) { moves.addAll(getPseudolegalMoves(allPieces, mPieces[mSide], pieceSq, PieceType.KING)); }

        // Pawn moves
        List<Integer> pawnIdxs = getBitIdxs(mPawns[mSide]);
        PieceType pawnType = mSide == 0 ? PieceType.WHITE_PAWN : PieceType.BLACK_PAWN;
        for (int pieceSq : pawnIdxs) if (pieceSq != -1) { moves.addAll(getPseudolegalMoves(allPieces, mPieces[mSide], pieceSq, pawnType)); }

        // TODO: GENERATE CASTLING/EN PASSANT MOVES
        if (mCanCastle[0][KINGSIDE_CASTLE])  moves.add(CastleMove.WHITE_KINGSIDE);
        if (mCanCastle[0][QUEENSIDE_CASTLE]) moves.add(CastleMove.WHITE_QUEENSIDE);
        if (mCanCastle[1][KINGSIDE_CASTLE])  moves.add(CastleMove.BLACK_KINGSIDE);
        if (mCanCastle[1][QUEENSIDE_CASTLE]) moves.add(CastleMove.BLACK_QUEENSIDE);

        // Step 2: See if any of our pseudolegal moves put our king in check
        
        return moves;
    }

    public List<NormalMove> getPseudolegalMoves(long occupancyAll, long occupancyFriendly, int square, PieceType pt) {
        List<NormalMove> ret = new ArrayList<>();

        long pieceMoves = getPieceMovesBitboard(occupancyAll, square, pt) & ~occupancyFriendly;
        List<Integer> moveIdxs = getBitIdxs(pieceMoves);
        for (int toSq : moveIdxs) {
            ret.add(new NormalMove(square, toSq, pt));
        }

        return ret;
    }

    private long getPieceMovesBitboard(long occupancy, int square, PieceType pt) {
        // The row value will be used for calculating pawn moves
        int row = square / 8;
        switch (pt) {
            case ROOK:
                occupancy &= mRookBlockerBoards[square][mRookBlockerBoards[square].length - 1];
                int rookMagicIndex = (int) ((occupancy * mRookMagics[square]) >>> (64 - mRookBitsSet[square]));
                return mRookMoves[square][rookMagicIndex];
            case BISHOP:
                occupancy &= mBishopBlockerBoards[square][mBishopBlockerBoards[square].length - 1];
                int bishopMagicIndex = (int) ((occupancy * mBishopMagics[square]) >>> (64 - mBishopBitsSet[square]));
                return mBishopMoves[square][bishopMagicIndex];
            case QUEEN:
                return getPieceMovesBitboard(occupancy, square, PieceType.ROOK) | getPieceMovesBitboard(occupancy, square, PieceType.BISHOP);
            case KING:
                return mOccupancyMaskKing[square];
            case KNIGHT:
                return mOccupancyMaskKnight[square];
            case WHITE_PAWN:
                // We have two capture masks because pawns can't capture one square ahead, only the two squares to
                // its diagonal. This is why we NOT the occupancy in the first part of the following return.
                // Also, we have to handle pawns moving forward two squares on their initial rank
                return (mOccupancyMaskWhitePawn[square] & ~occupancy) | (mOccupancyMaskWhitePawnCapture[square] & occupancy);
            case BLACK_PAWN:
                return (mOccupancyMaskBlackPawn[square] & ~occupancy) | (mOccupancyMaskBlackPawnCapture[square] & occupancy);
            default:
                return 0;
        }
    }
    //</editor-fold>
    //<editor-fold desc="Move making/unmaking">
    public void makeMove(NormalMove move) {
        int oppSide = mSide == 0 ? 1 : 0;
        int toSquare = move.getToSquare();
        int fromSquare = move.getFromSquare();
        long toSquareMask = 1L << toSquare;
        long fromSquareMask = 1L << fromSquare;

        boolean isMoveCapture = (toSquareMask & mPieces[oppSide]) != 0;

        long[] pieceBB = mPawns;
        PieceType movePieceType = move.getPieceType();
        switch (movePieceType) {
            case ROOK: pieceBB = mRooks; break;
            case BISHOP: pieceBB = mBishops; break;
            case QUEEN: pieceBB = mQueens; break;
            case KING: pieceBB = mKings; break;
            case KNIGHT: pieceBB = mKnights; break;
        }
        pieceBB[mSide] &= ~fromSquareMask;
        pieceBB[mSide] |= toSquareMask;
        mPieces[mSide] &= ~fromSquareMask;
        mPieces[mSide] |= toSquareMask;

        // If the move is a capture move, we also need to clear the enemy capture piece
        if (isMoveCapture) {
            pieceBB[oppSide] &= ~toSquareMask;
            mPieces[oppSide] &= ~toSquareMask;
        }

        // Lastly, we need to update the piece-on-square array
        mSquarePieces[fromSquare] = PieceType.EMPTY;
        mSquarePieces[toSquare] = movePieceType;
    }

    public void makeMove(CastleMove move) {
        makeMove(move.getKingMove());
        makeMove(move.getRookMove());
    }

    // Update our array of what squares pieces occupy
    // TODO: This function is kind of useless because we do all this in the makeMove() function
    // TODO: Either refactor this to emphasize that this is called only once or inline it into the calling functions
    private void updateSquarePieces() {
        for (int i : getBitIdxs(mRooks[0] | mRooks[1]))     mSquarePieces[i] = PieceType.ROOK;
        for (int i : getBitIdxs(mBishops[0] | mBishops[1])) mSquarePieces[i] = PieceType.BISHOP;
        for (int i : getBitIdxs(mQueens[0] | mQueens[1]))   mSquarePieces[i] = PieceType.QUEEN;
        for (int i : getBitIdxs(mKings[0] | mKings[1]))     mSquarePieces[i] = PieceType.KING;
        for (int i : getBitIdxs(mKnights[0] | mKnights[1])) mSquarePieces[i] = PieceType.KNIGHT;
        for (int i : getBitIdxs(mPawns[0]))                 mSquarePieces[i] = PieceType.WHITE_PAWN;
        for (int i : getBitIdxs(mPawns[1]))                 mSquarePieces[i] = PieceType.BLACK_PAWN;
    }

    //</editor-fold>
    //<editor-fold desc="Utility functions">
    private void printBitboard(long bitboard, String piece) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++, bitboard >>>= 1) System.out.print((bitboard & 1L) == 1L ? piece + " " : ". ");
            System.out.println();
        }
    }
    private int countBits(long bitboard) {
        return Long.bitCount(bitboard);
    }

    public List<Integer> getBitIdxs(long bitboard) {
        List<Integer> bitIdxs = new ArrayList<>(17);
        for (int i = 0; bitboard != 0; i++) {
            if ((bitboard & 1L) == 1L) {
                bitIdxs.add(i);
            }
            bitboard >>>= 1;
        }
        return bitIdxs;
    }
    //</editor-fold>
}
