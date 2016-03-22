package agh.sr.kurtz;

public enum Mark {
    NONE(0), X(1), O(-1);

    private int val;

    Mark(int val) {
        this.val = val;
    }

    public int value() {
        return val;
    }
}
