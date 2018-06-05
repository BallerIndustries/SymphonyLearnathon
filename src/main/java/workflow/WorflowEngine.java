package workflow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorflowEngine {

    static List<Workflow> ALL_WORKFLOWS = Arrays.asList(
            new Workflow(),
            new Workflow()
    );

    public static List<Workflow> getWorkflowsFor(String eric) {
        return ALL_WORKFLOWS;
    }
}
