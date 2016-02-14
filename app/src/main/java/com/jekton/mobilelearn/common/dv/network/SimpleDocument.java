package com.jekton.mobilelearn.common.dv.network;

import android.os.AsyncTask;

import com.jekton.mobilelearn.common.dv.AbstractDocument;
import com.jekton.mobilelearn.network.AbstractHttpRunnable;
import com.jekton.mobilelearn.network.OnResponseCallback;

import okhttp3.Response;

/**
 * @author Jekton
 */
public abstract class SimpleDocument<ViewOps extends OnDocumentFail>
        extends AbstractDocument<ViewOps>
        implements OnResponseCallback {

    private volatile AbstractHttpRunnable mHttpRunnable;

    protected void doPost(AbstractHttpRunnable runnable) {
        mHttpRunnable = runnable;

        AsyncTask.THREAD_POOL_EXECUTOR.execute(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHttpRunnable.cancel();
    }


    @Override
    public void onNetworkFail() {
        ViewOps view = getView();
        if (view != null) {
            view.onNetworkFail();
        }
    }

    @Override
    public void onResponseFail(Response response) {
        ViewOps view = getView();
        if (view != null) {
            view.onPostActionFail();
        }
    }

}
