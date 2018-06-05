package workflow;

import org.junit.Test;

import java.util.function.*;

import static org.junit.Assert.assertNotNull;


public class DispatcherTests {

    @Test
    public void listWorkflowsDispatchesToListWorkflows() {
        Function<String, String> dispatched = Dispatcher.dispatch("list workflows");
        assertNotNull(dispatched);
    }
}
