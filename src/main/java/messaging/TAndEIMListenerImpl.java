package messaging;

import clients.SymBotClient;
import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import org.apache.commons.lang3.StringEscapeUtils;
import workflow.Dispatcher;
import workflow.TravelAndExpensesHacking;

import java.util.function.BiFunction;

public class TAndEIMListenerImpl implements listeners.IMListener {

    private SymBotClient botClient;

    public TAndEIMListenerImpl(SymBotClient botClient) {
        this.botClient = botClient;
    }

    private TravelAndExpensesHacking hacking = new TravelAndExpensesHacking();

    public void onIMMessage(InboundMessage inboundMessage) {
        String user = inboundMessage.getUser().getFirstName();
        String message = inboundMessage.getMessageText();

        System.out.println("message = " + message);

//        OutboundMessage outboundMessage = hacking.process(inboundMessage);
//        try {
//            this.botClient.getMessagesClient().sendMessage(inboundMessage.getStream().getStreamId(), outboundMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        BiFunction<String, String, String> function = Dispatcher.dispatch(user, message);
//        if(function!=null) {
//            String output = function.apply(user, message);
//            if (output.trim().length() > 0) {
//                output = StringEscapeUtils.escapeHtml4(output);
//                OutboundMessage messageOut = new OutboundMessage();
//                messageOut.setMessage(output);
//                try {
//                    this.botClient.getMessagesClient().sendMessage(inboundMessage.getStream().getStreamId(), messageOut);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public void onIMCreated(Stream stream) {

    }
}
