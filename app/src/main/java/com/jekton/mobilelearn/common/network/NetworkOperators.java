package com.jekton.mobilelearn.common.network;

import android.os.AsyncTask;

/**
 * @author Jekton
 *
 * Note: We DO NOT want to provide static factory methods that takes an instance of
 * {@link java.util.concurrent.Executor} since it's the implementation details of our framework.
 */
public class NetworkOperators {

    /**
     * @return a {@link NetworkOperator} that can just handle {@link okhttp3.Request} exactly one
     * instance at a given time
     */
    public static NetworkOperator newSingleRequestOperator() {
        return new SingleRequestOperator(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static NetworkOperator newMultiRequestOperator() {
        return null;
    }

    private NetworkOperators() {
        throw new AssertionError("DON'T instantiate this class");
    }
}
