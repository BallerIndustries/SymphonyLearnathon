package workflow;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class UATWorkflowTemplate extends WorkflowTemplateBase {

    static List<String> ALL_AVAILABLE = Arrays.asList("Superfly v1.2", "Summit v10.3.5");

    public UATWorkflowTemplate() {
        super("UAT approval");
    }

    @Override
    public BiFunction<String, String, String> list() {
        return (a, b)->ALL_AVAILABLE.stream()
                .collect(Collectors.joining("\n"));
    }
}
