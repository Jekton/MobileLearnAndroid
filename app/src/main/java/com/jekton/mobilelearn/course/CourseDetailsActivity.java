package com.jekton.mobilelearn.course;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpActivity;
import com.jekton.mobilelearn.common.util.Toaster;
import com.jekton.mobilelearn.network.UrlConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Jekton
 */
public class CourseDetailsActivity
        extends SimpleHttpActivity<CourseDetailsViewOps, CourseDetailsDocument>
        implements CourseDetailsViewOps, View.OnClickListener {

    private static final String INTENT_EXTRA_COURSE = "CourseDetailsActivity.INTENT_EXTRA_COURSE";

    private Course mCourse;
    private Button mButton;

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

        ImageLoader.getInstance().displayImage(UrlConstants.HOST + mCourse.iconPath,
                                               (ImageView) findViewById(R.id.icon));

        TextView textView;
        Resources resources = getResources();

        textView = (TextView) findViewById(R.id.name);
        textView.setText(resources.getString(R.string.temp_course_name, mCourse.name));

        textView = (TextView) findViewById(R.id.desc);
        textView.setText(resources.getString(R.string.temp_course_desc, mCourse.desc));

        textView = (TextView) findViewById(R.id.creator);
        textView.setText(resources.getString(R.string.temp_course_creator, mCourse.createdBy));

        textView = (TextView) findViewById(R.id.nr_lecture);
        textView.setText(resources.getString(R.string.temp_lecture_num, mCourse.lectureNum));

        mButton = (Button) findViewById(R.id.take_course);
        mButton.setOnClickListener(this);
        if (mCourse.taken) {
            mButton.setText(R.string.btn_goto_course);
        }
    }


    @Override
    public void onCourseTaken() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mButton.setText(R.string.btn_goto_course);
                Toaster.showShort(CourseDetailsActivity.this, R.string.msg_success_take_course);
                closeDialog();
                mCourse.taken = true;
            }
        });
    }

    @Override
    public void onPostActionFail() {
        showToastAndDismissDialog(R.string.msg_fail_take_course);
    }



    public static Intent makeIntent(Context context, Course course) {
        Intent intent = new Intent(context, CourseDetailsActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(course);
        intent.putExtra(INTENT_EXTRA_COURSE, json);

        return intent;
    }

    @Override
    public void onClick(View v) {
        if (mCourse.taken) {
            // TODO: 2/16/2016  goto the course activity
        } else {
            showDialog();
            getDocument().onTakeCourse(mCourse._id);
        }
    }
}
