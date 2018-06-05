package workflow;

import org.junit.Test;

import java.util.function.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DispatcherTests {

    @Test
    public void listWorkflowsDispatchesToListWorkflows() {
        BiFunction<String, String, String> dispatched = Dispatcher.dispatch("eric", "list workflows");
        assertNotNull(dispatched);
        String expected = "UAT approval\n" + "T&E approval";
        String message = dispatched.apply("eric", "list workflows");
        assertEquals(expected, message);
    }

    @Test
    public void listUATDispatchesToListUAT() {
        BiFunction<String, String, String> dispatched = Dispatcher.dispatch("eric", "list UAT");
        assertNotNull(dispatched);
        String expected = "Superfly v1.2\n" + "Summit v10.3.5";
        String message = dispatched.apply("eric", "list UAT");
        assertEquals(expected, message);
    }
    @Test
    public void startTEApproval() {
        BiFunction<String, String, String> dispatched = Dispatcher.dispatch("eric", "start T&E approval");
        assertNotNull(dispatched);
    }
}
