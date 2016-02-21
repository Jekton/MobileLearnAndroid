package com.jekton.mobilelearn.course;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.course.widget.AllCourseListAdapter;

import java.util.List;

/**
 * @author Jekton
 */
public class AllCoursesFragment extends Fragment
        implements AdapterView.OnItemClickListener, MainActivity.CourseListOps {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AllCourseListAdapter mCourseListAdapter;
    private List<Course> mCourses;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_courses, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        mCourseListAdapter = new AllCourseListAdapter(getActivity());
        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(mCourseListAdapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) getActivity();
        activity.showDialog();
        activity.getDocument().onGettingAllCourses();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = CourseDetailsActivity.makeIntent(getActivity(), mCourses.get(position));
        startActivity(intent);
    }

    @Override
    public void onCoursesChange(List<Course> courses) {
        mCourses = courses;
        mCourseListAdapter.updateCourseList(courses);
    }
}
