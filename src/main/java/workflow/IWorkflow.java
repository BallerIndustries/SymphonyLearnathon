package workflow;

import java.util.function.BiFunction;

public interface IWorkflow {

    default boolean isTargetOf(String verb, String word) {
        throw new UnsupportedOperationException();
    }

    default BiFunction<String,String,String> add(String msg) {
        throw new UnsupportedOperationException();
    }

    default BiFunction<String,String,String> approve(String msg) {
        throw new UnsupportedOperationException();
    }

    default BiFunction<String,String,String> reject(String msg) {
        throw new UnsupportedOperationException();
    }

}
