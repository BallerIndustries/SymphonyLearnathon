package workflow;

import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Dispatcher {

    public static BiFunction<String, String,String> dispatch(String user, String msg) {
        String word = msg.toLowerCase().trim().split(" ")[0];
        switch(word) {
            case "list":
                return dispatchList(msg);
            case "start":
                return dispatchStart(msg);
            default:
                return null;

        }
    }

    private static BiFunction<String,String,String> dispatchStart(String msg) {
        String word = msg.toLowerCase().trim().split(" ")[1];
        IWorkflowTemplate workflow = findWorkflowTemplate(word);
        if(workflow==null)
            return  (a,b)->"Cannot use 'start' command for " + word;
        else
            return workflow.start(msg);

    }


    private static BiFunction<String,String,String> dispatchList(String msg) {
        IWorkflowTemplate workflow;
        String word = msg.toLowerCase().trim().split(" ")[1];
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
