package com.jekton.mobilelearn.common.network.operator;

import java.util.HashMap;

/**
 * Note: We DO NOT want to provide static factory methods that takes an instance of
 * {@link java.util.concurrent.Executor} since it's the implementation details of our framework.
 *
 * @author Jekton
 */
public class NetworkOperators {

    /**
     * @return a {@link NetworkOperator} that can just handle {@link okhttp3.Request} exactly one
     * instance at a given time
     */
    public static NetworkOperator newSingleRequestOperator() {
        return new SingleRequestOperator();
    }

    public static NetworkOperator newMultiRequestOperator() {
        return new MultiRequestOperator(new HashMap<Object, HttpRunnable>());
    }

    private NetworkOperators() {
        throw new AssertionError("DON'T instantiate this class");
    }
}
