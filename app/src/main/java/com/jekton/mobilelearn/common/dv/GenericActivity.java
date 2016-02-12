package com.jekton.mobilelearn.common.dv;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jekton.mobilelearn.common.util.Logger;

/**
 * @author Jekton
 */
public abstract class GenericActivity<ViewOps, DocumentOps extends BasicDocumentOps<ViewOps>>
        extends AppCompatActivity {

    private static final String LOG_TAG = GenericActivity.class.getSimpleName();

    private static final String RETAINED_FRAGMENT_TAG = "RETAINED_FRAGMENT_TAG";
    private static final String DOCUMENT_KEY = "DOCUMENT_KEY";

    private RetainedFragment mRetainedFragment;
    private DocumentOps mDocument;


    /**
     * Subclass must call this method to initial this light-weight Document-View framework
     * @param documentOpsClass class object of the DocumentOps
     * @param view "View" object of the Document-View framework
     */
    protected void onCreate(Class<DocumentOps> documentOpsClass, ViewOps view) {
        FragmentManager manager = getFragmentManager();
        mRetainedFragment = (RetainedFragment) manager.findFragmentByTag(RETAINED_FRAGMENT_TAG);
        if (mRetainedFragment == null) {
            initRetainedFragment(manager);
            initDocument(documentOpsClass);
        } else {
            mDocument = mRetainedFragment.getRetainedObject(DOCUMENT_KEY);
        }
        mDocument.setView(view);
    }

    private void initRetainedFragment(FragmentManager manager) {
        mRetainedFragment = new RetainedFragment();
        mRetainedFragment.setRetainInstance(true);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(mRetainedFragment, RETAINED_FRAGMENT_TAG);
        transaction.commit();
    }

    private void initDocument(Class<DocumentOps> documentOpsClass) {
        try {
            mDocument = documentOpsClass.newInstance();
        } catch (InstantiationException e) {
            Logger.e(LOG_TAG, e);
        } catch (IllegalAccessException e) {
            Logger.e(LOG_TAG, e);
        }
        mRetainedFragment.putRetainedObject(DOCUMENT_KEY, mDocument);
    }


    protected DocumentOps getDocument() {
        return mDocument;
    }

    /**
     * If client has used the "document" to computing some long running computation,
     * the subclass must manually call the method back in order to stop the computation.
     */
    protected void onDestroyDocument() {
        mDocument.onDestroy();
        mDocument = null;
    }


    protected <T> T getRetainedObject(String key) {
        return mRetainedFragment.getRetainedObject(key);
    }

    protected void putRetainedObject(Object obj) {
        mRetainedFragment.putRetainedObject(obj);
    }

    protected void putRetainedObject(String key, Object obj) {
        mRetainedFragment.putRetainedObject(key, obj);
    }

}
