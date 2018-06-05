package workflow;

import java.util.function.BiFunction;

public interface IWorkflow {

    default BiFunction<String,String,String> add(String msg) {
        throw new UnsupportedOperationException();
    }

    default boolean isTargetOf(String verb, String word) {
        throw new UnsupportedOperationException();
    }
}
