package com.jekton.mobilelearn.course;

import com.google.gson.Gson;
import com.jekton.mobilelearn.common.dv.AbstractDocument;
import com.jekton.mobilelearn.common.network.HttpUtils;
import com.jekton.mobilelearn.common.network.operator.NetworkOperator;
import com.jekton.mobilelearn.common.network.operator.NetworkOperators;
import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;
import com.jekton.mobilelearn.network.UrlConstants;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jekton
 */
public class CourseLearningDocument extends AbstractDocument<CourseLearningViewOps>
        implements CourseLearningDocumentOps {

    private static final String LOG_TAG = CourseLearningDocument.class.getSimpleName();

    private NetworkOperator mNetworkOperator = NetworkOperators.getSingleRequestOperator();

    @Override
    public void getCourseInfo(String courseId) {
        Request request = HttpUtils.makeGetRequest(
                String.format(UrlConstants.GET_TAKEN_COURSE_TEMPLATE, courseId));
        mNetworkOperator.executeRequest(request, new OnResponseCallback() {
            @Override
            public void onResponseSuccess(Response response) {
                CourseLearningViewOps view = getView();
                if (view != null) {
                    try {
                        Gson gson = new Gson();
                        Course course = gson.fromJson(response.body().string(), Course.class);
                        view.onCourseInfoChanged(course);
                    } catch (IOException e) {
                        Logger.e(LOG_TAG, e);
                    }
                }
            }

            @Override
            public void onNetworkFail() {
                CourseLearningViewOps view = getView();
                if (view != null) {
                    view.onNetworkFail();
                }
            }

            @Override
            public void onResponseFail(Response response) {
                CourseLearningViewOps view = getView();
                if (view != null) {
                    view.onGetCourseFail();
                }
            }
        });
    }
}
