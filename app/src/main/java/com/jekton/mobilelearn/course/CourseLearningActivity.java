package com.jekton.mobilelearn.course;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;

/**
 * @author Jekton
 */
public class CourseLearningActivity
        extends DialogEnabledActivity<CourseLearningViewOps, CourseLearningDocumentOps>
        implements CourseLearningViewOps {

    private static final String INTENT_EXTRA_COURSE_ID = "INTENT_EXTRA_COURSE_ID";

    private Course mCourse;


    private void fillCourseInfo() {

    }

    @Override
    public void onCourseInfoChanged(@NonNull final Course course) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCourse = course;
                fillCourseInfo();
            }
        });
    }

    @Override
    public void onGetCourseFail() {
        showToastAndDismissDialog(R.string.msg_fail_get_course_info);
    }

    @Override
    public void onNetworkFail() {
        showToastAndDismissDialog(R.string.err_network_error);
    }

    public static Intent makeIntent(Activity activity, String courseId) {
        Intent intent = new Intent(activity, CourseLearningActivity.class);
        intent.putExtra(INTENT_EXTRA_COURSE_ID, courseId);
        return intent;
    }
}
