package com.jekton.mobilelearn.course.file;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.jekton.mobilelearn.common.util.Logger;
import com.jekton.mobilelearn.course.Course;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        File courseDir = getCourseDir(course);
        if (!courseDir.exists()) {
            if (!courseDir.mkdirs()) {
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


    public static void writeToPath(InputStream in, String writeTo) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(writeTo));

            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public static File getCourseDir(Course course) {
        File appRoot = Environment.getExternalStorageDirectory();
        return new File(appRoot.getAbsolutePath()
                                + "/com.jekton.MobileLearn/file/"
                                + course.name.replace(" ", "_"));
    }

    public static String makeLocalPath(Course course, CourseFile file) {
        return getCourseDir(course).getAbsolutePath() + "/" + getStoredFileName(file.path);
    }

    private FileUtil() {
    }
}
