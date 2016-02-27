package com.jekton.mobilelearn;

import android.support.v7.app.AppCompatActivity;

import com.jekton.mobilelearn.common.dv.BasicDocumentOps;
import com.jekton.mobilelearn.common.dv.GenericActivity;
import com.jekton.mobilelearn.common.util.Logger;

/**
 * Base class that provide some basic implementation to initialize the Document-View framework.
 *
 * DocumentOps is expected to contain a default constructor.
 *
 * Subclass must call {@link #onCreateDocument(Object, Class)} to execute the initialization logic.
 *
 * If the instance of DocumentOps is used to compute some long-running computation such as
 * networking, the subclass must also call the {@link #onDestroyDocument()} to stop the document.
 *
 * @author Jekton
 */
public abstract class GenericActivityExample<
        ViewOps,
        DocumentOps extends BasicDocumentOps<ViewOps>>
        extends AppCompatActivity {

    private static final String LOG_TAG = GenericActivity.class.getSimpleName();

    private DocumentOps mDocument;

    protected void
    onCreateDocument(ViewOps view,
                     Class<? extends DocumentOps> documentOpsClass) {
        initDocument(documentOpsClass);
        mDocument.setView(view);
    }

    private void initDocument(Class<? extends DocumentOps> documentOpsClass) {
        try {
            mDocument = documentOpsClass.newInstance();
            mDocument.onCreate();
        } catch (InstantiationException e) {
            Logger.e(LOG_TAG, e);
        } catch (IllegalAccessException e) {
            Logger.e(LOG_TAG, e);
        }
    }

    public DocumentOps getDocument() {
        return mDocument;
    }

    protected void onDestroyDocument() {
        mDocument.onDestroy();
        mDocument = null;
    }
}
