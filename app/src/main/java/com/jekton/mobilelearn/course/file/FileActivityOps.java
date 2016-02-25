package com.jekton.mobilelearn.course.file;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author Jekton
 */
interface FileActivityOps {

    void onFilesChange(@NonNull List<CourseFile> courseFiles);

    void onGetFileListFail();

    void onNetworkError();

}
