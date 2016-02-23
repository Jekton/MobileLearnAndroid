package com.jekton.mobilelearn.course;

import com.jekton.mobilelearn.common.dv.BasicDocumentOps;

/**
 * @author Jekton
 */
interface CourseLearningDocumentOps extends BasicDocumentOps<CourseLearningViewOps> {

    void getCourseInfo(String courseId);

}
