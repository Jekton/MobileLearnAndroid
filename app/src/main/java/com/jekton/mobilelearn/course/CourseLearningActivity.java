package com.jekton.mobilelearn.course;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;
import com.jekton.mobilelearn.course.widget.LectureListAdapter;
import com.jekton.mobilelearn.network.UrlConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Jekton
 */
public class CourseLearningActivity
        extends DialogEnabledActivity<CourseLearningViewOps, CourseLearningDocumentOps>
        implements CourseLearningViewOps, AdapterView.OnItemClickListener {

    private static final String INTENT_EXTRA_COURSE_ID = "INTENT_EXTRA_COURSE_ID";

    private ImageView mIcon;
    private TextView mCourseName;
    private TextView mCourseDesc;
    private TextView mLectureListTitle;
    private LectureListAdapter mLectureListAdapter;

    private String mCourseId;
    private Course mCourse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDocument(this, CourseLearningDocument.class);
        setContentView(R.layout.activity_course_learning);

        initView();
    }

    private void initCourseId() {
        Intent intent = getIntent();
        mCourseId = intent.getStringExtra(INTENT_EXTRA_COURSE_ID);
        if (mCourseId == null) {
            throw new IllegalStateException("Intent used to start this activity must contains a key "
                                                    + INTENT_EXTRA_COURSE_ID);
        }
    }

    private void initView() {
        initActionBar();
        initCourseId();

        mIcon = (ImageView) findViewById(R.id.icon);
        mCourseName = (TextView) findViewById(R.id.name);
        mCourseDesc = (TextView) findViewById(R.id.desc);
        mLectureListTitle = (TextView) findViewById(R.id.lecture_list_title);
        ListView listView = (ListView) findViewById(R.id.lecture_list);
        mLectureListAdapter = new LectureListAdapter(this);
        listView.setAdapter(mLectureListAdapter);
        listView.setOnItemClickListener(this);

        showDialog();
        getDocument().getCourseInfo(mCourseId);
    }

       private void initActionBar() {
           Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           setSupportActionBar(toolbar);
       }

    private void fillCourseInfo() {
        ImageLoader.getInstance().displayImage(UrlConstants.HOST + mCourse.iconPath,
                                               mIcon);

        Resources resources = getResources();
        mCourseName.setText(resources.getString(R.string.temp_course_name, mCourse.name));
        mCourseDesc.setText(resources.getString(R.string.temp_course_desc, mCourse.desc));
        mLectureListAdapter.onCourseChange(mCourse);

        if (mCourse.lectureNum == 0) {
            mLectureListTitle.setText(R.string.no_lecture);
        } else {
            mLectureListTitle.setText(R.string.lectures);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_course_learning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.view_files) {
            // TODO: 2/25/2016
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCourseInfoChanged(@NonNull final Course course) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCourse = course;
                closeDialog();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = UrlConstants.HOST + mCourse.lectures[position].path;
        startActivity(VideoActivity.makeIntent(this, url));
    }
}
