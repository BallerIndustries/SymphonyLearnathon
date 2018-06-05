package workflow;

import java.util.function.BiFunction;

public abstract class WorkflowTemplateBase implements IWorkflowTemplate {

    String name;

    public WorkflowTemplateBase(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BiFunction<String, String, String> list() {
        return (a,b)->"Cannot use 'list' command for " + this.getName();
    }
}
