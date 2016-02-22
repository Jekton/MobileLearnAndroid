package com.jekton.mobilelearn.course;

import java.util.List;

/**
 * @author Jekton
 */
interface MainActivityOps {

    void onAllCoursesChange(List<Course> courses);

    void onMyCoursesChange(List<Course> courses);

    void onLogoutSuccess();

    void onGetCoursesFail();

    void onNetworkError();
}
