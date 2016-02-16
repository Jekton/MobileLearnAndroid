package com.jekton.mobilelearn.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpActivity;
import com.jekton.mobilelearn.course.widget.AllCourseListAdapter;

import java.util.List;

/**
 * @author Jekton
 */
public class AllCoursesActivity
        extends SimpleHttpActivity<AllCoursesViewOps, AllCoursesDocument>
        implements AllCoursesViewOps, AdapterView.OnItemClickListener {

    private static final String LOG_TAG = AllCoursesActivity.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AllCourseListAdapter mCourseListAdapter;
    private List<Course> mCourses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mCourseListAdapter = new AllCourseListAdapter(this);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mCourseListAdapter);
        listView.setOnItemClickListener(this);

        super.onCreateDocument(this, AllCoursesDocument.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        showDialog();
        getDocument().onGettingAllCourses();
    }


    private void doOnCourseChange(List<Course> courses) {
        mCourses = courses;
        mCourseListAdapter.updateCourseList(courses);
        closeDialog();
    }

    @Override
    public void onCoursesChange(final List<Course> courses) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doOnCourseChange(courses);
            }
        });
    }

    @Override
    public void onPostActionFail() {
        showToastAndDismissDialog(R.string.msg_fail_to_get_course_list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = CourseDetailsActivity.makeIntent(this, mCourses.get(position));
        startActivity(intent);
    }
}
