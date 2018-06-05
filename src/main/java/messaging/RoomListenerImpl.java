package messaging;

import clients.SymBotClient;
import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import model.events.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.Dispatcher;

import java.util.function.BiFunction;

public class RoomListenerImpl implements listeners.RoomListener {

    private SymBotClient botClient;

    public RoomListenerImpl(SymBotClient botClient) {
        this.botClient = botClient;
    }

    private final Logger logger = LoggerFactory.getLogger(RoomListenerImpl.class);

    public void onRoomMessage(InboundMessage inboundMessage) {
        String user = inboundMessage.getUser().getFirstName();
        String message = inboundMessage.getMessageText();
        BiFunction<String, String, String> function = Dispatcher.dispatch(user, message);
        if(function!=null) {
            String output = function.apply(user, message);
            if (output.trim().length() > 0) {
                output = StringEscapeUtils.escapeHtml4(output);
                OutboundMessage messageOut = new OutboundMessage();
                messageOut.setMessage(output);
                try {
                    this.botClient.getMessagesClient().sendMessage(inboundMessage.getStream().getStreamId(), messageOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        OutboundMessage messageOut = new OutboundMessage();
        messageOut.setMessage("Welcome "+userJoinedRoom.getAffectedUser().getFirstName()+"!");
        try {
            this.botClient.getMessagesClient().sendMessage(userJoinedRoom.getStream().getStreamId(), messageOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUserLeftRoom(UserLeftRoom userLeftRoom) {

    }
}
