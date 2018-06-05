package workflow;

import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TravelAndExpensesHackingTest {

    private static final String NAEEM_ROOM = "toVneMcWvABC3IQ_KPrkcn___pwxnyXAdA";
    private static final String PACCO_ROOM = "kdanHjGHLfOOZcK5sw5kG3___pwxn1xvdA";

    TravelAndExpensesHacking travelAndExpensesHacking = new TravelAndExpensesHacking();

    InboundMessage jibberjabber(String incomingMessage) {
        InboundMessage someInboundMessage = mock(InboundMessage.class);
        Stream someStream = mock(Stream.class);
        when(someInboundMessage.getMessageText()).thenReturn(incomingMessage);
        when(someInboundMessage.getStream()).thenReturn(someStream);
        when(someStream.getStreamId()).thenReturn("someID");

        return someInboundMessage;
    }

    void assertText(InboundMessage inboundMessage, int index, String expectedResponse, String expectedStreamId) {
        List<Pair<String, OutboundMessage>> result = travelAndExpensesHacking.process(inboundMessage);
        assertEquals(result.get(index).getRight().getMessage(),expectedResponse);
        assertEquals(result.get(index).getLeft(), expectedStreamId);

    }

    @Test
    public void canHandleTravelExpensesMesssage() {
        InboundMessage someInboundMessage = jibberjabber("Start Travel Expenses");
        assertText(someInboundMessage, 0, "Upload a CSV file with your expenses. Template is attached.","someID");
    }

    @Test
    public void canHandleYes(){
        InboundMessage someInboundMessage = jibberjabber("yEs");
        assertText(someInboundMessage,0, "Pacco has gone and a trip and would like to claim some expenses. Pacco spent $200 on transportation, $100 on food and $2200 on entertainment. Details are attached. [APPROVE/REJECT]?",NAEEM_ROOM);
    }

    @Test
    public void canHandleApprove(){
        InboundMessage someInboundMessage = jibberjabber("ApProve");
        assertText(someInboundMessage, 0,"Your travel expenses have been fully approved and you will be refunded $2500.",PACCO_ROOM);
        assertText(someInboundMessage,1,"Great! Thanks for approving.","someID");
    }

    @Test
    public void canHandleUnexpectedMessages() {
        InboundMessage someInboundMessage = jibberjabber("WOAahahhh what an unexpected message dude!!!!");
        assertEquals(travelAndExpensesHacking.process(someInboundMessage).size(), 0);
    }

    @Test
    public void canHandleReject(){
        InboundMessage someInboundMessage = jibberjabber("reject");
        assertText(someInboundMessage, 0,"Your travel expenses have been rejected for the trip named 'PACCOS_BOOZY_TRIP_2018'.",PACCO_ROOM);
        assertText(someInboundMessage,1,"Alright, these expenses have been rejected.","someID");
    }
}
