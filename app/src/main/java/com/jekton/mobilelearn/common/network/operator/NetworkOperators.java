package com.jekton.mobilelearn.common.network.operator;

import java.util.HashMap;

/**
 * Util class that used to provide some static factory method to generate instance of
 * {@link NetworkOperator}.
 *
 * For common use case, client should use {@link #getSingleRequestOperator()} and
 * {@link #getMultiRequestOperator()} to used a app-shared instance.
 *
 * Note: We DO NOT want to provide static factory methods that takes an instance of
 * {@link java.util.concurrent.Executor} since it's the implementation details of our framework.
 *
 * @author Jekton
 */
public class NetworkOperators {

    private static class SingleRequestSingleton {
        public static final NetworkOperator sNetworkOperator = newSingleRequestOperator();
    }

    private static class MultiRequestSingleton {
        public static final NetworkOperator sNetworkOperator = newMultiRequestOperator();
    }


    public static NetworkOperator getSingleRequestOperator() {
        return SingleRequestSingleton.sNetworkOperator;
    }

    public static NetworkOperator getMultiRequestOperator() {
        return MultiRequestSingleton.sNetworkOperator;
    }


    /**
     * @return a {@link NetworkOperator} that can just handle {@link okhttp3.Request} exactly one
     * instance at a given time
     */
    public static NetworkOperatorService newSingleRequestOperator() {
        return new SingleRequestOperator();
    }

    public static NetworkOperatorService newMultiRequestOperator() {
        return new MultiRequestOperator(new HashMap<Object, HttpRunnable>());
    }

    private NetworkOperators() {
        throw new AssertionError("DON'T instantiate this class");
    }
}
