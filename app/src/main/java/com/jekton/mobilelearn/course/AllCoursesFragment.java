package com.jekton.mobilelearn.course;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * @author Jekton
 */
public class AllCoursesFragment extends CourseListFragment {

    @Override
    protected void initCourseList() {
        getMainActivity().getDocument().onGettingAllCourses();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = CourseDetailsActivity.makeIntent(getActivity(), getCourses().get(position));
        startActivity(intent);
    }
}
