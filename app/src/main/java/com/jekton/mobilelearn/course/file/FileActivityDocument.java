package com.jekton.mobilelearn.course.file;

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

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jekton
 */
class FileActivityDocument extends AbstractDocument<FileActivityOps>
        implements FileActivityDocumentOps {

    private static final String LOG_TAG = FileActivityDocument.class.getSimpleName();

    private NetworkOperator mNetworkOperator = NetworkOperators.getSingleRequestOperator();
    private volatile String mCourseId;



    @Override
    public void initFileList(String courseId) {
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
                        view.onFilesChange(FileUtil.makeFileList(course));
                    } catch (IOException e) {
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
    public void performActionFor(CourseFile courseFile) {

    }
}
