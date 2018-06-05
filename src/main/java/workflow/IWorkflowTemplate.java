package workflow;

import java.util.function.BiFunction;

public interface IWorkflowTemplate {

    String getName();
    default BiFunction<String, String, String> list() {
        return (a,b)->"Cannot use 'list' command for " + this.getName();
    }
    default BiFunction<String, String, String> start(String msg) {
        return (a,b)->"Cannot use 'start' command for " + this.getName();
    }
    default BiFunction<String, String, String> add(String msg) {
        return (a,b)->"Cannot use 'add' command for " + this.getName();
    }
    default BiFunction<String, String, String> approve(String msg) {
        return (a,b)->"Cannot use 'approve' command for " + this.getName();
    }
    default BiFunction<String, String, String> reject(String msg) {
        return (a,b)->"Cannot use 'reject' command for " + this.getName();
    }

}
