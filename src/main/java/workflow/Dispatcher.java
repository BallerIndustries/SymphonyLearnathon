package workflow;

import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Dispatcher {

    public static BiFunction<String, String,String> dispatch(String user, String msg) {

        switch(msg.toLowerCase().trim()) {
            case "list workflows":
                return Dispatcher::listWorkflows;
            default:
                return null;

        }
    }

    public static String listWorkflows(String user, String message) {
        return WorflowEngine.getWorkflowsFor(user).stream()
                .map(IWorkflow::getName)
                .collect(Collectors.joining("\n"));
   }

}
