package workflow;

import java.util.Arrays;
import java.util.List;

public class WorflowEngine {

    static List<IWorkflowTemplate> ALL_WORKFLOWS = Arrays.asList(
            new UATWorkflowTemplate(),
            new TravelAndExpensesWorkflowTemplate()
    );

    public static List<IWorkflowTemplate> getWorkflowsFor(String eric) {
        return ALL_WORKFLOWS;
    }
}
