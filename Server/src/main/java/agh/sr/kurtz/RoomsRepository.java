package agh.sr.kurtz;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RoomsRepository {
    private Map<RoomID, Room> rooms = new ConcurrentHashMap<RoomID, Room>();

    public synchronized Room load(RoomID roomID) {
        return rooms.get(roomID);
    }

    public synchronized void addRoom(Room newRoom) {
        try {
            if (!rooms.containsValue(newRoom)) {
                rooms.put(newRoom.getID(), newRoom);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Set<Room> view() {
        return new HashSet<Room>(rooms.values());
    }
}
