package com.jekton.mobilelearn.common.dv;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

/**
 * @author Jekton
 */
public class RetainedFragment<DocumentOps> extends Fragment {

    private static final String LOG_TAG = RetainedFragment.class.getSimpleName();

    private DocumentOps mDocument;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "RetainedFragment created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "RetainedFragment destroyed");
    }

    public DocumentOps getDocument() {
        return mDocument;
    }

    public void setDocument(DocumentOps document) {
        mDocument = document;
    }
}
