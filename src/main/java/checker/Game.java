package checker;

import static checker.Check.checkersMap;

public class Game {
    private Check check;

    public Game(int col, int row) {
        Ranges.setSize(new Coord(col, row));
        check = new Check();
    }

    public void start() {
        Check.start();
        findWhitePossible();
    }

    public Box getBox(Coord coord) {
        return check.get(coord);
    }

    private static int whatSelected = 0;

    public static void pressLeftButton(Coord coord) {
        if ((coord.x + coord.y) % 2 == 0 && (coord.x > 0) && (coord.y < 8)) {
            if (whatSelected == 0 || whatSelected == 1) {
                whiteMove(coord);
            } else {
                blackMove(coord);
            }
        }
    }

    public static int finish() {
        int whiteWin = 0;
        int blackWin = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEPOSSIBLE") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEENPOSSIBLE") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("WHITE") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEEN") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("WHITESTART") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEENSTART")) {
                    whiteWin = 1;
                }
                if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKPOSSIBLE") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEENPOSSIBLE") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("BLACK") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEEN") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("BLACKSTART") ||
                        checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEENSTART")) {
                    blackWin = 1;
                }
            }
        }
        if (whiteWin == 0) return 1;
        if (blackWin == 0) return 2;
        return 0;
    }


    private static int whInt1 = 0;
    private static int whInt2 = 0;
    private static int whInt3 = 0;
    private static int whInt4 = 0;
    private static Coord whitePrevious;
    private static int whoWhite;
    private static int whiteInt = 0;
    private static int whiteKillChecker = 0;
    private static int whiteKillQueen = 0;
    private static boolean whiteMustKill = false;
    private static int contWhite = 1;
    private static boolean whiteMustKillContinue = false;
    private static boolean whiteQueenMustKillContinue = false;

    private static void whiteMove(Coord coord) {
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEPOSSIBLE") ||
                checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENPOSSIBLE"))
            whiteMoveStart(coord);
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("POSSIBLE"))
            whiteMoveFinish(coord);
    }

    private static void deleteWhitePossible() {
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEPOSSIBLE"))
                    checkersMap.set(new Coord(x, y), Box.WHITE);
                if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEENPOSSIBLE"))
                    checkersMap.set(new Coord(x, y), Box.WHITEQUEEN);
            }
        }
    }

    private static void findWhitePossible() {
        whiteMustKill = false;
        findWhiteKill();
        if (!whiteMustKill) findWhiteMove();
    }

    private static void findWhiteKillForOneCoord(int x, int y, int xNext, int yNext) {
        if (checkersMap.get(new Coord(x, y)).toString().equals("WHITE"))
            if (checkersMap.get(new Coord(x + xNext, y + yNext)).toString().equals("BLACK") ||
                    checkersMap.get(new Coord(x + xNext, y + yNext)).toString().equals("BLACKQUEEN"))
                if (checkersMap.get(new Coord(x + 2 * xNext, y + 2 * yNext)).toString().equals("BLACKSQUARE")) {
                    checkersMap.set(new Coord(x, y), Box.WHITEPOSSIBLE);
                    whiteMustKill = true;
                    whiteKillChecker = 1;
                }
    }

    private static int sign(int x) {
        if (x > 0) return 1;
        else if (x < 0) return -1;
        return 0;
    }


    private static void findWhiteQueenKillForOneCoordOthers(int x, int y, int z1, int z2) {
        if (contWhite == 1) {
            if (checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("WHITEPOSSIBLE") ||
                    checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("WHITEQUEENPOSSIBLE") ||
                    checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("WHITE") ||
                    checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("WHITEQUEEN")) {
                contWhite = 0;
            } else {
                if (checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("BLACK") ||
                        checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("BLACKQUEEN")) {
                    if (checkersMap.get(new Coord(x + z1 + sign(z1), y + z2 + sign(z2))).toString().equals("BLACKSQUARE")) {
                        contWhite = 2;
                        checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                        whiteKillQueen = 1;
                        whiteMustKill = true;
                    } else contWhite = 0;
                }
            }

        }
    }

    private static void findWhiteQueenKillForOneCoord(int x, int y) {
        if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEEN")) {
            for (int z = 1; z < 8; z++) {
                if (x + z < 8 && y + z < 7) {
                    findWhiteQueenKillForOneCoordOthers(x, y, z, z);
                }
            }
            contWhite = 1;
            for (int z = 1; z < 8; z++) {
                if (x - z > 1 && y - z > 0) {
                    findWhiteQueenKillForOneCoordOthers(x, y, -z, -z);

                }
            }
            contWhite = 1;
            for (int z = 1; z < 8; z++) {
                if (x - z > 1 && y + z < 7) {
                    findWhiteQueenKillForOneCoordOthers(x, y, -z, z);
                }
            }
            contWhite = 1;
            for (int z = 1; z < 8; z++) {
                if (x + z < 8 && y - z > 0) {
                    findWhiteQueenKillForOneCoordOthers(x, y, z, -z);
                }
            }
            contWhite = 1;
        }
    }


    private static void findWhiteKill() {
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (x < 7 && y < 6) findWhiteKillForOneCoord(x, y, 1, 1);
                if (x > 2 && y < 6) findWhiteKillForOneCoord(x, y, -1, 1);
                if (x < 7 && y > 1) findWhiteKillForOneCoord(x, y, 1, -1);
                if (x > 2 && y > 1) findWhiteKillForOneCoord(x, y, -1, -1);
            }
        }
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                findWhiteQueenKillForOneCoord(x, y);
            }
        }
    }

    private static void findWhiteKillContinue2(Coord coord, int xNext, int yNext) {
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITESTART"))
            if (checkersMap.get(new Coord(coord.x + xNext, coord.y + yNext)).toString().equals("BLACK") ||
                    checkersMap.get(new Coord(coord.x + xNext, coord.y + yNext)).toString().equals("BLACKQUEEN"))
                if (checkersMap.get(new Coord(coord.x + 2 * xNext, coord.y + 2 * yNext)).toString().equals("BLACKSQUARE")) {
                    checkersMap.set(new Coord(coord.x + 2 * xNext, coord.y + 2 * yNext), Box.POSSIBLE);
                    whiteMustKillContinue = true;
                    whiteKillChecker = 1;
                }
    }

    private static void findWhiteKillContinue(Coord coord) {
        if (coord.x < 7 && coord.y < 6) findWhiteKillContinue2(coord, 1, 1);
        if (coord.x > 2 && coord.y < 6) findWhiteKillContinue2(coord, -1, 1);
        if (coord.x < 7 && coord.y > 1) findWhiteKillContinue2(coord, 1, -1);
        if (coord.x > 2 && coord.y > 1) findWhiteKillContinue2(coord, -1, -1);
    }


    private static void findWhiteQueenKillContinue(Coord coord) {
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENSTART")) {
            whiteInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x + z < 8 && coord.y + z < 7) {
                    if (contWhite == 1) {
                        if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITE") ||
                                checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                            contWhite = 0;
                        }
                        if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACK") ||
                                checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                            if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                contWhite = 2;

                            } else contWhite = 0;
                        }
                    }
                    if (contWhite == 2 && whiteInt == 0) {

                        if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + z + 1, coord.y + z + 1), Box.POSSIBLE);
                            whiteQueenMustKillContinue = true;
                            whiteKillQueen = 1;

                            for (int rrr = 1; rrr < 8; rrr++) {
                                if (coord.x + z + 1 + rrr < 9 && coord.y + z + 1 + rrr < 8) {
                                    if (whiteInt == 0) {
                                        if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                            whiteInt = 1;
                                        }
                                        if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE") ||
                                                checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("POSSIBLE")) {
                                            checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);

                                        }

                                    }
                                }
                            }
                            whiteMustKill = true;
                        }
                    }
                }
            }

            contWhite = 1;
            whiteInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x - z > 1 && coord.y - z > 0) {
                    if (contWhite == 1) {
                        if (whiteInt == 0) {
                            if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITEQUEEN"))
                                contWhite = 0;
                            if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    contWhite = 2;
                                } else contWhite = 0;
                            }
                        }
                        if (contWhite == 2 && whiteInt == 0) {
                            if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                checkersMap.set(new Coord(coord.x - z - 1, coord.y - z - 1), Box.POSSIBLE);
                                whiteQueenMustKillContinue = true;
                                whiteKillQueen = 1;
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (coord.x - z - 1 - rrr > 0 && coord.y - z - 1 - rrr > -1) {
                                        if (whiteInt == 0) {

                                            if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                whiteInt = 1;
                                            }
                                            if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);


                                            }


                                        }
                                    }
                                }
                                whiteMustKill = true;
                            }
                        }
                    }
                }
            }

            contWhite = 1;
            whiteInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x - z > 1 && coord.y + z < 7) {
                    if (contWhite == 1) {
                        if (whiteInt == 0) {
                            if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITEQUEEN"))
                                contWhite = 0;
                            if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    contWhite = 2;

                                } else contWhite = 0;
                            }


                            if (contWhite == 2 && whiteInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z - 1, coord.y + z + 1), Box.POSSIBLE);
                                    whiteQueenMustKillContinue = true;
                                    whiteKillQueen = 1;
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x - z - 1 - rrr > 0 && coord.y + z + 1 + rrr < 8) {
                                            if (whiteInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    whiteInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                }

                                            }
                                        }
                                    }
                                    whiteMustKill = true;
                                }
                            }
                        }
                    }

                }
            }
            contWhite = 1;
            whiteInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x + z < 8 && coord.y - z > 0) {
                    if (contWhite == 1) {
                        if (whiteInt == 0) {
                            if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITEQUEEN"))
                                contWhite = 0;
                            if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    contWhite = 2;

                                } else contWhite = 0;
                            }


                            if (contWhite == 2 && whiteInt == 0) {

                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z + 1, coord.y - z - 1), Box.POSSIBLE);
                                    whiteQueenMustKillContinue = true;
                                    whiteKillQueen = 1;
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x + z + 1 + rrr < 9 && coord.y - z - 1 - rrr > -1) {
                                            if (whiteInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    whiteInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);

                                                }

                                            }
                                        }
                                    }
                                    whiteMustKill = true;
                                }
                            }
                        }

                    }
                }
            }

            contWhite = 1;
            whiteInt = 0;

        }
    }

    private static void findWhiteMove() {
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (checkersMap.get(new Coord(x, y)).toString().equals("WHITE")) {
                    if (x < 8 && y > 0) {
                        if (checkersMap.get(new Coord(x + 1, y - 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.WHITEPOSSIBLE);
                    }
                    if (y > 0) {
                        if (checkersMap.get(new Coord(x - 1, y - 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.WHITEPOSSIBLE);
                    }
                }

                if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEEN")) {
                    if (x < 8)
                        if (checkersMap.get(new Coord(x + 1, y + 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                    if (checkersMap.get(new Coord(x - 1, y + 1)).toString().equals("BLACKSQUARE"))
                        checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                    if (x < 8 && y > 0)
                        if (checkersMap.get(new Coord(x + 1, y - 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                    if (y > 0)
                        if (checkersMap.get(new Coord(x - 1, y - 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                }
            }
        }
    }

    private static void whiteMoveStart(Coord coord) {
        if (whiteMustKill) {
            if (whatSelected == 1) {
                for (int x = 1; x < 9; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE")) {
                            checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEPOSSIBLE")) {
                    for (int x = 1; x < 9; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEENSTART")) {
                                checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                            }
                            if (checkersMap.get(new Coord(x, y)).toString().equals("WHITESTART")) {
                                checkersMap.set(new Coord(x, y), Box.WHITEPOSSIBLE);
                            }
                        }
                    }
                    checkersMap.set(new Coord(coord.x, coord.y), Box.WHITESTART);
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENPOSSIBLE")) {
                    for (int x = 1; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (checkersMap.get(new Coord(x, y)).toString().equals("WHITESTART")) {
                                checkersMap.set(new Coord(x, y), Box.WHITEPOSSIBLE);
                            }
                            if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEENSTART")) {
                                checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                            }
                        }
                    }
                    checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                }

                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITESTART")) {
                    if (coord.x > 2 && coord.y > 1)
                        if (checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("BLACK") &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y - 2), Box.POSSIBLE);
                        }
                    if (coord.x < 8 && coord.y > 1)
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("BLACK") &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y - 2), Box.POSSIBLE);

                        }
                    if (coord.x > 2 && coord.y < 7)
                        if (checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("BLACK") &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y + 2), Box.POSSIBLE);

                        }
                    if (coord.x < 8 && coord.y < 7)
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("BLACK") &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y + 2), Box.POSSIBLE);

                        }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENSTART")) {
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 8 && coord.y + z < 7) {
                            if (contWhite == 1) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                                    contWhite = 0;
                                }
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                        contWhite = 2;
                                    } else contWhite = 0;
                                }
                            }
                            if (contWhite == 2 && whiteInt == 0) {
                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z + 1, coord.y + z + 1), Box.POSSIBLE);
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x + z + 1 + rrr < 9 && coord.y + z + 1 + rrr < 8) {
                                            if (whiteInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    whiteInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                }

                                            }
                                        }
                                    }
                                    whiteMustKill = true;
                                }
                            }
                        }
                    }
                    contWhite = 1;
                    whiteInt = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 1 && coord.y - z > 0) {
                            if (contWhite == 1) {
                                if (whiteInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITE") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITEQUEEN"))
                                        contWhite = 0;
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACK") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKQUEEN")) {
                                        if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                            contWhite = 2;
                                        } else contWhite = 0;
                                    }
                                }
                                if (contWhite == 2 && whiteInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        checkersMap.set(new Coord(coord.x - z - 1, coord.y - z - 1), Box.POSSIBLE);
                                        for (int rrr = 1; rrr < 8; rrr++) {
                                            if (coord.x - z - 1 - rrr > 0 && coord.y - z - 1 - rrr > -1) {
                                                if (whiteInt == 0) {
                                                    if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        whiteInt = 1;
                                                    }
                                                    if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                    }
                                                }
                                            }
                                        }
                                        whiteMustKill = true;
                                    }
                                }
                            }
                        }
                    }
                    contWhite = 1;
                    whiteInt = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 1 && coord.y + z < 7) {
                            if (contWhite == 1) {
                                if (whiteInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITE") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITEQUEEN"))
                                        contWhite = 0;
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACK") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                                        if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                            contWhite = 2;
                                        } else contWhite = 0;
                                    }
                                    if (contWhite == 2 && whiteInt == 0) {
                                        if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                            checkersMap.set(new Coord(coord.x - z - 1, coord.y + z + 1), Box.POSSIBLE);
                                            for (int rrr = 1; rrr < 8; rrr++) {
                                                if (coord.x - z - 1 - rrr > 0 && coord.y + z + 1 + rrr < 8) {
                                                    if (whiteInt == 0) {
                                                        if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                            whiteInt = 1;
                                                        }
                                                        if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                            checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                        }

                                                    }
                                                }
                                            }
                                            whiteMustKill = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    contWhite = 1;
                    whiteInt = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 8 && coord.y - z > 0) {
                            if (contWhite == 1) {
                                if (whiteInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITE") ||
                                            checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITEQUEEN"))
                                        contWhite = 0;
                                    if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACK") ||
                                            checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKQUEEN")) {
                                        if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                            contWhite = 2;
                                        } else contWhite = 0;
                                    }
                                    if (contWhite == 2 && whiteInt == 0) {
                                        if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                            checkersMap.set(new Coord(coord.x + z + 1, coord.y - z - 1), Box.POSSIBLE);
                                            for (int rrr = 1; rrr < 8; rrr++) {
                                                if (coord.x + z + 1 + rrr < 9 && coord.y - z - 1 - rrr > -1) {
                                                    if (whiteInt == 0) {
                                                        if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                            whiteInt = 1;
                                                        }
                                                        if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                            checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                        }
                                                    }
                                                }
                                            }
                                            whiteMustKill = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    contWhite = 1;
                    whiteInt = 0;
                }
                whitePrevious = new Coord(coord.x, coord.y);
            }

            if (whatSelected == 0) {
                whatSelected = 1;
                whitePrevious = new Coord(coord.x, coord.y);
                if (whiteKillChecker == 1) {
                    if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEPOSSIBLE")) {
                        checkersMap.set(new Coord(coord.x, coord.y), Box.WHITESTART);
                    }
                }
                if (whiteKillQueen == 1) {
                    if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENPOSSIBLE")) {
                        checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITESTART")) {
                    if (coord.x > 2 && coord.y > 1)
                        if ((checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("BLACK") ||
                                (checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("BLACKQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y - 2), Box.POSSIBLE);
                        }
                    if (coord.x < 7 && coord.y > 1)
                        if ((checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("BLACK") ||
                                (checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("BLACKQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y - 2), Box.POSSIBLE);
                        }
                    if (coord.x > 2 && coord.y < 7)
                        if ((checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("BLACK") ||
                                (checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("BLACKQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y + 2), Box.POSSIBLE);
                        }
                    if (coord.x < 7 && coord.y < 7)
                        if ((checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("BLACK") ||
                                (checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("BLACKQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y + 2), Box.POSSIBLE);
                        }
                }
            }
            if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENSTART")) {
                for (int z = 1; z < 8; z++) {
                    if (coord.x + z < 8 && coord.y + z < 7) {
                        if (contWhite == 1) {
                            if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                                contWhite = 0;
                            }
                            if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    contWhite = 2;
                                } else contWhite = 0;
                            }
                        }
                        if (contWhite == 2 && whiteInt == 0) {
                            if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                checkersMap.set(new Coord(coord.x + z + 1, coord.y + z + 1), Box.POSSIBLE);
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (coord.x + z + 1 + rrr < 9 && coord.y + z + 1 + rrr < 8) {
                                        if (whiteInt == 0) {
                                            if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                whiteInt = 1;
                                            }
                                            if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                            }
                                        }
                                    }
                                }
                                whiteMustKill = true;
                            }
                        }
                    }
                }
                contWhite = 1;
                whiteInt = 0;
                for (int z = 1; z < 8; z++) {
                    if (coord.x - z > 1 && coord.y - z > 0) {
                        if (contWhite == 1) {
                            if (whiteInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITEQUEEN"))
                                    contWhite = 0;
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        contWhite = 2;
                                    } else contWhite = 0;
                                }
                            }
                            if (contWhite == 2 && whiteInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z - 1, coord.y - z - 1), Box.POSSIBLE);
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x - z - 1 - rrr > 0 && coord.y - z - 1 - rrr > -1) {
                                            if (whiteInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    whiteInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                }
                                            }
                                        }
                                    }
                                    whiteMustKill = true;
                                }
                            }
                        }
                    }
                }
                contWhite = 1;
                whiteInt = 0;
                for (int z = 1; z < 8; z++) {
                    if (coord.x - z > 1 && coord.y + z < 7) {
                        if (contWhite == 1) {
                            if (whiteInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITEQUEEN"))
                                    contWhite = 0;
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                        contWhite = 2;
                                    } else contWhite = 0;
                                }
                                if (contWhite == 2 && whiteInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                        checkersMap.set(new Coord(coord.x - z - 1, coord.y + z + 1), Box.POSSIBLE);
                                        for (int rrr = 1; rrr < 8; rrr++) {
                                            if (coord.x - z - 1 - rrr > 0 && coord.y + z + 1 + rrr < 8) {
                                                if (whiteInt == 0) {
                                                    if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                        whiteInt = 1;
                                                    }
                                                    if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                        checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                    }

                                                }
                                            }
                                        }
                                        whiteMustKill = true;
                                    }
                                }
                            }
                        }
                    }
                }
                contWhite = 1;
                whiteInt = 0;
                for (int z = 1; z < 8; z++) {
                    if (coord.x + z < 8 && coord.y - z > 0) {
                        if (contWhite == 1) {
                            if (whiteInt == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITEQUEEN"))
                                    contWhite = 0;
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        contWhite = 2;
                                    } else contWhite = 0;
                                }
                                if (contWhite == 2 && whiteInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        checkersMap.set(new Coord(coord.x + z + 1, coord.y - z - 1), Box.POSSIBLE);
                                        for (int rrr = 1; rrr < 8; rrr++) {
                                            if (coord.x + z + 1 + rrr < 9 && coord.y - z - 1 - rrr > -1) {
                                                if (whiteInt == 0) {
                                                    if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        whiteInt = 1;
                                                    }
                                                    if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                    }
                                                }
                                            }
                                        }
                                        whiteMustKill = true;
                                    }
                                }
                            }
                        }
                    }
                }
                contWhite = 1;
                whiteInt = 0;
            }
        } else {
            if (whatSelected == 1) {
                for (int y = 0; y < 8; y++) {
                    for (int x = 1; x < 9; x++) {
                        if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                            checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                        if (checkersMap.get(new Coord(x, y)).toString().equals("WHITESTART")) {
                            checkersMap.set(new Coord(x, y), Box.WHITEPOSSIBLE);
                        }
                        if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEENSTART")) {
                            checkersMap.set(new Coord(x, y), Box.WHITEQUEENPOSSIBLE);
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.WHITESTART);
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITESTART")) {
                    if (checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("BLACKSQUARE")) {
                        checkersMap.set(new Coord(coord.x - 1, coord.y - 1), Box.POSSIBLE);
                        whoWhite = 0;
                    }
                    if (coord.x != 8) {
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 1, coord.y - 1), Box.POSSIBLE);
                            whoWhite = 0;
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENSTART")) {
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y + z < 8) {
                            if (whInt1 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y + z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt1 = 1;
                            }
                        }
                    }
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y - z > -1) {
                            if (whInt2 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y - z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt2 = 1;
                            }
                        }
                    }
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y - z > -1) {
                            if (whInt3 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y - z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt3 = 1;
                            }
                        }
                    }
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y + z < 8) {
                            if (whInt4 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y + z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt4 = 1;
                            }

                        }
                    }
                }
                whitePrevious = new Coord(coord.x, coord.y);
                whInt1 = 0;
                whInt2 = 0;
                whInt3 = 0;
                whInt4 = 0;
            }
            if (whatSelected == 0) {
                whatSelected = 1;
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.WHITESTART);
                    if (checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("BLACKSQUARE")) {
                        checkersMap.set(new Coord(coord.x - 1, coord.y - 1), Box.POSSIBLE);
                        whoWhite = 0;
                    }
                    if (coord.x != 8) {
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 1, coord.y - 1), Box.POSSIBLE);
                            whoWhite = 0;
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("WHITEQUEENPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y + z < 8) {
                            if (whInt1 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y + z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt1 = 1;
                            }
                        }
                    }
                    whInt1 = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y - z > -1) {
                            if (whInt2 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y - z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt2 = 1;
                            }
                        }
                    }
                    whInt2 = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y - z > -1) {
                            if (whInt3 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y - z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt3 = 1;
                            }
                        }
                    }
                    whInt3 = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y + z < 8) {
                            if (whInt4 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y + z), Box.POSSIBLE);
                                    whoWhite = 1;
                                } else whInt4 = 1;
                            }

                        }
                    }
                    whInt4 = 0;
                }
                whitePrevious = new Coord(coord.x, coord.y);
            }
        }
    }


    private static void whiteMoveFinish(Coord coord) {
        if (whiteMustKill) {
            if (whiteKillChecker == 1) {
                if (checkersMap.get(whitePrevious).toString().equals("WHITESTART")) {
                    if (coord.x - whitePrevious.x == 2 && coord.y - whitePrevious.y == 2) {
                        checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(whitePrevious.x + 1, whitePrevious.y + 1), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(whitePrevious.x + 2, whitePrevious.y + 2), Box.WHITESTART);
                        whiteMustKillContinue = false;
                        for (int y = 0; y < 8; y++) {
                            for (int x = 1; x < 9; x++) {
                                if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                    checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                            }
                        }
                        findWhiteKillContinue(coord);
                        if (whiteMustKillContinue) {
                            whitePrevious.x = coord.x;
                            whitePrevious.y = coord.y;
                            whatSelected = 1;
                            whiteKillQueen = 0;
                        } else {
                            whatSelected = 2;
                            checkersMap.set(new Coord(whitePrevious.x + 2, whitePrevious.y + 2), Box.WHITE);
                        }
                    }
                    if (whitePrevious.x - coord.x == 2 && coord.y - whitePrevious.y == 2) {
                        checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(whitePrevious.x - 1, whitePrevious.y + 1), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(whitePrevious.x - 2, whitePrevious.y + 2), Box.WHITESTART);
                        whiteMustKillContinue = false;
                        for (int y = 0; y < 8; y++) {
                            for (int x = 1; x < 9; x++) {
                                if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                    checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                            }
                        }
                        findWhiteKillContinue(coord);
                        if (whiteMustKillContinue) {
                            whitePrevious.x = coord.x;
                            whitePrevious.y = coord.y;
                            whatSelected = 1;
                            whiteKillQueen = 0;
                        } else {
                            whatSelected = 2;
                            checkersMap.set(new Coord(whitePrevious.x - 2, whitePrevious.y + 2), Box.WHITE);
                        }
                    }
                    if (coord.x - whitePrevious.x == 2 && whitePrevious.y - coord.y == 2) {
                        checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(whitePrevious.x + 1, whitePrevious.y - 1), Box.BLACKSQUARE);
                        if (!(coord.y == 0 && checkersMap.get(new Coord(whitePrevious.x + 2, whitePrevious.y - 2)).toString().equals("POSSIBLE"))) {
                            checkersMap.set(new Coord(whitePrevious.x + 2, whitePrevious.y - 2), Box.WHITESTART);
                            whiteMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findWhiteKillContinue(coord);
                            if (whiteMustKillContinue) {
                                whitePrevious.x = coord.x;
                                whitePrevious.y = coord.y;
                                whatSelected = 1;
                                whiteKillQueen = 0;
                            } else {
                                whatSelected = 2;
                                checkersMap.set(new Coord(whitePrevious.x + 2, whitePrevious.y - 2), Box.WHITE);
                            }
                        } else {
                            checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                            whiteMustKillContinue = false;
                            whiteQueenMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findWhiteQueenKillContinue(coord);

                            if (whiteQueenMustKillContinue) {
                                whitePrevious.x = coord.x;
                                whitePrevious.y = coord.y;
                                whatSelected = 1;

                            } else {
                                checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEEN);
                            }
                            if (!whiteQueenMustKillContinue) whatSelected = 2;
                        }

                    }
                    if (whitePrevious.x - coord.x == 2 && whitePrevious.y - coord.y == 2) {
                        checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(whitePrevious.x - 1, whitePrevious.y - 1), Box.BLACKSQUARE);
                        if (!(coord.y == 0 && checkersMap.get(new Coord(whitePrevious.x - 2, whitePrevious.y - 2)).toString().equals("POSSIBLE"))) {
                            checkersMap.set(new Coord(whitePrevious.x - 2, whitePrevious.y - 2), Box.WHITESTART);
                            whiteMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findWhiteKillContinue(coord);
                            if (whiteMustKillContinue) {
                                whitePrevious.x = coord.x;
                                whitePrevious.y = coord.y;
                                whatSelected = 1;
                                whiteKillQueen = 0;
                            } else {
                                whatSelected = 2;
                                checkersMap.set(new Coord(whitePrevious.x - 2, whitePrevious.y - 2), Box.WHITE);
                            }
                        } else {
                            checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                            whiteMustKillContinue = false;
                            whiteQueenMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findWhiteQueenKillContinue(coord);
                            if (whiteQueenMustKillContinue) {
                                whitePrevious.x = coord.x;
                                whitePrevious.y = coord.y;
                                whatSelected = 1;
                            } else {
                                checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEEN);
                            }
                            if (!whiteQueenMustKillContinue) whatSelected = 2;

                        }
                    }
                }
            }
            if (whiteKillQueen == 1) {
                if (checkersMap.get(whitePrevious).toString().equals("WHITEQUEENSTART")) {
                    for (int z = 0; z < 9; z++) {
                        if (whitePrevious.x + z < 9 && whitePrevious.y + z < 8) {
                            if (checkersMap.get(new Coord(whitePrevious.x + z, whitePrevious.y + z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(whitePrevious.x + z, whitePrevious.y + z)).toString().equals("BLACKQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (whitePrevious.x + rrr < 9 && whitePrevious.y + rrr < 8) {
                                        if (checkersMap.get(new Coord(whitePrevious.x + rrr, whitePrevious.y + rrr)).toString().equals("POSSIBLE") &&
                                                whitePrevious.x + rrr == coord.x && whitePrevious.y + rrr == coord.y) {
                                            checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                                            checkersMap.set(new Coord(whitePrevious.x + z, whitePrevious.y + z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                                            whiteQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findWhiteQueenKillContinue(coord);

                                            if (whiteQueenMustKillContinue) {
                                                whitePrevious.x = coord.x;
                                                whitePrevious.y = coord.y;
                                                whatSelected = 1;

                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int z = 0; z < 9; z++) {
                        if (whitePrevious.x - z > 0 && whitePrevious.y - z > -1) {
                            if (checkersMap.get(new Coord(whitePrevious.x - z, whitePrevious.y - z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(whitePrevious.x - z, whitePrevious.y - z)).toString().equals("BLACKQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (whitePrevious.x - rrr > 0 && whitePrevious.y - rrr > -1) {
                                        if (checkersMap.get(new Coord(whitePrevious.x - rrr, whitePrevious.y - rrr)).toString().equals("POSSIBLE") &&
                                                whitePrevious.x - rrr == coord.x && whitePrevious.y - rrr == coord.y) {

                                            checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                                            checkersMap.set(new Coord(whitePrevious.x - z, whitePrevious.y - z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                                            whiteQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findWhiteQueenKillContinue(coord);
                                            if (whiteQueenMustKillContinue) {
                                                whitePrevious.x = coord.x;
                                                whitePrevious.y = coord.y;
                                                whatSelected = 1;


                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int z = 0; z < 9; z++) {
                        if (whitePrevious.x - z > 0 && whitePrevious.y + z < 8) {
                            if (checkersMap.get(new Coord(whitePrevious.x - z, whitePrevious.y + z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(whitePrevious.x - z, whitePrevious.y + z)).toString().equals("BLACKQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (whitePrevious.x - rrr > 0 && whitePrevious.y + rrr < 8) {
                                        if (checkersMap.get(new Coord(whitePrevious.x - rrr, whitePrevious.y + rrr)).toString().equals("POSSIBLE") &&
                                                whitePrevious.x - rrr == coord.x && whitePrevious.y + rrr == coord.y) {
                                            checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                                            checkersMap.set(new Coord(whitePrevious.x - z, whitePrevious.y + z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                                            whiteQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findWhiteQueenKillContinue(coord);
                                            if (whiteQueenMustKillContinue) {
                                                whitePrevious.x = coord.x;
                                                whitePrevious.y = coord.y;
                                                whatSelected = 1;

                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int z = 0; z < 9; z++) {
                        if (whitePrevious.x + z < 9 && whitePrevious.y - z > -1) {
                            if (checkersMap.get(new Coord(whitePrevious.x + z, whitePrevious.y - z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(whitePrevious.x + z, whitePrevious.y - z)).toString().equals("BLACKQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (whitePrevious.x + rrr < 9 && whitePrevious.y - rrr > -1) {
                                        if (checkersMap.get(new Coord(whitePrevious.x + rrr, whitePrevious.y - rrr)).toString().equals("POSSIBLE") &&
                                                whitePrevious.x + rrr == coord.x && whitePrevious.y - rrr == coord.y) {
                                            checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEENSTART);
                                            checkersMap.set(new Coord(whitePrevious.x + z, whitePrevious.y - z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(whitePrevious.x, whitePrevious.y), Box.BLACKSQUARE);
                                            whiteQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findWhiteQueenKillContinue(coord);
                                            if (whiteQueenMustKillContinue) {
                                                whitePrevious.x = coord.x;
                                                whitePrevious.y = coord.y;
                                                whatSelected = 1;

                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!whiteQueenMustKillContinue) whatSelected = 2;
            }
        } else {
            if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("POSSIBLE")) {
                if (whoWhite == 0) checkersMap.set(new Coord(coord.x, coord.y), Box.WHITE);
                if (whoWhite == 1) checkersMap.set(new Coord(coord.x, coord.y), Box.WHITEQUEEN);
                checkersMap.set(whitePrevious, Box.BLACKSQUARE);
                whatSelected = 2;
            }

        }

        deleteWhitePossible();

        for (int x = 1; x < 9; x++) {
            if (checkersMap.get(new Coord(x, 0)).toString().equals("WHITE")) {
                checkersMap.set(new Coord(x, 0), Box.WHITEQUEEN);
            }
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (checkersMap.get(new Coord(x, y)).toString().equals("WHITESTART")) {
                    whiteKillQueen = 0;
                }
                if (checkersMap.get(new Coord(x, y)).toString().equals("WHITEQUEENSTART")) {
                    whiteKillChecker = 0;
                }
            }
        }

        if (!whiteMustKillContinue && !whiteQueenMustKillContinue) {
            for (int y = 0; y < 8; y++) {
                for (int x = 1; x < 9; x++) {
                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                }
            }

            whiteKillQueen = 0;
            whiteKillChecker = 0;
            findBlackPossible();
        }
    }


    private static int blInt1 = 0;
    private static int blInt2 = 0;
    private static int blInt3 = 0;
    private static int blInt4 = 0;
    private static Coord blackPrevious;
    private static int whoBlack;
    private static int blackInt = 0;
    private static int blackKillChecker = 0;
    private static int blackKillQueen = 0;
    private static boolean blackMustKill = false;
    private static int contBlack = 1;
    private static boolean blackMustKillContinue = false;
    private static boolean blackQueenMustKillContinue = false;

    private static void blackMove(Coord coord) {
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKPOSSIBLE") ||
                checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENPOSSIBLE"))
            blackMoveStart(coord);
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("POSSIBLE"))
            blackMoveFinish(coord);
    }

    private static void deleteBlackPossible() {
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKPOSSIBLE"))
                    checkersMap.set(new Coord(x, y), Box.BLACK);
                if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEENPOSSIBLE"))
                    checkersMap.set(new Coord(x, y), Box.BLACKQUEEN);
            }
        }
    }

    private static void findBlackPossible() {
        blackMustKill = false;
        findBlackKill();
        if (!blackMustKill) findBlackMove();
    }

    private static void findBlackKillForOneCoord(int x, int y, int xNext, int yNext) {
        if (checkersMap.get(new Coord(x, y)).toString().equals("BLACK"))
            if (checkersMap.get(new Coord(x + xNext, y + yNext)).toString().equals("WHITE") ||
                    checkersMap.get(new Coord(x + xNext, y + yNext)).toString().equals("WHITEQUEEN"))
                if (checkersMap.get(new Coord(x + 2 * xNext, y + 2 * yNext)).toString().equals("BLACKSQUARE")) {
                    checkersMap.set(new Coord(x, y), Box.BLACKPOSSIBLE);
                    blackMustKill = true;
                    blackKillChecker = 1;
                }
    }

    private static void findBlackQueenKillForOneCoordOthers(int x, int y, int z1, int z2) {
        if (contBlack == 1) {
            if (checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("BLACKPOSSIBLE") ||
                    checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("BLACKQUEENPOSSIBLE") ||
                    checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("BLACK") ||
                    checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("BLACKQUEEN")) {
                contBlack = 0;
            } else {
                if (checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("WHITE") ||
                        checkersMap.get(new Coord(x + z1, y + z2)).toString().equals("WHITEQUEEN")) {
                    if (checkersMap.get(new Coord(x + z1 + sign(z1), y + z2 + sign(z2))).toString().equals("BLACKSQUARE")) {
                        contBlack = 2;
                        checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                        blackKillQueen = 1;
                        blackMustKill = true;
                    } else contBlack = 0;
                }
            }

        }
    }

    private static void findBlackQueenKillForOneCoord(int x, int y) {
        if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEEN")) {
            for (int z = 1; z < 8; z++) {
                if (x + z < 8 && y + z < 7) {
                    findBlackQueenKillForOneCoordOthers(x, y, z, z);
                }
            }
            contBlack = 1;
            for (int z = 1; z < 8; z++) {
                if (x - z > 1 && y - z > 0) {
                    findBlackQueenKillForOneCoordOthers(x, y, -z, -z);

                }
            }
            contBlack = 1;
            for (int z = 1; z < 8; z++) {
                if (x - z > 1 && y + z < 7) {
                    findBlackQueenKillForOneCoordOthers(x, y, -z, z);
                }
            }
            contBlack = 1;
            for (int z = 1; z < 8; z++) {
                if (x + z < 8 && y - z > 0) {
                    findBlackQueenKillForOneCoordOthers(x, y, z, -z);
                }
            }
            contBlack = 1;
        }
    }


    private static void findBlackKill() {
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (x < 7 && y < 6) findBlackKillForOneCoord(x, y, 1, 1);
                if (x > 2 && y < 6) findBlackKillForOneCoord(x, y, -1, 1);
                if (x < 7 && y > 1) findBlackKillForOneCoord(x, y, 1, -1);
                if (x > 2 && y > 1) findBlackKillForOneCoord(x, y, -1, -1);
            }
        }
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                findBlackQueenKillForOneCoord(x, y);
            }
        }
    }

    private static void findBlackKillContinue2(Coord coord, int xNext, int yNext) {
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKSTART"))
            if (checkersMap.get(new Coord(coord.x + xNext, coord.y + yNext)).toString().equals("WHITE") ||
                    checkersMap.get(new Coord(coord.x + xNext, coord.y + yNext)).toString().equals("WHITEQUEEN"))
                if (checkersMap.get(new Coord(coord.x + 2 * xNext, coord.y + 2 * yNext)).toString().equals("BLACKSQUARE")) {
                    checkersMap.set(new Coord(coord.x + 2 * xNext, coord.y + 2 * yNext), Box.POSSIBLE);
                    blackMustKillContinue = true;
                    blackKillChecker = 1;
                }
    }

    private static void findBlackKillContinue(Coord coord) {
        if (coord.x < 7 && coord.y < 6) findBlackKillContinue2(coord, 1, 1);
        if (coord.x > 2 && coord.y < 6) findBlackKillContinue2(coord, -1, 1);
        if (coord.x < 7 && coord.y > 1) findBlackKillContinue2(coord, 1, -1);
        if (coord.x > 2 && coord.y > 1) findBlackKillContinue2(coord, -1, -1);
    }


    private static void findBlackQueenKillContinue(Coord coord) {
        if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENSTART")) {
            blackInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x + z < 8 && coord.y + z < 7) {
                    if (contBlack == 1) {
                        if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACK") ||
                                checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                            contBlack = 0;
                        }
                        if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITE") ||
                                checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                            if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                contBlack = 2;

                            } else contBlack = 0;
                        }
                    }
                    if (contBlack == 2 && blackInt == 0) {

                        if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + z + 1, coord.y + z + 1), Box.POSSIBLE);
                            blackQueenMustKillContinue = true;
                            blackKillQueen = 1;

                            for (int rrr = 1; rrr < 8; rrr++) {
                                if (coord.x + z + 1 + rrr < 9 && coord.y + z + 1 + rrr < 8) {
                                    if (blackInt == 0) {
                                        if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                            blackInt = 1;
                                        }
                                        if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE") ||
                                                checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("POSSIBLE")) {
                                            checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);

                                        }

                                    }
                                }
                            }
                            blackMustKill = true;
                        }
                    }
                }
            }

            contBlack = 1;
            blackInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x - z > 1 && coord.y - z > 0) {
                    if (contBlack == 1) {
                        if (blackInt == 0) {
                            if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKQUEEN"))
                                contBlack = 0;
                            if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITEQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    contBlack = 2;
                                } else contBlack = 0;
                            }
                        }
                        if (contBlack == 2 && blackInt == 0) {
                            if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                checkersMap.set(new Coord(coord.x - z - 1, coord.y - z - 1), Box.POSSIBLE);
                                blackQueenMustKillContinue = true;
                                blackKillQueen = 1;
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (coord.x - z - 1 - rrr > 0 && coord.y - z - 1 - rrr > -1) {
                                        if (blackInt == 0) {

                                            if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                blackInt = 1;
                                            }
                                            if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);


                                            }


                                        }
                                    }
                                }
                                blackMustKill = true;
                            }
                        }
                    }
                }
            }

            contBlack = 1;
            blackInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x - z > 1 && coord.y + z < 7) {
                    if (contBlack == 1) {
                        if (blackInt == 0) {
                            if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKQUEEN"))
                                contBlack = 0;
                            if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    contBlack = 2;

                                } else contBlack = 0;
                            }


                            if (contBlack == 2 && blackInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z - 1, coord.y + z + 1), Box.POSSIBLE);
                                    blackQueenMustKillContinue = true;
                                    blackKillQueen = 1;
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x - z - 1 - rrr > 0 && coord.y + z + 1 + rrr < 8) {
                                            if (blackInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    blackInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                }

                                            }
                                        }
                                    }
                                    blackMustKill = true;
                                }
                            }
                        }
                    }

                }
            }
            contBlack = 1;
            blackInt = 0;

            for (int z = 1; z < 8; z++) {
                if (coord.x + z < 8 && coord.y - z > 0) {
                    if (contBlack == 1) {
                        if (blackInt == 0) {
                            if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKQUEEN"))
                                contBlack = 0;
                            if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITEQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    contBlack = 2;

                                } else contBlack = 0;
                            }


                            if (contBlack == 2 && blackInt == 0) {

                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z + 1, coord.y - z - 1), Box.POSSIBLE);
                                    blackQueenMustKillContinue = true;
                                    blackKillQueen = 1;
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x + z + 1 + rrr < 9 && coord.y - z - 1 - rrr > -1) {
                                            if (blackInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    blackInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);

                                                }

                                            }
                                        }
                                    }
                                    blackMustKill = true;
                                }
                            }
                        }

                    }
                }
            }

            contBlack = 1;
            blackInt = 0;

        }
    }

    private static void findBlackMove() {
        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (checkersMap.get(new Coord(x, y)).toString().equals("BLACK")) {
                    if (x < 8 ) {
                        if (checkersMap.get(new Coord(x + 1, y + 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.BLACKPOSSIBLE);
                    }
                    if (checkersMap.get(new Coord(x - 1, y + 1)).toString().equals("BLACKSQUARE"))
                        checkersMap.set(new Coord(x, y), Box.BLACKPOSSIBLE);
                }

                if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEEN")) {
                    if (x < 8)
                        if (checkersMap.get(new Coord(x + 1, y + 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                    if (checkersMap.get(new Coord(x - 1, y + 1)).toString().equals("BLACKSQUARE"))
                        checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                    if (x < 8 && y > 0)
                        if (checkersMap.get(new Coord(x + 1, y - 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                    if (y > 0)
                        if (checkersMap.get(new Coord(x - 1, y - 1)).toString().equals("BLACKSQUARE"))
                            checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                }
            }
        }
    }

    private static void blackMoveStart(Coord coord) {
        if (blackMustKill) {
            if (whatSelected == 3) {
                for (int x = 1; x < 9; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE")) {
                            checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKPOSSIBLE")) {
                    for (int x = 1; x < 9; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEENSTART")) {
                                checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                            }
                            if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKSTART")) {
                                checkersMap.set(new Coord(x, y), Box.BLACKPOSSIBLE);
                            }
                        }
                    }
                    checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKSTART);
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENPOSSIBLE")) {
                    for (int x = 1; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKSTART")) {
                                checkersMap.set(new Coord(x, y), Box.BLACKPOSSIBLE);
                            }
                            if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEENSTART")) {
                                checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                            }
                        }
                    }
                    checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                }

                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKSTART")) {
                    if (coord.x > 2 && coord.y > 1)
                        if (checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("WHITE") &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y - 2), Box.POSSIBLE);
                        }
                    if (coord.x < 8 && coord.y > 1)
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("WHITE") &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y - 2), Box.POSSIBLE);

                        }
                    if (coord.x > 2 && coord.y < 7)
                        if (checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("WHITE") &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y + 2), Box.POSSIBLE);

                        }
                    if (coord.x < 8 && coord.y < 7)
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("WHITE") &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y + 2), Box.POSSIBLE);

                        }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENSTART")) {
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 8 && coord.y + z < 7) {
                            if (contBlack == 1) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                                    contBlack = 0;
                                }
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                        contBlack = 2;
                                    } else contBlack = 0;
                                }
                            }
                            if (contBlack == 2 && blackInt == 0) {
                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z + 1, coord.y + z + 1), Box.POSSIBLE);
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x + z + 1 + rrr < 9 && coord.y + z + 1 + rrr < 8) {
                                            if (blackInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    blackInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                }

                                            }
                                        }
                                    }
                                    blackMustKill = true;
                                }
                            }
                        }
                    }
                    contBlack = 1;
                    blackInt = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 1 && coord.y - z > 0) {
                            if (contBlack == 1) {
                                if (blackInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACK") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKQUEEN"))
                                        contBlack = 0;
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITE") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITEQUEEN")) {
                                        if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                            contBlack = 2;
                                        } else contBlack = 0;
                                    }
                                }
                                if (contBlack == 2 && blackInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        checkersMap.set(new Coord(coord.x - z - 1, coord.y - z - 1), Box.POSSIBLE);
                                        for (int rrr = 1; rrr < 8; rrr++) {
                                            if (coord.x - z - 1 - rrr > 0 && coord.y - z - 1 - rrr > -1) {
                                                if (blackInt == 0) {
                                                    if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        blackInt = 1;
                                                    }
                                                    if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                    }
                                                }
                                            }
                                        }
                                        blackMustKill = true;
                                    }
                                }
                            }
                        }
                    }
                    contBlack = 1;
                    blackInt = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 1 && coord.y + z < 7) {
                            if (contBlack == 1) {
                                if (blackInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACK") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKQUEEN"))
                                        contBlack = 0;
                                    if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITE") ||
                                            checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                                        if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                            contBlack = 2;
                                        } else contBlack = 0;
                                    }
                                    if (contBlack == 2 && blackInt == 0) {
                                        if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                            checkersMap.set(new Coord(coord.x - z - 1, coord.y + z + 1), Box.POSSIBLE);
                                            for (int rrr = 1; rrr < 8; rrr++) {
                                                if (coord.x - z - 1 - rrr > 0 && coord.y + z + 1 + rrr < 8) {
                                                    if (blackInt == 0) {
                                                        if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                            blackInt = 1;
                                                        }
                                                        if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                            checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                        }

                                                    }
                                                }
                                            }
                                            blackMustKill = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    contBlack = 1;
                    blackInt = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 8 && coord.y - z > 0) {
                            if (contBlack == 1) {
                                if (blackInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACK") ||
                                            checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKQUEEN"))
                                        contBlack = 0;
                                    if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITE") ||
                                            checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITEQUEEN")) {
                                        if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                            contBlack = 2;
                                        } else contBlack = 0;
                                    }
                                    if (contBlack == 2 && blackInt == 0) {
                                        if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                            checkersMap.set(new Coord(coord.x + z + 1, coord.y - z - 1), Box.POSSIBLE);
                                            for (int rrr = 1; rrr < 8; rrr++) {
                                                if (coord.x + z + 1 + rrr < 9 && coord.y - z - 1 - rrr > -1) {
                                                    if (blackInt == 0) {
                                                        if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                            blackInt = 1;
                                                        }
                                                        if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                            checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                        }
                                                    }
                                                }
                                            }
                                            blackMustKill = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    contBlack = 1;
                    blackInt = 0;
                }
                blackPrevious = new Coord(coord.x, coord.y);
            }

            if (whatSelected == 2) {
                whatSelected = 3;
                blackPrevious = new Coord(coord.x, coord.y);
                if (blackKillChecker == 1) {
                    if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKPOSSIBLE")) {
                        checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKSTART);
                    }
                }
                if (blackKillQueen == 1) {
                    if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENPOSSIBLE")) {
                        checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKSTART")) {
                    if (coord.x > 2 && coord.y > 1)
                        if ((checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("WHITE") ||
                                (checkersMap.get(new Coord(coord.x - 1, coord.y - 1)).toString().equals("WHITEQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y - 2), Box.POSSIBLE);
                        }
                    if (coord.x < 7 && coord.y > 1)
                        if ((checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("WHITE") ||
                                (checkersMap.get(new Coord(coord.x + 1, coord.y - 1)).toString().equals("WHITEQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y - 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y - 2), Box.POSSIBLE);
                        }
                    if (coord.x > 2 && coord.y < 7)
                        if ((checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("WHITE") ||
                                (checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("WHITEQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x - 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x - 2, coord.y + 2), Box.POSSIBLE);
                        }
                    if (coord.x < 7 && coord.y < 7)
                        if ((checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("WHITE") ||
                                (checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("WHITEQUEEN"))) &&
                                checkersMap.get(new Coord(coord.x + 2, coord.y + 2)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 2, coord.y + 2), Box.POSSIBLE);
                        }
                }
            }
            if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENSTART")) {
                for (int z = 1; z < 8; z++) {
                    if (coord.x + z < 8 && coord.y + z < 7) {
                        if (contBlack == 1) {
                            if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACK") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKQUEEN")) {
                                contBlack = 0;
                            }
                            if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                                if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                    contBlack = 2;
                                } else contBlack = 0;
                            }
                        }
                        if (contBlack == 2 && blackInt == 0) {
                            if (checkersMap.get(new Coord(coord.x + z + 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                checkersMap.set(new Coord(coord.x + z + 1, coord.y + z + 1), Box.POSSIBLE);
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (coord.x + z + 1 + rrr < 9 && coord.y + z + 1 + rrr < 8) {
                                        if (blackInt == 0) {
                                            if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                blackInt = 1;
                                            }
                                            if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                            }
                                        }
                                    }
                                }
                                blackMustKill = true;
                            }
                        }
                    }
                }
                contBlack = 1;
                blackInt = 0;
                for (int z = 1; z < 8; z++) {
                    if (coord.x - z > 1 && coord.y - z > 0) {
                        if (contBlack == 1) {
                            if (blackInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKQUEEN"))
                                    contBlack = 0;
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("WHITEQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        contBlack = 2;
                                    } else contBlack = 0;
                                }
                            }
                            if (contBlack == 2 && blackInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z - 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z - 1, coord.y - z - 1), Box.POSSIBLE);
                                    for (int rrr = 1; rrr < 8; rrr++) {
                                        if (coord.x - z - 1 - rrr > 0 && coord.y - z - 1 - rrr > -1) {
                                            if (blackInt == 0) {
                                                if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    blackInt = 1;
                                                }
                                                if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                    checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                }
                                            }
                                        }
                                    }
                                    blackMustKill = true;
                                }
                            }
                        }
                    }
                }
                contBlack = 1;
                blackInt = 0;
                for (int z = 1; z < 8; z++) {
                    if (coord.x - z > 1 && coord.y + z < 7) {
                        if (contBlack == 1) {
                            if (blackInt == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKQUEEN"))
                                    contBlack = 0;
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("WHITEQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                        contBlack = 2;
                                    } else contBlack = 0;
                                }
                                if (contBlack == 2 && blackInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x - z - 1, coord.y + z + 1)).toString().equals("BLACKSQUARE")) {
                                        checkersMap.set(new Coord(coord.x - z - 1, coord.y + z + 1), Box.POSSIBLE);
                                        for (int rrr = 1; rrr < 8; rrr++) {
                                            if (coord.x - z - 1 - rrr > 0 && coord.y + z + 1 + rrr < 8) {
                                                if (blackInt == 0) {
                                                    if (!checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                        blackInt = 1;
                                                    }
                                                    if (checkersMap.get(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr)).toString().equals("BLACKSQUARE")) {
                                                        checkersMap.set(new Coord(coord.x - z - 1 - rrr, coord.y + z + 1 + rrr), Box.POSSIBLE);
                                                    }

                                                }
                                            }
                                        }
                                        blackMustKill = true;
                                    }
                                }
                            }
                        }
                    }
                }
                contBlack = 1;
                blackInt = 0;
                for (int z = 1; z < 8; z++) {
                    if (coord.x + z < 8 && coord.y - z > 0) {
                        if (contBlack == 1) {
                            if (blackInt == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACK") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKQUEEN"))
                                    contBlack = 0;
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITE") ||
                                        checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("WHITEQUEEN")) {
                                    if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        contBlack = 2;
                                    } else contBlack = 0;
                                }
                                if (contBlack == 2 && blackInt == 0) {
                                    if (checkersMap.get(new Coord(coord.x + z + 1, coord.y - z - 1)).toString().equals("BLACKSQUARE")) {
                                        checkersMap.set(new Coord(coord.x + z + 1, coord.y - z - 1), Box.POSSIBLE);
                                        for (int rrr = 1; rrr < 8; rrr++) {
                                            if (coord.x + z + 1 + rrr < 9 && coord.y - z - 1 - rrr > -1) {
                                                if (blackInt == 0) {
                                                    if (!checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        blackInt = 1;
                                                    }
                                                    if (checkersMap.get(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr)).toString().equals("BLACKSQUARE")) {
                                                        checkersMap.set(new Coord(coord.x + z + 1 + rrr, coord.y - z - 1 - rrr), Box.POSSIBLE);
                                                    }
                                                }
                                            }
                                        }
                                        blackMustKill = true;
                                    }
                                }
                            }
                        }
                    }
                }
                contBlack = 1;
                blackInt = 0;
            }
        } else {
            if (whatSelected == 3) {
                for (int y = 0; y < 8; y++) {
                    for (int x = 1; x < 9; x++) {
                        if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                            checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                        if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKSTART")) {
                            checkersMap.set(new Coord(x, y), Box.BLACKPOSSIBLE);
                        }
                        if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEENSTART")) {
                            checkersMap.set(new Coord(x, y), Box.BLACKQUEENPOSSIBLE);
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKSTART);
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKSTART")) {
                    if (checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("BLACKSQUARE")) {
                        checkersMap.set(new Coord(coord.x - 1, coord.y + 1), Box.POSSIBLE);
                        whoBlack = 0;
                    }
                    if (coord.x != 8) {
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 1, coord.y + 1), Box.POSSIBLE);
                            whoBlack = 0;
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENSTART")) {
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y + z < 8) {
                            if (blInt1 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y + z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt1 = 1;
                            }
                        }
                    }
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y - z > -1) {
                            if (blInt2 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y - z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt2 = 1;
                            }
                        }
                    }
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y - z > -1) {
                            if (blInt3 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y - z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt3 = 1;
                            }
                        }
                    }
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y + z < 8) {
                            if (blInt4 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y + z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt4 = 1;
                            }

                        }
                    }
                }
                blackPrevious = new Coord(coord.x, coord.y);
                blInt1 = 0;
                blInt2 = 0;
                blInt3 = 0;
                blInt4 = 0;
            }
            if (whatSelected == 2) {
                whatSelected = 3;
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKSTART);
                    if (checkersMap.get(new Coord(coord.x - 1, coord.y + 1)).toString().equals("BLACKSQUARE")) {
                        checkersMap.set(new Coord(coord.x - 1, coord.y + 1), Box.POSSIBLE);
                        whoBlack = 0;
                    }
                    if (coord.x != 8) {
                        if (checkersMap.get(new Coord(coord.x + 1, coord.y + 1)).toString().equals("BLACKSQUARE")) {
                            checkersMap.set(new Coord(coord.x + 1, coord.y + 1), Box.POSSIBLE);
                            whoBlack = 0;
                        }
                    }
                }
                if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("BLACKQUEENPOSSIBLE")) {
                    checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y + z < 8) {
                            if (blInt1 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y + z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt1 = 1;
                            }
                        }
                    }
                    blInt1 = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y - z > -1) {
                            if (blInt2 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y - z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt2 = 1;
                            }
                        }
                    }
                    blInt2 = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x + z < 9 && coord.y - z > -1) {
                            if (blInt3 == 0) {
                                if (checkersMap.get(new Coord(coord.x + z, coord.y - z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x + z, coord.y - z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt3 = 1;
                            }
                        }
                    }
                    blInt3 = 0;
                    for (int z = 1; z < 8; z++) {
                        if (coord.x - z > 0 && coord.y + z < 8) {
                            if (blInt4 == 0) {
                                if (checkersMap.get(new Coord(coord.x - z, coord.y + z)).toString().equals("BLACKSQUARE")) {
                                    checkersMap.set(new Coord(coord.x - z, coord.y + z), Box.POSSIBLE);
                                    whoBlack = 1;
                                } else blInt4 = 1;
                            }

                        }
                    }
                    blInt4 = 0;
                }
                blackPrevious = new Coord(coord.x, coord.y);
            }
        }
    }


    private static void blackMoveFinish(Coord coord) {
        if (blackMustKill) {
            if (blackKillChecker == 1) {
                if (checkersMap.get(blackPrevious).toString().equals("BLACKSTART")) {
                    if (coord.x - blackPrevious.x == 2 && coord.y - blackPrevious.y == 2) {
                        checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(blackPrevious.x + 1, blackPrevious.y + 1), Box.BLACKSQUARE);
                        if (!(coord.y == 7 && checkersMap.get(new Coord(blackPrevious.x + 2, blackPrevious.y + 2)).toString().equals("POSSIBLE"))) {
                            checkersMap.set(new Coord(blackPrevious.x + 2, blackPrevious.y + 2), Box.BLACKSTART);
                            blackMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackKillContinue(coord);
                            if (blackMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;
                                blackKillQueen = 0;
                            } else {
                                whatSelected = 0;
                                checkersMap.set(new Coord(blackPrevious.x + 2, blackPrevious.y + 2), Box.BLACK);
                            }
                        }
                        else {
                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                            blackMustKillContinue = false;
                            blackQueenMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackQueenKillContinue(coord);

                            if (blackQueenMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;

                            } else {
                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                            }
                            if (!blackQueenMustKillContinue) whatSelected = 0;
                        }

                    }
                    if (blackPrevious.x - coord.x == 2 && coord.y - blackPrevious.y == 2) {
                        checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(blackPrevious.x - 1, blackPrevious.y + 1), Box.BLACKSQUARE);
                        if (!(coord.y == 7 && checkersMap.get(new Coord(blackPrevious.x - 2, blackPrevious.y + 2)).toString().equals("POSSIBLE"))) {
                            checkersMap.set(new Coord(blackPrevious.x - 2, blackPrevious.y + 2), Box.BLACKSTART);
                            blackMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackKillContinue(coord);
                            if (blackMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;
                                blackKillQueen = 0;
                            } else {
                                whatSelected = 0;
                                checkersMap.set(new Coord(blackPrevious.x - 2, blackPrevious.y + 2), Box.BLACK);
                            }
                        }
                        else {
                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                            blackMustKillContinue = false;
                            blackQueenMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackQueenKillContinue(coord);

                            if (blackQueenMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;

                            } else {
                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                            }
                            if (!blackQueenMustKillContinue) whatSelected = 0;
                        }
                    }
                    if (coord.x - blackPrevious.x == 2 && blackPrevious.y - coord.y == 2) {
                        checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(blackPrevious.x + 1, blackPrevious.y - 1), Box.BLACKSQUARE);
                        if (!(coord.y == 0 && checkersMap.get(new Coord(blackPrevious.x + 2, blackPrevious.y - 2)).toString().equals("POSSIBLE"))) {
                            checkersMap.set(new Coord(blackPrevious.x + 2, blackPrevious.y - 2), Box.BLACKSTART);
                            blackMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackKillContinue(coord);
                            if (blackMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;
                                blackKillQueen = 0;
                            } else {
                                whatSelected = 0;
                                checkersMap.set(new Coord(blackPrevious.x + 2, blackPrevious.y - 2), Box.BLACK);
                            }
                        } else {
                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                            blackMustKillContinue = false;
                            blackQueenMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackQueenKillContinue(coord);

                            if (blackQueenMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;

                            } else {
                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                            }
                            if (!blackQueenMustKillContinue) whatSelected = 0;
                        }

                    }
                    if (blackPrevious.x - coord.x == 2 && blackPrevious.y - coord.y == 2) {
                        checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                        checkersMap.set(new Coord(blackPrevious.x - 1, blackPrevious.y - 1), Box.BLACKSQUARE);
                        if (!(coord.y == 0 && checkersMap.get(new Coord(blackPrevious.x - 2, blackPrevious.y - 2)).toString().equals("POSSIBLE"))) {
                            checkersMap.set(new Coord(blackPrevious.x - 2, blackPrevious.y - 2), Box.BLACKSTART);
                            blackMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackKillContinue(coord);
                            if (blackMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;
                                blackKillQueen = 0;
                            } else {
                                whatSelected = 0;
                                checkersMap.set(new Coord(blackPrevious.x - 2, blackPrevious.y - 2), Box.BLACK);
                            }
                        } else {
                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                            blackMustKillContinue = false;
                            blackQueenMustKillContinue = false;
                            for (int y = 0; y < 8; y++) {
                                for (int x = 1; x < 9; x++) {
                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                }
                            }
                            findBlackQueenKillContinue(coord);
                            if (blackQueenMustKillContinue) {
                                blackPrevious.x = coord.x;
                                blackPrevious.y = coord.y;
                                whatSelected = 3;
                            } else {
                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                            }
                            if (!blackQueenMustKillContinue) whatSelected = 0;

                        }
                    }
                }
            }
            if (blackKillQueen == 1) {
                if (checkersMap.get(blackPrevious).toString().equals("BLACKQUEENSTART")) {
                    for (int z = 0; z < 9; z++) {
                        if (blackPrevious.x + z < 9 && blackPrevious.y + z < 8) {
                            if (checkersMap.get(new Coord(blackPrevious.x + z, blackPrevious.y + z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(blackPrevious.x + z, blackPrevious.y + z)).toString().equals("WHITEQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (blackPrevious.x + rrr < 9 && blackPrevious.y + rrr < 8) {
                                        if (checkersMap.get(new Coord(blackPrevious.x + rrr, blackPrevious.y + rrr)).toString().equals("POSSIBLE") &&
                                                blackPrevious.x + rrr == coord.x && blackPrevious.y + rrr == coord.y) {
                                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                                            checkersMap.set(new Coord(blackPrevious.x + z, blackPrevious.y + z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                                            blackQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findBlackQueenKillContinue(coord);

                                            if (blackQueenMustKillContinue) {
                                                blackPrevious.x = coord.x;
                                                blackPrevious.y = coord.y;
                                                whatSelected = 3;

                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int z = 0; z < 9; z++) {
                        if (blackPrevious.x - z > 0 && blackPrevious.y - z > -1) {
                            if (checkersMap.get(new Coord(blackPrevious.x - z, blackPrevious.y - z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(blackPrevious.x - z, blackPrevious.y - z)).toString().equals("WHITEQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (blackPrevious.x - rrr > 0 && blackPrevious.y - rrr > -1) {
                                        if (checkersMap.get(new Coord(blackPrevious.x - rrr, blackPrevious.y - rrr)).toString().equals("POSSIBLE") &&
                                                blackPrevious.x - rrr == coord.x && blackPrevious.y - rrr == coord.y) {

                                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                                            checkersMap.set(new Coord(blackPrevious.x - z, blackPrevious.y - z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                                            blackQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findBlackQueenKillContinue(coord);
                                            if (blackQueenMustKillContinue) {
                                                blackPrevious.x = coord.x;
                                                blackPrevious.y = coord.y;
                                                whatSelected = 3;


                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int z = 0; z < 9; z++) {
                        if (blackPrevious.x - z > 0 && blackPrevious.y + z < 8) {
                            if (checkersMap.get(new Coord(blackPrevious.x - z, blackPrevious.y + z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(blackPrevious.x - z, blackPrevious.y + z)).toString().equals("WHITEQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (blackPrevious.x - rrr > 0 && blackPrevious.y + rrr < 8) {
                                        if (checkersMap.get(new Coord(blackPrevious.x - rrr, blackPrevious.y + rrr)).toString().equals("POSSIBLE") &&
                                                blackPrevious.x - rrr == coord.x && blackPrevious.y + rrr == coord.y) {
                                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                                            checkersMap.set(new Coord(blackPrevious.x - z, blackPrevious.y + z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                                            blackQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findBlackQueenKillContinue(coord);
                                            if (blackQueenMustKillContinue) {
                                                blackPrevious.x = coord.x;
                                                blackPrevious.y = coord.y;
                                                whatSelected = 3;

                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int z = 0; z < 9; z++) {
                        if (blackPrevious.x + z < 9 && blackPrevious.y - z > -1) {
                            if (checkersMap.get(new Coord(blackPrevious.x + z, blackPrevious.y - z)).toString().equals("WHITE") ||
                                    checkersMap.get(new Coord(blackPrevious.x + z, blackPrevious.y - z)).toString().equals("WHITEQUEEN")) {
                                for (int rrr = 1; rrr < 8; rrr++) {
                                    if (blackPrevious.x + rrr < 9 && blackPrevious.y - rrr > -1) {
                                        if (checkersMap.get(new Coord(blackPrevious.x + rrr, blackPrevious.y - rrr)).toString().equals("POSSIBLE") &&
                                                blackPrevious.x + rrr == coord.x && blackPrevious.y - rrr == coord.y) {
                                            checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEENSTART);
                                            checkersMap.set(new Coord(blackPrevious.x + z, blackPrevious.y - z), Box.BLACKSQUARE);
                                            checkersMap.set(new Coord(blackPrevious.x, blackPrevious.y), Box.BLACKSQUARE);
                                            blackQueenMustKillContinue = false;
                                            for (int y = 0; y < 8; y++) {
                                                for (int x = 1; x < 9; x++) {
                                                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                                                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                                                }
                                            }
                                            findBlackQueenKillContinue(coord);
                                            if (blackQueenMustKillContinue) {
                                                blackPrevious.x = coord.x;
                                                blackPrevious.y = coord.y;
                                                whatSelected = 3;

                                            } else {
                                                checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!blackQueenMustKillContinue) whatSelected = 0;
            }
        } else {
            if (checkersMap.get(new Coord(coord.x, coord.y)).toString().equals("POSSIBLE")) {
                if (whoBlack == 0) checkersMap.set(new Coord(coord.x, coord.y), Box.BLACK);
                if (whoBlack == 1) checkersMap.set(new Coord(coord.x, coord.y), Box.BLACKQUEEN);
                checkersMap.set(blackPrevious, Box.BLACKSQUARE);
                whatSelected = 0;
            }

        }

        deleteBlackPossible();

        for (int x = 1; x < 9; x++) {
            if (checkersMap.get(new Coord(x, 7)).toString().equals("BLACK")) {
                checkersMap.set(new Coord(x, 7), Box.BLACKQUEEN);
            }
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 1; x < 9; x++) {
                if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKSTART")) {
                    blackKillQueen = 0;
                }
                if (checkersMap.get(new Coord(x, y)).toString().equals("BLACKQUEENSTART")) {
                    blackKillChecker = 0;
                }
            }
        }

        if (!blackMustKillContinue && !blackQueenMustKillContinue) {
            for (int y = 0; y < 8; y++) {
                for (int x = 1; x < 9; x++) {
                    if (checkersMap.get(new Coord(x, y)).toString().equals("POSSIBLE"))
                        checkersMap.set(new Coord(x, y), Box.BLACKSQUARE);
                }
            }

            blackKillQueen = 0;
            blackKillChecker = 0;
            findWhitePossible();
        }
    }

}