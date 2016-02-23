package com.jekton.mobilelearn.course;

import android.support.annotation.NonNull;

/**
 * @author Jekton
 */
interface CourseLearningViewOps {

    void onCourseInfoChanged(@NonNull Course course);

    void onGetCourseFail();

    void onNetworkFail();
}
