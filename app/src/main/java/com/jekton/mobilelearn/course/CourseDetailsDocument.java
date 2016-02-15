package com.jekton.mobilelearn.course;

import android.support.annotation.NonNull;

import com.jekton.mobilelearn.common.dv.network.SimpleHttpDocument;
import com.jekton.mobilelearn.common.network.AbstractHttpRunnable;
import com.jekton.mobilelearn.common.network.OnResponseCallback;
import com.jekton.mobilelearn.network.UrlConstants;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jekton
 */
class CourseDetailsDocument extends SimpleHttpDocument<CourseDetailsViewOps>
        implements CourseDetailsDocumentOps {

    @Override
    public void onTakeCourse(String courseId) {
        doHttpRequest(new TakeCourseRunnable(courseId, this));
    }

    @Override
    public void onResponseSuccess(Response response) {
        CourseDetailsViewOps view = getView();
        if (view != null) {
            view.onCourseTaken();
        }
    }

    private class TakeCourseRunnable extends AbstractHttpRunnable {

        private final String mCourseId;

        public TakeCourseRunnable(String courseId, OnResponseCallback callback) {
            super(callback);
            mCourseId = courseId;
        }

        @NonNull
        @Override
        protected Request makeRequest() {
            return new Request.Builder()
                    .url(String.format(UrlConstants.TAKE_COURSE_TEMPLATE, mCourseId))
                    .build();
        }
    }
}
