package com.jekton.mobilelearn.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpActivity;

/**
 * @author Jekton
 */
public class CourseDetailsActivity
        extends SimpleHttpActivity<CourseDetailsViewOps, CourseDetailsDocument>
        implements CourseDetailsViewOps {

    private static final String INTENT_EXTRA_COURSE = "CourseDetailsActivity.INTENT_EXTRA_COURSE";

    private Course mCourse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        fillCourseInfo();

        super.onCreateDocument(this, CourseDetailsDocument.class);
    }


    private void initCourse() {
        Intent intent = getIntent();
        String json = intent.getStringExtra(INTENT_EXTRA_COURSE);
        if (json == null) {
            throw new AssertionError("Intent that used to start the activity must contain"
                                             + " a extra json string that presents the course");
        }
        Gson gson = new Gson();
        mCourse = gson.fromJson(json, Course.class);
    }

    private void fillCourseInfo() {
        initCourse();

        TextView textView;
        textView = (TextView) findViewById(R.id.name);
        textView.setText(mCourse.name);

        textView = (TextView) findViewById(R.id.desc);
        textView.setText(mCourse.desc);

        textView = (TextView) findViewById(R.id.creator);
        textView.setText(mCourse.createdBy);

        textView = (TextView) findViewById(R.id.nr_lecture);
        textView.setText(mCourse.lectureNum);

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



    public static Intent makeIntent(Context context, Course course) {
        Intent intent = new Intent(context, CourseDetailsActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(course);
        intent.putExtra(INTENT_EXTRA_COURSE, json);

        return intent;
    }
}
