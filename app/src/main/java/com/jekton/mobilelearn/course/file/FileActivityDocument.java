package com.jekton.mobilelearn.course.file;

import android.content.Intent;

import com.google.gson.Gson;
import com.jekton.mobilelearn.common.dv.AbstractDocument;
import com.jekton.mobilelearn.common.network.HttpUtils;
import com.jekton.mobilelearn.common.network.operator.NetworkOperator;
import com.jekton.mobilelearn.common.network.operator.NetworkOperators;
import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;
import com.jekton.mobilelearn.course.Course;
import com.jekton.mobilelearn.network.UrlConstants;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jekton
 */
class FileActivityDocument extends AbstractDocument<FileActivityOps>
        implements FileActivityDocumentOps {

    private static final String LOG_TAG = FileActivityDocument.class.getSimpleName();

    private NetworkOperator mNetworkOperator = NetworkOperators.getSingleRequestOperator();
    private AtomicReference<String> mCourseId;
    private AtomicReference<Course> mCourse;
    private AtomicReference<List<CourseFile>> mCourseFiles;
    private volatile boolean mHasDownloadingSet;


    public FileActivityDocument() {
        mCourseId = new AtomicReference<>();
        mCourse = new AtomicReference<>();
        mCourseFiles = new AtomicReference<>();
    }

    @Override
    public void initFileList(String courseId) {
        mCourseId.set(courseId);
        String url = String.format(UrlConstants.GET_TAKEN_COURSE_TEMPLATE, courseId);
        Request request = HttpUtils.makeGetRequest(url);
        mNetworkOperator.executeRequest(request, new OnResponseCallback() {
            @Override
            public void onResponseSuccess(Response response) {
                FileActivityOps view = getView();
                if (view != null) {
                    try {
                        Gson gson = new Gson();
                        Course course = gson.fromJson(response.body().string(), Course.class);
                        mCourse.set(course);

                        List<CourseFile> courseFiles = FileUtil.makeFileList(course);
                        if (courseFiles == null) {
                            view.onLocalFileSystemError();
                        } else {
                            setDownloadingOrNot(courseFiles);
                            mCourseFiles.set(courseFiles);
                            view.onFilesChange(courseFiles);
                        }
                    } catch (IOException e) {
                        onResponseFail(response);
                        Logger.e(LOG_TAG, e);
                    }
                }
            }

            @Override
            public void onNetworkFail() {
                FileActivityOps view = getView();
                if (view != null) {
                    view.onNetworkError();
                }
            }

            @Override
            public void onResponseFail(Response response) {
                FileActivityOps view = getView();
                if (view != null) {
                    view.onGetFileListFail();
                }
            }
        });

    }

    @Override
    public void onStateChange(String path, int percent) {
        Logger.d(LOG_TAG, "path = " + path + ", percent = " + percent);
        List<CourseFile> courseFiles = mCourseFiles.get();
        for (CourseFile file : courseFiles) {
            if (file.path.equals(path)) {
                if (percent == 100) {
                    file.state = CourseFile.STATE_DOWNLOADED;
                } else if (percent == -1) {
                    file.state = CourseFile.STATE_NOT_DOWNLOAD;
                } else {
                    file.state = CourseFile.STATE_DOWNLOADING;
                    file.downloadProgress = percent;
                }

                notifyFileListChanged();
                return;
            }
        }
    }

    private void notifyFileListChanged() {
        FileActivityOps view = getView();
        if (view != null) {
            view.onFilesChange(mCourseFiles.get());
        }
    }

    @Override
    public void setDownloadingSetAvailable(boolean available) {
        mHasDownloadingSet = available;
    }

    private void setDownloadingOrNot(List<CourseFile> courseFiles) {
        if (!mHasDownloadingSet) {
            return;
        }

        FileActivityOps view = getView();
        if (view != null) {
            Set<String> downloading = view.getDownloadingSet();
            for (CourseFile file : courseFiles) {
                if (downloading.contains(file.path)) {
                    file.state = CourseFile.STATE_DOWNLOADING;
                }
            }
        }
    }

    @Override
    public void performActionFor(CourseFile courseFile) {
        Logger.d(LOG_TAG, "file state: " + courseFile.state);
        switch (courseFile.state) {
            case CourseFile.STATE_NOT_DOWNLOAD:
                downFile(courseFile);
            case CourseFile.STATE_DOWNLOADING:
                // no-op
            case CourseFile.STATE_DOWNLOADED:
                openFile(courseFile);
            default:
                // should not happen
                // no-op
        }
    }

    private void downFile(CourseFile file) {
        FileActivityOps view = getView();
        if (view != null) {
            Intent downFileIntent = FileDownloadService.makeDownloadIntent(
                    view.getContext(),
                    file.path,
                    FileUtil.makeLocalPath(mCourse.get(), file)
            );
            view.getContext().startService(downFileIntent);
            file.state = CourseFile.STATE_DOWNLOADING;
            notifyFileListChanged();
        }
    }

    private void openFile(CourseFile file) {
        // TODO: 2/25/2016 open file using external apps
    }
}
