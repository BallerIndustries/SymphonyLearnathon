package workflow;

import app.ApprovalBot;
import clients.SymBotClient;
import exceptions.SymClientException;
import messaging.RoomListenerImpl;
import model.*;

import javax.ws.rs.core.NoContentException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class UATWorkflowTemplate extends WorkflowTemplateBase {

    static List<String> ALL_AVAILABLE = Arrays.asList("Superfly v1.2", "Summit v10.3.5");


    public static void reset() {
        ALL_AVAILABLE = Arrays.asList("Superfly v1.2", "Summit v10.3.5");
    }

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
            UATWorkflow workflow = findExistingWorkflow(uat);
            if(workflow!=null)
                return (a,b)->"A chat room '" + roomName + "' already exists";
            else try {
                String roomId = createChatRoomFor(roomName, uat);
                sendWelcomeToChatRoom(roomId, roomName, uat);
                sendInstructionToChatRoom(roomId, roomName, uat);
                workflow = new UATWorkflow(roomId, roomName, uat);
                Dispatcher.ALL_WORKFLOWS.put(uat, workflow);
                return (a,b)->"A chat room '" + roomName + "' has been opened";
            } catch(Throwable t) {
                t.printStackTrace();
                return (a,b)->t.getMessage();
            }
        }
    }

    private UATWorkflow findExistingWorkflow(String uat) {
        return (UATWorkflow)Dispatcher.ALL_WORKFLOWS.get(uat);
    }

    private void sendWelcomeToChatRoom(String roomId, String roomName, String uat) throws SymClientException {
        SymBotClient botClient = ApprovalBot.app.getBotClient();
        OutboundMessage message = new OutboundMessage();
        message.setMessage("Welcome to " + roomName);
        botClient.getMessagesClient().sendMessage(roomId, message);
    }

    private void sendInstructionToChatRoom(String roomId, String roomName, String uat) throws SymClientException {
        SymBotClient botClient = ApprovalBot.app.getBotClient();
        OutboundMessage message = new OutboundMessage();
        message.setMessage("To add approvers, type 'add approver @ApproveName'");
        botClient.getMessagesClient().sendMessage(roomId, message);
    }


    private String createChatRoomFor(String roomName, String uat) throws SymClientException, NoContentException {
        SymBotClient botClient = ApprovalBot.app.getBotClient();
        InboundMessage inbound = RoomListenerImpl.INBOUND_MESSAGE.get();
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
    }

}
