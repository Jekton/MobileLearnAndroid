package com.jekton.mobilelearn.course.file;

import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import com.jekton.mobilelearn.R;

/**
 * @author Jekton
 */
public class CourseFile {

    @IntDef({STATE_NOT_DOWNLOAD, STATE_DOWNLOADING, STATE_DOWNLOADED})
    public @interface FileState {}

    public static final int STATE_NOT_DOWNLOAD = 0;
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_DOWNLOADED = 2;

    public @FileState int state;
    public String filename;
    public String path;


    public @StringRes int getStateStringRes() {
        switch (state) {
            case STATE_NOT_DOWNLOAD:
                return R.string.file_state_not_download;
            case STATE_DOWNLOADING:
                return R.string.file_state_downloading;
            case STATE_DOWNLOADED:
                return R.string.file_state_downloaded;
            default:
                throw new AssertionError("unknown state " + state);
        }
    }
}
