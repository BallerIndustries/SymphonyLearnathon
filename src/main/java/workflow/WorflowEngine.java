package workflow;

import java.util.Arrays;
import java.util.List;

public class WorflowEngine {

    static List<IWorkflowTemplate> ALL_TEMPLATES = Arrays.asList(
            new UATWorkflowTemplate(),
            new TravelAndExpensesWorkflowTemplate()
    );

    public static List<IWorkflowTemplate> getWorkflowTemplatesFor(String eric) {
        return ALL_TEMPLATES;
    }

    public static List<IWorkflowTemplate> getAllWorkflowTemplates() {
        return ALL_TEMPLATES;
    }
}
