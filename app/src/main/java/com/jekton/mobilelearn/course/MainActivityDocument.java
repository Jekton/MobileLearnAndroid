package com.jekton.mobilelearn.course;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jekton.mobilelearn.common.dv.AbstractDocument;
import com.jekton.mobilelearn.common.network.HttpUtils;
import com.jekton.mobilelearn.common.network.operator.NetworkOperator;
import com.jekton.mobilelearn.common.network.operator.NetworkOperators;
import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;
import com.jekton.mobilelearn.network.UrlConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Response;

/**
 * @author Jekton
 */
class MainActivityDocument extends AbstractDocument<MainActivityOps>
        implements MainActivityDocumentOps {

    public static final String LOG_TAG = MainActivityDocument.class.getSimpleName();

    private static final int REQUEST_ALL_COURSES = 0;
    private static final int REQUEST_MY_COURSES = 1;
    private static final int REQUEST_LOGOUT = 2;

    private NetworkOperator mNetworkOperator = NetworkOperators.getMultiRequestOperator();


    @Override
    public void onGettingAllCourses() {
        mNetworkOperator.cancelRequest(REQUEST_MY_COURSES);
        mNetworkOperator.executeRequest(
                REQUEST_ALL_COURSES,
                HttpUtils.makeGetRequest(UrlConstants.GET_ALL_COURSES),
                new ResponseCallback() {
                    @Override
                    protected void onSuccess(@NonNull MainActivityOps view,
                                             @NonNull List<Course> courses) {
                        view.onAllCoursesChange(courses);
                    }
                }
        );
    }

    @Override
    public void onGettingMyCourses() {
        mNetworkOperator.cancelRequest(REQUEST_ALL_COURSES);
        mNetworkOperator.executeRequest(
                REQUEST_MY_COURSES,
                HttpUtils.makeGetRequest(UrlConstants.GET_TAKEN_COURSES),
                new ResponseCallback() {
                    @Override
                    protected void onSuccess(@NonNull MainActivityOps view,
                                             @NonNull List<Course> courses) {
                        view.onMyCoursesChange(courses);
                    }
                }
        );
    }

    @Override
    public void onLogout() {
        mNetworkOperator.cancelRequest(REQUEST_ALL_COURSES);
        mNetworkOperator.executeRequest(
                REQUEST_LOGOUT,
                HttpUtils.makeGetRequest(UrlConstants.LOGOUT),
                new OnResponseCallback() {
                    @Override
                    public void onResponseSuccess(Response response) {
                        MainActivityOps view = getView();
                        if (view != null) {
                            view.onLogoutSuccess();
                        }
                    }

                    @Override
                    public void onNetworkFail() {
                        MainActivityOps view = getView();
                        if (view != null) {
                            view.onNetworkError();
                        }
                    }

                    @Override
                    public void onResponseFail(Response response) {
                        MainActivityOps view = getView();
                        if (view != null) {
                            view.onGetCoursesFail();
                        }
                    }
                }
        );
    }



    private abstract class ResponseCallback implements OnResponseCallback {
        @Override
        public void onResponseSuccess(Response response) {
            MainActivityOps view = getView();
            if (view != null) {
                try {
                    Gson gson = new Gson();
                    Course[] courses = gson.fromJson(response.body().string(), Course[].class);

                    onSuccess(view, Arrays.asList(courses));
                } catch (IOException e) {
                    Logger.e(LOG_TAG, e);
                    onResponseFail(response);
                }
            }
        }

        protected abstract void onSuccess(@NonNull MainActivityOps view,
                                          @NonNull List<Course> courses);

        @Override
        public void onNetworkFail() {
            MainActivityOps view = getView();
            if (view != null) {
                view.onNetworkError();
            }
        }

        @Override
        public void onResponseFail(Response response) {
            MainActivityOps view = getView();
            if (view != null) {
                view.onGetCoursesFail();
            }
        }

    }
}
