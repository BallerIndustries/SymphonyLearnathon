package workflow;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WorkflowEngineTests {

    @Test
    public void workflowListIsReturned() {
        List<Workflow> list = WorflowEngine.getWorkflowsFor("eric");
        assertEquals(2, list.size());
    }
}
