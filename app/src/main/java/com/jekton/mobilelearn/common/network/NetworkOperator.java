package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;

import okhttp3.Request;

/**
 * @author Jekton
 */
public interface NetworkOperator {

    void executeRequest(@NonNull Request request,
                        @NonNull OnResponseCallback callback);

    /**
     * execute a {@link Request} in some background thread and associate it with a key
     * @param key the key that can be used to cancel this request
     * @param request request that to be executed
     * @param callback callback of this request
     */
    void executeRequest(@NonNull Object key,
                        @NonNull Request request,
                        @NonNull OnResponseCallback callback);

    void cancelRequest(@NonNull Object key);

    void cancelRequests();

    void shutdown();
}
