package agh.sr.kurtz;

public enum GameResult {
    WIN("You won!"),
    LOST("You loose..."),
    DRAW("It's a draw!");

    private String message;

    GameResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
