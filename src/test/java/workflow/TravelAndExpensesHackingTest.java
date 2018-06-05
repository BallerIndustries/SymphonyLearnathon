package workflow;

import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
        Assert.assertEquals(result.get(index).getRight().getMessage(),expectedResponse);
        Assert.assertEquals(result.get(index).getLeft(), expectedStreamId);

    }


    @Test
    public void canHandleTravelExpensesMesssage() {
        InboundMessage someInboundMessage = jibberjabber("Travel Expenses");
        assertText(someInboundMessage, 0, "Upload a CSV file with your expenses. Template is attached.","someID");
    }

    @Test
    public void canHandleYes(){
        InboundMessage someInboundMessage = jibberjabber("yEs");
        assertText(someInboundMessage,0, "Yo Naeem, wassup? Pacco has gone and a trip and would like to claim some expenses. Pacco spent $200 on transportation, $100 on food and $2200 on entertainment. All details are attached to this message.",NAEEM_ROOM);
    }

    @Test
    public void canHandleApprove(){
        InboundMessage someInboundMessage = jibberjabber("ApProve");
        assertText(someInboundMessage, 0,"You're travel expenses have been fully approved and you will be refunded $2500 for the trip named 'PACCOS_BOOZY_TRIP_2018'. See ya later alligator!",PACCO_ROOM);
        assertText(someInboundMessage,1,"Great! Thanks for approving.","someID");
    }

    @Test
    public void canHandleUnexpectedMessages() {
        InboundMessage someInboundMessage = jibberjabber("WOAahahhh what an unexpected message dude!!!!");
        assertText(someInboundMessage, 0,"UNHANDLED MESSAGE","someID");
    }


}