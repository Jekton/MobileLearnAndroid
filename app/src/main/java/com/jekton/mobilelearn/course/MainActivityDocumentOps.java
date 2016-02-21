package com.jekton.mobilelearn.course;

import com.jekton.mobilelearn.common.dv.BasicDocumentOps;

/**
 * @author Jekton
 */
interface MainActivityDocumentOps extends BasicDocumentOps<MainActivityOps> {

    void onGettingAllCourses();

    void onGettingMyCourses();

}
