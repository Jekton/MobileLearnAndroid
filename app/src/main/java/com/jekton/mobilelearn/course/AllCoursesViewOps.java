package com.jekton.mobilelearn.course;

import com.jekton.mobilelearn.common.dv.network.OnDocumentFail;

import java.util.List;

/**
 * @author Jekton
 */
interface AllCoursesViewOps extends OnDocumentFail {

    void onCoursesChange(List<Course> courses);

}
