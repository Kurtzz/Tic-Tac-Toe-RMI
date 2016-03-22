package agh.sr.kurtz.exception;

/**
 * Created by kurtz on 18.03.16.
 */
public class RoomAlreadyFullException extends RuntimeException {
    public RoomAlreadyFullException(String message) {
        super(message);
    }

    public RoomAlreadyFullException() {
        super("Room is already full");
    }
}
