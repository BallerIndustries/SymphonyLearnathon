package workflow;

import java.util.Arrays;
import java.util.List;

public class WorflowEngine {

    public static IWorkflowTemplate UAT_WORKFLOW = new UATWorkflowTemplate();
//    public static IWorkflowTemplate TAE_WORKFLOW = new TravelAndExpensesWorkflowTemplate();

    static List<IWorkflowTemplate> ALL_TEMPLATES = Arrays.asList(
            UAT_WORKFLOW/*,*/
//            TAE_WORKFLOW
    );

    public static List<IWorkflowTemplate> getWorkflowTemplatesFor(String eric) {
        return ALL_TEMPLATES;
    }

    public static List<IWorkflowTemplate> getAllWorkflowTemplates() {
        return ALL_TEMPLATES;
    }
}
