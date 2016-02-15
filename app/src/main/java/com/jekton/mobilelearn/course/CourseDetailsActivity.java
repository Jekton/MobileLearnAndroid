package com.jekton.mobilelearn.course;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpActivity;

/**
 * @author Jekton
 */
public class CourseDetailsActivity
        extends SimpleHttpActivity<CourseDetailsViewOps, CourseDetailsDocument>
        implements CourseDetailsViewOps {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDocument(this, CourseDetailsDocument.class);
    }

    @Override
    public void onCourseTaken() {
        showToastAndDismissDialog(R.string.msg_take_course_success);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO disable the "take course" button
            }
        });
    }

    @Override
    public void onPostActionFail() {
        showToastAndDismissDialog(R.string.msg_take_course_fail);
    }

}
