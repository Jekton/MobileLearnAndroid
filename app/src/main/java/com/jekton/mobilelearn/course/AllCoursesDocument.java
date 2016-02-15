package com.jekton.mobilelearn.course;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpDocument;
import com.jekton.mobilelearn.common.network.AbstractHttpRunnable;
import com.jekton.mobilelearn.common.network.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;
import com.jekton.mobilelearn.network.UrlConstants;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jekton
 */
public class AllCoursesDocument
        extends SimpleHttpDocument<AllCoursesViewOps>
        implements AllCoursesDocumentOps {

    private static final String LOG_TAG = AllCoursesDocument.class.getSimpleName();


    @Override
    public void onGettingAllCourses() {
        doHttpAction(new GetAllCourseRunnable(this));
    }

    @Override
    public void onResponseSuccess(Response response) {

        try {
            Gson gson = new Gson();
            Course[] courses = gson.fromJson(response.body().string(), Course[].class);
            AllCoursesViewOps view = getView();
            if (view != null) {
                view.onCoursesChange(Arrays.asList(courses));
            }
        } catch (IOException e) {
            Logger.e(LOG_TAG, e);
            onResponseFail(response);
        }
    }


    private class GetAllCourseRunnable extends AbstractHttpRunnable {

        public GetAllCourseRunnable(OnResponseCallback callback) {
            super(callback);
        }

        @NonNull
        @Override
        protected Request makeRequest() {
            return new Request.Builder()
                    .url(UrlConstants.GET_ALL_COURSES)
                    .get()
                    .build();
        }
    }
}
