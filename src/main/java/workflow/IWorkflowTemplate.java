package workflow;

import java.util.function.BiFunction;

public interface IWorkflowTemplate {

    String getName();
    BiFunction<String, String,String> list();
    BiFunction<String,String,String> start(String msg);
    BiFunction<String,String,String> add(String msg);
}
