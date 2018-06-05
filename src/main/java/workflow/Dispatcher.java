package workflow;

import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Dispatcher {

    public static BiFunction<String, String,String> dispatch(String user, String msg) {
        String[] words = msg.toLowerCase().trim().split(" ");
        switch(words[0]) {
            case "list":
                return dispatchList(words[1]);
            default:
                return null;

        }
    }

    private static BiFunction<String,String,String> dispatchList(String word) {
        IWorkflowTemplate workflow;
        switch(word) {
            case "workflows":
                return Dispatcher::listWorkflows;
            default:
                workflow = findWorkflowTemplate(word);
                return workflow==null ? (a,b)->"Cannot use 'list' command for " + word : workflow.list();
        }
    }

    private static IWorkflowTemplate findWorkflowTemplate(String word) {
        return WorflowEngine.getAllWorkflowTemplates().stream()
                .filter(wt->wt.getName().toLowerCase().startsWith(word))
                .findFirst()
                .orElse(null);
    }

    public static String listWorkflows(String user, String message) {
        return WorflowEngine.getWorkflowTemplatesFor(user).stream()
                .map(IWorkflowTemplate::getName)
                .collect(Collectors.joining("\n"));
   }

}
