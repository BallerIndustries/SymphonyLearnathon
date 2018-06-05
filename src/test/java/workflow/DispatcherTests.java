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
}
