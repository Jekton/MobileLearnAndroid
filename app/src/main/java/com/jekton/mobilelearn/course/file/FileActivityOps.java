package com.jekton.mobilelearn.course.file;

import android.support.annotation.NonNull;

import com.jekton.mobilelearn.common.dv.ContextView;

import java.util.List;
import java.util.Set;

/**
 * @author Jekton
 */
interface FileActivityOps extends ContextView {

    void onFilesChange(@NonNull List<CourseFile> courseFiles);

    void onGetFileListFail();

    void onNetworkError();

    void onLocalFileSystemError();

    void onOpenFileFail();

    Set<String> getDownloadingSet();
}
