package com.jekton.mobilelearn.course.file;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jekton.mobilelearn.common.network.HttpUtils;
import com.jekton.mobilelearn.common.network.operator.NetworkOperatorService;
import com.jekton.mobilelearn.common.network.operator.NetworkOperators;
import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Response;

/**
 * @author Jekton
 */
public class FileDownloadService extends Service {

    private static final String LOG_TAG = FileDownloadService.class.getSimpleName();
    private static final String INTENT_ACTION = "com.jekton.mobilelearn.FileDownloadService";

    private static final String INTENT_KEY_DOWNLOAD_FROM = "INTENT_KEY_DOWNLOAD_FROM";
    private static final String INTENT_KEY_STORE_TO = "INTENT_KEY_STORE_TO";

    private NetworkOperatorService mNetworkOperator;
    private Set<String> mDownloadingSet;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetworkOperator = NetworkOperators.newMultiRequestOperator();
        mDownloadingSet = Collections.synchronizedSet(new HashSet<String>());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNetworkOperator.shutdown();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String downloadFrom = intent.getStringExtra(INTENT_KEY_DOWNLOAD_FROM);
        if (downloadFrom == null) {
            return Service.START_STICKY;
        }
        String storeTo = intent.getStringExtra(INTENT_KEY_STORE_TO);
        if (storeTo == null) {
            return Service.START_STICKY;
        }
        Logger.d(LOG_TAG, "downloadFrom: " + downloadFrom);
        Logger.d(LOG_TAG, "storeTo: " + storeTo);

        if (!mDownloadingSet.contains(downloadFrom)) {
            mDownloadingSet.add(downloadFrom);
            mNetworkOperator.executeRequest(
                    HttpUtils.makeGetRequest(downloadFrom),
                    new DownLoadResponseCallback(downloadFrom, storeTo));
        }


        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static Intent makeDownIntent(String downloadFrom, String storeTo) {
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_KEY_DOWNLOAD_FROM, downloadFrom);
        intent.putExtra(INTENT_KEY_STORE_TO, storeTo);

        return intent;
    }


    private class DownLoadResponseCallback implements OnResponseCallback {

        private final String mDownloadFrom;
        private final String mStoreTo;

        public DownLoadResponseCallback(String downloadFrom, String storeTo) {
            mDownloadFrom = downloadFrom;
            mStoreTo = storeTo;
        }

        @Override
        public void onResponseSuccess(Response response) {
            try {
                FileUtil.writeToPath(response.body().byteStream(), mStoreTo);
                mDownloadingSet.remove(mDownloadFrom);
                if (mDownloadingSet.size() == 0) {
                    stopSelf();
                }
                // TODO: 2/25/2016
            } catch (IOException e) {
                // TODO: 2/25/2016
                Logger.e(LOG_TAG, e);
            }
        }

        @Override
        public void onNetworkFail() {
            // TODO: 2/25/2016
        }

        @Override
        public void onResponseFail(Response response) {
            // TODO: 2/25/2016
        }
    }
}
