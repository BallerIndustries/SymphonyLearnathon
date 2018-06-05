package messaging;

import clients.SymBotClient;
import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import model.events.*;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.TravelAndExpensesHacking;

import java.util.List;

public class TAndERoomListenerImpl implements listeners.RoomListener {

    private SymBotClient botClient;
    private TravelAndExpensesHacking hacking = new TravelAndExpensesHacking();

    public TAndERoomListenerImpl(SymBotClient botClient) {
        this.botClient = botClient;
    }

    private final Logger logger = LoggerFactory.getLogger(TAndERoomListenerImpl.class);

    public static ThreadLocal<InboundMessage> INBOUND_MESSAGE = new ThreadLocal<>();

    public void onRoomMessage(InboundMessage inboundMessage) {
        try {
            String message = inboundMessage.getMessageText();
            System.out.println("room message = " + message);
            List<Pair<String, OutboundMessage>> messages = hacking.process(inboundMessage);

            for (Pair<String, OutboundMessage> pair : messages) {
                this.botClient.getMessagesClient().sendMessage(pair.getLeft(), pair.getRight());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
        messageOut.setMessage("Welcome " + userJoinedRoom.getAffectedUser().getFirstName() + "!");
        try {
            this.botClient.getMessagesClient().sendMessage(userJoinedRoom.getStream().getStreamId(), messageOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUserLeftRoom(UserLeftRoom userLeftRoom) {

    }
}
