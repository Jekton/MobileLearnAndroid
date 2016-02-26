package com.jekton.mobilelearn.course.file;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jekton.mobilelearn.common.network.operator.NetworkOperatorService;
import com.jekton.mobilelearn.common.network.operator.NetworkOperators;
import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Jekton
 */
public class FileUploadService extends Service {

    private static final String TAG = FileUploadService.class.getSimpleName();

    private static final String INTENT_EXTRA_FILE_PATH = "INTENT_EXTRA_FILE_PATH";
    private static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private final NetworkOperatorService mNetworkOperator
            = NetworkOperators.newMultiRequestOperator();
    private final Object mLock = new Object();
    private final Set<String> mUploadingSet = new HashSet<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String filePath = intent.getStringExtra(INTENT_EXTRA_FILE_PATH);
        String url = intent.getStringExtra(INTENT_EXTRA_URL);
        if (filePath == null || url == null) {
            return START_NOT_STICKY;
        }
        Logger.d(TAG, "filePath = " + filePath);
        Logger.d(TAG, "url = " + url);
        String cookie = url + filePath;
        synchronized (mLock) {
            if (mUploadingSet.contains(cookie)) {
                return START_NOT_STICKY;
            } else {
                mUploadingSet.add(cookie);
            }
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return START_NOT_STICKY;
        }


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                                 RequestBody.create(null, file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mNetworkOperator.executeRequest(request, new UpLoadResponseCallback(cookie));

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        mNetworkOperator.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static Intent makeIntent(Context context, String filePath, String url) {
        Intent intent = new Intent(context, FileUploadService.class);
        intent.putExtra(INTENT_EXTRA_FILE_PATH, filePath);
        intent.putExtra(INTENT_EXTRA_URL, url);
        return intent;
    }


    private class UpLoadResponseCallback implements OnResponseCallback {

        private final String mCookie;

        public UpLoadResponseCallback(String cookie) {
            mCookie = cookie;
        }

        private void removeCookie() {
            synchronized (mLock) {
                mUploadingSet.remove(mCookie);
                if (mUploadingSet.size() == 0) {
                    stopSelf();
                }
            }
        }

        @Override
        public void onResponseSuccess(Response response) {
            Logger.d(TAG, "onResponseSuccess");
            removeCookie();
        }

        @Override
        public void onNetworkFail() {
            Logger.d(TAG, "onResponseFail");
            removeCookie();
        }

        @Override
        public void onResponseFail(Response response) {
            Logger.d(TAG, "onResponseFail");
            removeCookie();
        }
    }
}
