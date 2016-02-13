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

    private RetainedFragment<DocumentOps> mRetainedFragment;
    private DocumentOps mDocument;


    /**
     * Subclass must call this method to initial this light-weight Document-View framework
     * @param documentOpsClass class object of the DocumentOps
     * @param view "View" object of the Document-View framework
     */
    protected void onCreateDocument(ViewOps view, Class<? extends DocumentOps> documentOpsClass) {
        FragmentManager manager = getFragmentManager();

        // The found fragment is exactly with type RetainedFragment<DocumentOps>
        @SuppressWarnings("unchecked")
        RetainedFragment<DocumentOps> fragment =
                (RetainedFragment<DocumentOps>) manager.findFragmentByTag(RETAINED_FRAGMENT_TAG);
        mRetainedFragment = fragment;

        if (mRetainedFragment == null) {
            initRetainedFragment(manager);
            initDocument(documentOpsClass);
        } else {
            mDocument = mRetainedFragment.getDocument();
        }
        mDocument.setView(view);
    }

    private void initRetainedFragment(FragmentManager manager) {
        mRetainedFragment = new RetainedFragment<>();
        mRetainedFragment.setRetainInstance(true);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(mRetainedFragment, RETAINED_FRAGMENT_TAG);
        transaction.commit();
    }

    private void initDocument(Class<? extends DocumentOps> documentOpsClass) {
        try {
            mDocument = documentOpsClass.newInstance();
        } catch (InstantiationException e) {
            Logger.e(LOG_TAG, e);
        } catch (IllegalAccessException e) {
            Logger.e(LOG_TAG, e);
        }
        mRetainedFragment.setDocument(mDocument);
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


}
