package com.jekton.mobilelearn.course;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpActivity;
import com.jekton.mobilelearn.common.util.Logger;

import java.util.List;

/**
 * @author Jekton
 */
public class AllCoursesActivity
        extends SimpleHttpActivity<AllCoursesViewOps, AllCoursesDocument>
        implements AllCoursesViewOps {

    private static final String LOG_TAG = AllCoursesActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);
        super.onCreateDocument(this, AllCoursesDocument.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        showDialog();
        getDocument().onGettingAllCourses();
    }


    @Override
    public void onCoursesChange(List<Course> courses) {
        for (Course course : courses) {
            Logger.d(LOG_TAG, course.toString());
        }
    }

    @Override
    public void onPostActionFail() {
        showToastAndDismissDialog(R.string.msg_fail_to_get_course_list);
    }

    @Override
    public void onNetworkFail() {
        showToastAndDismissDialog(R.string.err_network_error);
    }
}
