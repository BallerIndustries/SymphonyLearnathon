package workflow;

public class SimpleWorkflow implements IWorkflow {

    String name;

    public SimpleWorkflow(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
