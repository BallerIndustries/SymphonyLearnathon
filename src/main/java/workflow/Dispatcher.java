package workflow;

import java.util.function.Function;

public class Dispatcher {

    public static Function<String,String> dispatch(String msg) {

        switch(msg.toLowerCase().trim()) {
            case "list workflows":
                return Dispatcher::listWorkflows;
            default:
                return null;

        }
    }

    public static String listWorkflows(String message) {
        return "empty";
    }

}
