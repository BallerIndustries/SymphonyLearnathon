package workflow;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Dispatcher {

    static Map<String, IWorkflow> ALL_WORKFLOWS = new HashMap<>();

    public static void reset() {
        ALL_WORKFLOWS = new HashMap<>();
    }

    public static BiFunction<String, String,String> dispatch(String user, String msg) {
        String word = msg.toLowerCase().trim().split(" ")[0];
        switch(word) {
            case "list":
                return dispatchList(msg);
            case "start":
                return dispatchStart(msg);
            case "add":
                return dispatchAdd(msg);
            case "approve":
                return dispatchApprove(msg);
            case "reject":
                return dispatchReject(msg);
            default:
                return null;

        }
    }

    private static BiFunction<String,String,String> dispatchReject(String msg) {
        String word = msg.toLowerCase().trim().split(" ")[1];
        IWorkflowTemplate template = findWorkflowTemplate(word);
        if(template!=null)
            return template.reject(msg);
        IWorkflow workflow = findWorkflow("reject", word);
        if(workflow!=null)
            return workflow.reject(msg);
        return  (a,b)->"Cannot use 'reject' command for " + word;
    }

    private static BiFunction<String,String,String> dispatchApprove(String msg) {
        String word = msg.toLowerCase().trim().split(" ")[1];
        IWorkflowTemplate template = findWorkflowTemplate(word);
        if(template!=null)
            return template.approve(msg);
        IWorkflow workflow = findWorkflow("approve", word);
        if(workflow!=null)
            return workflow.approve(msg);
        return  (a,b)->"Cannot use 'approve' command for " + word;
    }

    private static BiFunction<String,String,String> dispatchAdd(String msg) {
        String word = msg.toLowerCase().trim().split(" ")[1];
        IWorkflowTemplate template = findWorkflowTemplate(word);
        if(template!=null)
            return template.add(msg);
        IWorkflow workflow = findWorkflow("add", word);
        if(workflow!=null)
            return workflow.add(msg);
        return  (a,b)->"Cannot use 'add' command for " + word;
    }

    private static IWorkflow findWorkflow(String verb, String word) {
        return ALL_WORKFLOWS.values().stream()
                .filter(workflow -> workflow.isTargetOf(verb, word))
                .findFirst()
                .orElse(null);
    }

    private static BiFunction<String,String,String> dispatchStart(String msg) {
        String word = msg.toLowerCase().trim().split(" ")[1];
        IWorkflowTemplate template = findWorkflowTemplate(word);
        if(template==null)
            return  (a,b)->"Cannot use 'start' command for " + word;
        else
            return template.start(msg);

    }


    private static BiFunction<String,String,String> dispatchList(String msg) {
        IWorkflowTemplate template;
        String word = msg.toLowerCase().trim().split(" ")[1];
        switch(word) {
            case "workflows":
                return Dispatcher::listWorkflows;
            default:
                template = findWorkflowTemplate(word);
                return template==null ? (a,b)->"Cannot use 'list' command for " + word : template.list();
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
