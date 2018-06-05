package workflow;

public abstract class WorkflowTemplateBase implements IWorkflowTemplate {

    String name;

    public WorkflowTemplateBase(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
