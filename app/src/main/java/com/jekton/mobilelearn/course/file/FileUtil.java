package com.jekton.mobilelearn.course.file;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.jekton.mobilelearn.common.util.Logger;
import com.jekton.mobilelearn.course.Course;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jekton
 */
class FileUtil {

    private static final String LOG_TAG = FileUtil.class.getSimpleName();

    /**
     * Using the local file system to check whether the file downloaded and make the corresponding
     * file list.
     *
     * @param course course that the file list belongs to
     * @return course file list according to the local file system state
     */
    public static synchronized @Nullable List<CourseFile> makeFileList(Course course) {
        List<CourseFile> courseFiles = new ArrayList<>();

        File appRoot = Environment.getExternalStorageDirectory();
        File courseDir = new File(appRoot.getAbsolutePath() + "/" + course.name);
        if (!courseDir.exists()) {
            if (!courseDir.mkdir()) {
                Logger.e(LOG_TAG, "Fail to create directory: " + courseDir.getAbsolutePath());
                return null;
            }
        }

        Set<String> fileSet = new HashSet<>();
        for (File file : courseDir.listFiles()) {
            fileSet.add(file.getName());
        }

        for (Course.Path path : course.files) {
            // STATE_DOWNLOADING is concerned late
            @CourseFile.FileState int state = CourseFile.STATE_NOT_DOWNLOAD;
            if (fileSet.contains(getStoredFileName(path.path))) {
                state = CourseFile.STATE_DOWNLOADED;
            }
            courseFiles.add(new CourseFile(path.filename, path.path, state));
        }

        return courseFiles;
    }


    private static String getStoredFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private FileUtil() {
    }
}
