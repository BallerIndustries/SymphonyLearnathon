package workflow;

import app.ApprovalBot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.SymClientException;
import messaging.RoomListenerImpl;
import model.InboundMessage;
import model.OutboundMessage;
import model.UserInfo;

import javax.ws.rs.core.NoContentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static messaging.RoomListenerImpl.INBOUND_MESSAGE;
import static workflow.Approval.Status.APPROVED;
import static workflow.Approval.Status.PENDING;
import static workflow.Approval.Status.REJECTED;

public class UATWorkflow implements IWorkflow {

    String roomId;
    String roomName;
    String uat;
    List<Approval> approvals = new ArrayList<>();

    public UATWorkflow(String roomId, String roomName, String uat) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.uat = uat;
    }

    @Override
    public boolean isTargetOf(String verb, String word) {
        if ("approve".equals(verb) || "reject".equals(verb))
            return roomIsChatRoomOfThisUAT();
        else if("add".equals(verb)) {
            switch (word.toLowerCase()) {
                case "approver":
                    return roomIsChatRoomOfThisUAT();
                default:
                    return false;
            }
        } else
            return false;
    }

    private boolean roomIsChatRoomOfThisUAT() {
        InboundMessage message = INBOUND_MESSAGE.get();
        return message.getStream().getStreamId().equals(roomId);
    }

    @Override
    public BiFunction<String, String, String> add(String msg) {
        return (user, message) -> processAdd(message);
    }

    public String processAdd(String text) {
        // TODO switch on add 'what'
        try {
            return addApprovers(text);
        } catch(Throwable t) {
            t.printStackTrace();
            return t.getMessage();
        }
    }

    private String addApprovers(String text) throws IOException {
        List<Long> approvers = collectApproverIds();
        return approvers.stream().map(approverId->{
            Approval approval = findApprovalByApproverId(approverId);
            if(approval!=null)
                return "@" + approval.approverName + " is already an approver!";
            else try {
                approval = new Approval();
                approval.approverId = approverId;
                approval.approverName = getApproverNameFromId(approverId);
                approval.status = Approval.Status.PENDING;
                approvals.add(approval);
                addApproverToRoom(approverId);
                sendWelcomeToApprover(approverId, approval.approverName);
                return "@" + approval.approverName + " added as approver.";
            } catch(Throwable t) {
                t.printStackTrace();
                throw new RuntimeException(t);
            }
        }).collect(Collectors.joining("\n"));
    }

    private Approval findApprovalByApproverId(Long approverId) {
        return approvals.stream()
                .filter(a->a.approverId.equals(approverId))
                .findFirst().orElse(null);
    }

    private void sendWelcomeToApprover(Long approverId, String approverName) throws SymClientException {
        String content = "Hi " + approverName + ", your approval is requested for '" + this.uat +
                "'. A dedicated chat room '" + this.roomName + "' has been created. " +
                "Please go there and 'approve' or 'reject'.";
        String streamId = ApprovalBot.app.getBotClient().getStreamsClient().getUserIMStreamId(approverId);
        OutboundMessage message = new OutboundMessage();
        message.setMessage(content);
        ApprovalBot.app.getBotClient().getMessagesClient().sendMessage(streamId, message);
    }

    private void addApproverToRoom(Long approverId) throws SymClientException {
        ApprovalBot.app.getBotClient().getStreamsClient().addMemberToRoom(roomId, approverId);
    }

    private String getApproverNameFromId(Long approverId) throws SymClientException, NoContentException {
        UserInfo info = ApprovalBot.app.getBotClient().getUsersClient().getUserFromId(approverId, true);
        return info.getDisplayName();
    }

    private List<Long> collectApproverIds() throws IOException {
        List<Long> approvers = new ArrayList<>();
        String jsonText = RoomListenerImpl.INBOUND_MESSAGE.get().getData();
        JsonNode json = new ObjectMapper().readTree(jsonText);
        for (JsonNode child : json) {
            JsonNode id = child.get("id");
            JsonNode item = id.get(0);
            String type = item.get("type").asText();
            if (type.contains("userId")) {
                Long value = item.get("value").asLong();
                approvers.add(value);
            }
        }
        return approvers;
    }

    @Override
    public BiFunction<String, String, String> approve(String msg) {
        return (user, message) -> processStatus(message, APPROVED);
    }

    @Override
    public BiFunction<String, String, String> reject(String msg) {
        return (user, message) -> processStatus(message, REJECTED);
    }

    private String processStatus(String message, Approval.Status status) {
        InboundMessage inbound = RoomListenerImpl.INBOUND_MESSAGE.get();
        Long sender = inbound.getUser().getUserId();
        Approval approval = findApprovalByApproverId(sender);
        if(approval==null)
            return "You are not a registered approver for '" + uat + "'.";
        else {
            if(approval.status!=PENDING) {
                return "You have already " + approval.status.name().toLowerCase() + " '" + uat + "'.";
            } else {
                approval.status = status;
                return checkFullyApproved();
            }
        }
    }

    private String checkFullyApproved() {
        if(approvals.stream().anyMatch(approval->approval.status==Approval.Status.REJECTED))
            return "'" + uat + "' has been rejected. No further action is possible.";
        else if(approvals.stream().allMatch(approval->approval.status== APPROVED))
            return "'" + uat + "' is now fully approved. No further action is required.";
        else
            return "You have approved '" + uat + "'. Other approvals are pending.";

    }


}
