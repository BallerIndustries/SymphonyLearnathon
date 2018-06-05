package workflow;

import model.Attachment;
import model.InboundMessage;
import model.OutboundMessage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TravelAndExpensesHacking {

    private static final String NAEEM_ROOM = "toVneMcWvABC3IQ_KPrkcn___pwxnyXAdA";
    private static final String PACCO_ROOM = "kdanHjGHLfOOZcK5sw5kG3___pwxn1xvdA";

    List<Pair<String, OutboundMessage>> processAttachments(InboundMessage inboundMessage) {
        if (inboundMessage.getAttachments() == null || inboundMessage.getAttachments().size() <= 0) {
            return null;
        }

        Attachment recievedFile = inboundMessage.getAttachments().get(0);

        if (!recievedFile.getName().endsWith(".csv")) {
            return null;
        }

        List<Pair<String, OutboundMessage>> messages = new ArrayList<>();
        OutboundMessage outboundMessage = new OutboundMessage();
        outboundMessage.setMessage("You spent $200 on transportation, $100 on food and $2200 on entertainment. If this is correct, say YES, otherwise, make changes and reupload the CSV file.");
        String streamId = inboundMessage.getStream().getStreamId();
        Pair<String, OutboundMessage> pair = new ImmutablePair<>(streamId, outboundMessage);
        messages.add(pair);

        return messages;
    }

    List<Pair<String, OutboundMessage>> processText(InboundMessage inboundMessage) {
        List<Pair<String, OutboundMessage>> messages = new ArrayList<>();

        String msg = inboundMessage.getMessageText().toUpperCase();

        String outMsg = null;
        OutboundMessage outboundMessage = new OutboundMessage();

        switch (msg) {

            case "TRAVEL EXPENSES":
                outMsg = "Upload a CSV file with your expenses. Template is attached.";
                File file = Utils.getInstance().getFileFromResources("tande_template.csv");
                outboundMessage.setAttachment(new File[]{file});
                break;

            case "YES":
                outMsg = "Great! Sending to Naeem Ahmed for approval";
                OutboundMessage naeemMessage = new OutboundMessage();
                naeemMessage.setMessage("Pacco has gone and a trip and would like to claim some expenses. Pacco spent $200 on transportation, $100 on food and $2200 on entertainment. Details are attached. [APPROVE/REJECT]?");

                File file2 = Utils.getInstance().getFileFromResources("paccos-boozy-trip.csv");

                naeemMessage.setAttachment(new File[]{file2});
                messages.add(new ImmutablePair<>(NAEEM_ROOM, naeemMessage));
                break;

            case "APPROVE":
                outMsg = "Great! Thanks for approving.";
                OutboundMessage paccoMessage = new OutboundMessage();
                paccoMessage.setMessage("Your travel expenses have been fully approved and you will be refunded $2500.");
                messages.add(new ImmutablePair<>(PACCO_ROOM, paccoMessage));
                break;

            case "REJECT":
                outMsg = "Alright, these expenses have been rejected.";
                OutboundMessage paccoMessage2 = new OutboundMessage();
                paccoMessage2.setMessage("Your travel expenses have been rejected for the trip named 'PACCOS_BOOZY_TRIP_2018'.");
                messages.add(new ImmutablePair<>(PACCO_ROOM, paccoMessage2));
                break;

            default:
                outMsg = "UNHANDLED MESSAGE";
                break;
        }

        outboundMessage.setMessage(outMsg);
        messages.add(new ImmutablePair<>(inboundMessage.getStream().getStreamId(), outboundMessage));
        return messages;
    }

    public List<Pair<String, OutboundMessage>> process(InboundMessage inboundMessage) {
        List<Pair<String, OutboundMessage>> attachmentResult = processAttachments(inboundMessage);
        List<Pair<String, OutboundMessage>> result = processText(inboundMessage);

        if (attachmentResult != null) {
            return attachmentResult;
        }
        if (result != null) {
            return result;
        }
        else {
            throw new RuntimeException("Did not expect that to happen");
        }
    }
}
