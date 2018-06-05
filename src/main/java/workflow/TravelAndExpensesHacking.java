package workflow;

import model.InboundMessage;
import model.OutboundMessage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TravelAndExpensesHacking {

    private static final String NAEEM_ROOM = "toVneMcWvABC3IQ_KPrkcn___pwxnyXAdA";
    private static final String PACCO_ROOM = "kdanHjGHLfOOZcK5sw5kG3___pwxn1xvdA";

    public List<Pair<String, OutboundMessage>> process(InboundMessage inboundMessage) {

        OutboundMessage outboundMessage = new OutboundMessage();
        String msg = inboundMessage.getMessageText().toUpperCase().trim();
        String streamId = inboundMessage.getStream().getStreamId();
        List<Pair<String, OutboundMessage>> messages = new ArrayList<>();

        String outMsg;
        switch (msg) {
            case "UPLOADED CSV FILE":
                outMsg = "Thanks for the file. You spent $200 on transportation, $100 on food and $2200 on entertainment.\n" +
                        "If this is say yes, otherwise, make changes and reupload the CSV file.";
                break;

            case "TRAVEL EXPENSES":
                outMsg = "Upload a CSV file with your expenses. Template is attached.";
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource("tande_template.csv").getFile());
                outboundMessage.setAttachment(new File[]{file});
                break;

            case "YES":
                outMsg = "Great! Sending to Naeem Ahmed for approval";
                OutboundMessage naeemMessage = new OutboundMessage();
                naeemMessage.setMessage("Yo Naeem, wassup? Pacco has gone and a trip and would like to claim some expenses.\n" +
                        "Pacco spent $200 on transportation, $100 on food and $2200 on entertainment.\n" +
                        "All details are attached to this message.");

                ClassLoader classLoader2 = getClass().getClassLoader();
                File file2 = new File(classLoader2.getResource("paccos-boozy-trip.csv").getFile());

                naeemMessage.setAttachment(new File[] {file2});
                messages.add(new ImmutablePair<>(NAEEM_ROOM, naeemMessage));
                break;

            case "APPROVE":
                outMsg = "Great! Thanks for approving.";
                OutboundMessage paccoMessage = new OutboundMessage();
                paccoMessage.setMessage("You're travel expenses have been fully approved and you will be refunded $2500 for the trip\n" +
                        "named 'PACCOS_BOOZY_TRIP_2018'. See ya later alligator!");
                messages.add(new ImmutablePair<>(PACCO_ROOM, paccoMessage));
                break;

            default:
                outMsg = "UNHANDLED MESSAGE";
                break;
        }

        outboundMessage.setMessage(outMsg);
        messages.add(new ImmutablePair<>(streamId, outboundMessage));
        return messages;
    }
}
