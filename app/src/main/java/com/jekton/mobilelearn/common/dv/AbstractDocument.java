package com.jekton.mobilelearn.common.dv;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * @author Jekton
 */
public abstract class AbstractDocument<ViewOps> implements BasicDocumentOps<ViewOps> {

    /**
     * Since we can't rely the client to call us back while the Activity is automatically
     * being destroyed and re-constructed, we must use a WeakReference here.
     */
    private volatile WeakReference<ViewOps> mReference;

    @Override
    public void onCreate() {
        // no-op
        // since this is the later added method, add this default implementation to avoid damage
        // the client
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public void setView(ViewOps view) {
        mReference = new WeakReference<>(view);
    }

    public @Nullable ViewOps getView() {
        if (mReference == null) {
            return null;
        }
        return mReference.get();
    }

}
