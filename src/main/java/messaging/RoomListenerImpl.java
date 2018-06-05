package messaging;

import clients.SymBotClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import model.events.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import workflow.Dispatcher;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.util.function.BiFunction;

public class RoomListenerImpl implements listeners.RoomListener {

    private SymBotClient botClient;

    public RoomListenerImpl(SymBotClient botClient) {
        this.botClient = botClient;
    }

    private final Logger logger = LoggerFactory.getLogger(RoomListenerImpl.class);

    public static ThreadLocal<InboundMessage> INBOUND_MESSAGE = new ThreadLocal<>();

    public void onRoomMessage(InboundMessage message) {
        try {
            INBOUND_MESSAGE.set(message);
            processRoomMessage();
        } catch(Throwable t) {
            t.printStackTrace();
        } finally {
            INBOUND_MESSAGE.set(null);
        }
    }

    public void processRoomMessage() {
        InboundMessage message = INBOUND_MESSAGE.get();
        String user = message.getUser().getFirstName();
        String messageML = message.getMessage();
        String text = extractText(messageML);
        BiFunction<String, String, String> function = Dispatcher.dispatch(user, text);
        if(function!=null) {
            String output = function.apply(user, text);
            if (output!=null && output.trim().length() > 0) {
                output = StringEscapeUtils.escapeHtml4(output);
                OutboundMessage messageOut = new OutboundMessage();
                messageOut.setMessage(output);
                try {
                    this.botClient.getMessagesClient().sendMessage(message.getStream().getStreamId(), messageOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String extractText(String text) {
        try {
            StringBuilder sb = new StringBuilder();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new ByteArrayInputStream(text.getBytes())), new DefaultHandler() {

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    sb.append(ch, start, length);
                }
            });
            return sb.toString();
        } catch(Throwable t) {
            throw new RuntimeException(t);
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
