package com.jekton.mobilelearn.course.file;

import android.support.annotation.IntDef;

/**
 * @author Jekton
 */
public class CourseFile {

    @IntDef({STATE_NOT_DOWNLOAD, STATE_DOWNLOADING, STATE_DOWNLOADED})
    public @interface FileState {}

    public static final int STATE_NOT_DOWNLOAD = 0;
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_DOWNLOADED = 2;

    public CourseFile(String filename, String path, @FileState int state) {
        this.filename = filename;
        this.path = path;
        this.state = state;
    }

    public String filename;
    public String path;
    public @FileState int state;
    public int downloadProgress;  // only used when STATE_DOWNLOADING
}
