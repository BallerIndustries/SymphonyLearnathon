package workflow;

import java.util.function.BiFunction;

public interface IWorkflowTemplate {

    String getName();
    BiFunction<String, String,String> list();
}
