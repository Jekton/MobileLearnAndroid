package com.jekton.mobilelearn.common.dv;

import android.support.annotation.CallSuper;
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
    private WeakReference<ViewOps> mReference;
    private volatile boolean isDestroyed;


    @Override
    @CallSuper
    public void onDestroy() {
        isDestroyed = true;
    }

    @Override
    public void setView(ViewOps view) {
        mReference = new WeakReference<>(view);
    }

    public @Nullable ViewOps getView() {
        return mReference.get();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
