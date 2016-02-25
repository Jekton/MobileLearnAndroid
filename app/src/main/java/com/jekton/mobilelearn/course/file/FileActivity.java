package com.jekton.mobilelearn.course.file;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.jekton.mobilelearn.course.CourseLearningActivity;

import java.util.List;

/**
 * @author Jekton
 */
public class FileActivity extends AppCompatActivity implements FileActivityOps {

    private static final String INTENT_EXTRA_COURSE_ID = "INTENT_EXTRA_COURSE_ID";


    private String mCourseId;




    private void initCourseId() {
        Intent intent = getIntent();
        mCourseId = intent.getStringExtra(INTENT_EXTRA_COURSE_ID);
        if (mCourseId == null) {
            throw new IllegalStateException("Intent used to start this activity must contains a key "
                                                    + INTENT_EXTRA_COURSE_ID);
        }
    }

    public static Intent makeIntent(Activity activity, String courseId) {
        Intent intent = new Intent(activity, CourseLearningActivity.class);
        intent.putExtra(INTENT_EXTRA_COURSE_ID, courseId);
        return intent;
    }

    @Override
    public void onFilesChange(@NonNull List<CourseFile> courseFiles) {

    }

    @Override
    public void onGetFileListFail() {

    }

    @Override
    public void onNetworkError() {

    }
}
