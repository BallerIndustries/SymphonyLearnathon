package workflow;

import app.ApprovalBot;
import clients.SymBotClient;
import messaging.RoomListenerImpl;
import model.*;

import javax.ws.rs.core.NoContentException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class UATWorkflowTemplate extends WorkflowTemplateBase {

    static List<String> ALL_AVAILABLE = Arrays.asList("Superfly v1.2", "Summit v10.3.5");

    public UATWorkflowTemplate() {
        super("UAT approval");
    }

    @Override
    public BiFunction<String, String, String> list() {
        return (a, b)->ALL_AVAILABLE.stream()
                .collect(Collectors.joining("\n"));
    }

    @Override
    public BiFunction<String, String, String> start(String msg) {
        if(msg.startsWith("start "))
            msg = msg.substring("start ".length());
        if(msg.toLowerCase().startsWith("uat "))
            msg = msg.substring("uat ".length());
        if(msg.toLowerCase().startsWith("approval "))
            msg = msg.substring("approval ".length());
        String uat = msg;
        if(!ALL_AVAILABLE.contains(msg))
            return (a, b)->"No such UAT to approve: " + uat;
        else {
            String roomName = uat + " UAT approval";
            createChatRoomFor(roomName, uat);
            return (a,b)->"A chat room '" + roomName + "' has been opened";
        }
    }

    private String createChatRoomFor(String roomName, String uat){
        SymBotClient botClient = ApprovalBot.app.getBotClient();
        InboundMessage inbound = RoomListenerImpl.INBOUND_MESSAGE.get();
        try {
            Room room = new Room();
            room.setName(roomName);
            room.setDescription("Chat room for approving " + uat);
            room.setDiscoverable(true);
            room.setPublic(true);
            room.setViewHistory(true);
            RoomInfo roomInfo = botClient.getStreamsClient().createRoom(room);
            String roomId = roomInfo.getRoomSystemInfo().getId();
            UserInfo requester = botClient.getUsersClient().getUserFromEmail(inbound.getUser().getEmail(), true);
            botClient.getStreamsClient().addMemberToRoom(roomId,requester.getId());
            botClient.getStreamsClient().promoteUserToOwner(roomId, requester.getId());
            UserInfo thisBot = botClient.getUsersClient().getUserFromEmail("bot.user80@example.com", true);
            botClient.getStreamsClient().addMemberToRoom(roomId,thisBot.getId());
            return roomId;
        } catch (Throwable e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
