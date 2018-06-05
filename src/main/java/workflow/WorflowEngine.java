package workflow;

import java.util.Arrays;
import java.util.List;

public class WorflowEngine {

    static List<IWorkflow> ALL_WORKFLOWS = Arrays.asList(
            new SimpleWorkflow("RFC 2341 approval"),
            new SimpleWorkflow("GARS approvals")
    );

    public static List<IWorkflow> getWorkflowsFor(String eric) {
        return ALL_WORKFLOWS;
    }
}
