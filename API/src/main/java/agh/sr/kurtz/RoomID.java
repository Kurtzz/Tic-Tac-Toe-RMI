package agh.sr.kurtz;

import java.io.Serializable;
import java.util.UUID;

public class RoomID implements Serializable {
    private UUID uuid;

    public RoomID(UUID uuid) {
        this.uuid = uuid;
    }

    public RoomID() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomID roomID = (RoomID) o;

        if (uuid != null ? !uuid.equals(roomID.uuid) : roomID.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
