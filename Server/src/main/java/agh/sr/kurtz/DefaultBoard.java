package agh.sr.kurtz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Random;

public class DefaultBoard implements Board {
    private static final Logger log = LoggerFactory.getLogger(DefaultBoard.class);
    public static final int BOARD_SIZE = 3;
    private Player xPlayer;
    private Player oPlayer;
    private Player nextMovePlayer;
    private Mark nextMoveMark;
    private int doneMovesCounter;
    private final Mark[][] board = new Mark[BOARD_SIZE][BOARD_SIZE];

    public DefaultBoard() {
        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = Mark.NONE;
            }
        }
    }

    public void startGame(Player xPlayer, Player oPlayer) throws RemoteException {
        this.xPlayer = xPlayer;
        this.oPlayer = oPlayer;

        xPlayer.onGameStart(Mark.X);
        oPlayer.onGameStart(Mark.O);

        drawNextMovePlayer();
        nextMovePlayer.onMove();
    }

    private void drawNextMovePlayer() throws RemoteException {
        Random random = new Random(System.currentTimeMillis());
        boolean circleStarts = random.nextBoolean();

        if (circleStarts) {
            nextMovePlayer = oPlayer;
            nextMoveMark = Mark.O;
        } else {
            nextMovePlayer = xPlayer;
            nextMoveMark = Mark.X;
        }
    }

    private void toggleNextMovePlayer() {
        if (nextMovePlayer == xPlayer) {
            nextMovePlayer = oPlayer;
            nextMoveMark = Mark.O;
        } else {
            nextMovePlayer = xPlayer;
            nextMoveMark = Mark.X;
        }
    }

    public String showBoard() throws RemoteException {
        StringBuilder builder = new StringBuilder();
        char[] fieldNumbers = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

        int index = 0;
        for (Mark[] marks : board) {
            for (Mark mark : marks) {
                if (Mark.NONE.equals(mark)) {
                    builder.append(fieldNumbers[index]);
                } else {
                    builder.append(mark);
                }
                builder.append(" ");
                index++;
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public void mark(int field, Player player) throws RemoteException {
        field--;
        int yPosition = field % BOARD_SIZE;
        int xPosition = field / BOARD_SIZE;

        if (!board[xPosition][yPosition].equals(Mark.NONE)) {
            throw new RemoteException("Field not allowed exception!");
        }

        board[xPosition][yPosition] = nextMoveMark;
        doneMovesCounter++;
        log.info("Board marked by {} on {}-{}", player.getNick(), xPosition, yPosition);

        notifyPlayers();

        if (weHaveWinner()) {
            player.onWin();
            toggleNextMovePlayer();
            nextMovePlayer.onLoose();
            return;
        }

        if (itWasTheLastPossibleMove()) {
            xPlayer.onDraw();
            oPlayer.onDraw();
            return;
        }

        toggleNextMovePlayer();
        nextMovePlayer.onMove();
    }

    private boolean itWasTheLastPossibleMove() {
        return doneMovesCounter == BOARD_SIZE * BOARD_SIZE;
    }

    private boolean weHaveWinner() {
        if (checkRowsAndColumns()) return true;
        if (checkCrossLines()) return true;

        return false;
    }

    private boolean checkCrossLines() {
        int length = board.length;
        int left = 0;
        int right = 0;
        for (int i = 0; i < length; i++) {
            left += board[i][i].value();
            right += board[i][length - i - 1].value();
        }

        return isWinningLine(left) || isWinningLine(right);
    }

    private boolean isWinningLine(int sum) {
        return Math.abs(sum) == BOARD_SIZE;
    }

    private boolean checkRowsAndColumns() {
        for (int i = 0; i < board.length; i++) {
            int sumRows = 0, sumColumns = 0;
            for (int j = 0; j < board[i].length; j++) {
                sumRows += board[i][j].value();
                sumColumns += board[j][i].value();
            }

            if (isWinningLine(sumRows) || isWinningLine(sumColumns)) {
                return true;
            }
        }
        return false;
    }

    private void notifyPlayers() throws RemoteException {
        xPlayer.onBoardUpdated();
        oPlayer.onBoardUpdated();
    }
}
