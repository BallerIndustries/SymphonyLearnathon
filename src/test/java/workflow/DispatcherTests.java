package workflow;

import org.junit.Test;

import java.util.function.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DispatcherTests {

    @Test
    public void listWorkflowsDispatchesToListWorkflows() {
        String cmd = "list workflows";
        BiFunction<String, String, String> dispatched = Dispatcher.dispatch("eric", cmd);
        assertNotNull(dispatched);
        String expected = "UAT approval\n" + "T&E approval";
        String message = dispatched.apply("eric", cmd);
        assertEquals(expected, message);
    }

    @Test
    public void listUATDispatchesToListUAT() {
        String cmd = "list UAT";
        BiFunction<String, String, String> dispatched = Dispatcher.dispatch("eric", cmd);
        assertNotNull(dispatched);
        String expected = "Superfly v1.2\n" + "Summit v10.3.5";
        String message = dispatched.apply("eric", cmd);
        assertEquals(expected, message);
    }

    @Test
    public void startUATSummitRequestsApprover() {
        String cmd = "start UAT Superfly v1.2";
        BiFunction<String, String, String> dispatched = Dispatcher.dispatch("eric", cmd);
        assertNotNull(dispatched);
        String expected = "A chat room 'Superfly v1.2 UAT approval' has been opened";
        String message = dispatched.apply("eric", cmd);
        assertEquals(expected, message);
    }
    @Test
    public void startTEApproval() {
        BiFunction<String, String, String> dispatched = Dispatcher.dispatch("eric", "start T&E approval");
        assertNotNull(dispatched);
    }
}
