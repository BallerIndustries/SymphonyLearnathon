package messaging;

import model.InboundMessage;
import model.Stream;
import model.events.*;

/**
 * Created by nerd-they-nerd-jack on 3/6/2018.
 */
public class TestRoomListener implements listeners.RoomListener {

    public boolean roomCreated = false;

    public void onRoomMessage(InboundMessage inboundMessage) {
        roomCreated = true;
    }

    public void onRoomCreated(RoomCreated roomCreated) {

    }

    public void onRoomDeactivated(RoomDeactivated roomDeactivated) {

    }

    public void onRoomMemberDemotedFromOwner(RoomMemberDemotedFromOwner roomMemberDemotedFromOwner) {

    }

    public void onRoomMemberPromotedToOwner(RoomMemberPromotedToOwner roomMemberPromotedToOwner) {

    }

    public void onRoomReactivated(Stream stream) {

    }

    public void onRoomUpdated(RoomUpdated roomUpdated) {

    }

    public void onUserJoinedRoom(UserJoinedRoom userJoinedRoom) {

    }

    public void onUserLeftRoom(UserLeftRoom userLeftRoom) {

    }
}
