package agh.sr.kurtz.bot;

import agh.sr.kurtz.DefaultBoard;

import java.util.Random;

public class RandomBotLogic implements BotLogic {
    private Random random = new Random(System.currentTimeMillis());
    public int nextMove() {
        return random.nextInt(DefaultBoard.BOARD_SIZE * DefaultBoard.BOARD_SIZE + 1);
    }
}
