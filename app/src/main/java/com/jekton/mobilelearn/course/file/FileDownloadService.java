package com.jekton.mobilelearn.course.file;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jekton.mobilelearn.common.network.HttpUtils;
import com.jekton.mobilelearn.common.network.operator.NetworkOperatorService;
import com.jekton.mobilelearn.common.network.operator.NetworkOperators;
import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;
import com.jekton.mobilelearn.network.UrlConstants;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Response;

/**
 * @author Jekton
 */
public class FileDownloadService extends Service {

    private static final String LOG_TAG = FileDownloadService.class.getSimpleName();

    private static final String INTENT_KEY_REMOTE_PATH = "INTENT_KEY_REMOTE_PATH";
    private static final String INTENT_KEY_STORE_TO = "INTENT_KEY_STORE_TO";

    private IBinder mBinder = new DownloadServiceBinder();

    private final Object mLock = new Object();
    private Set<String> mDownloadingSet = new HashSet<>();
    private NetworkOperatorService mNetworkOperator = NetworkOperators.newMultiRequestOperator();


    @Override
    public void onDestroy() {
        mNetworkOperator.shutdown();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String remotePath = intent.getStringExtra(INTENT_KEY_REMOTE_PATH);
        if (remotePath == null) {
            return Service.START_STICKY;
        }
        String storeTo = intent.getStringExtra(INTENT_KEY_STORE_TO);
        if (storeTo == null) {
            return Service.START_STICKY;
        }
        Logger.d(LOG_TAG, "remotePath: " + remotePath);
        Logger.d(LOG_TAG, "storeTo: " + storeTo);

        boolean needToExecuted = false;
        synchronized (mLock) {
            if (!mDownloadingSet.contains(remotePath)) {
                mDownloadingSet.add(remotePath);
                needToExecuted = true;
            }
        }
        if (needToExecuted) {
            mNetworkOperator.executeRequest(
                    HttpUtils.makeGetRequest(UrlConstants.HOST + remotePath),
                    new DownLoadResponseCallback(remotePath, storeTo));
            return Service.START_REDELIVER_INTENT;
        }

        return Service.START_STICKY;
    }


    public class DownloadServiceBinder extends Binder {
        public FileDownloadService getService() {
            return FileDownloadService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, FileDownloadService.class);
    }

    public static Intent makeDownloadIntent(Context context, String remotePath, String storeTo) {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.putExtra(INTENT_KEY_REMOTE_PATH, remotePath);
        intent.putExtra(INTENT_KEY_STORE_TO, storeTo);

        return intent;
    }


    public Set<String> getDownloadingSet() {
        synchronized (mLock) {
            return new HashSet<>(mDownloadingSet);
        }
    }


    private class DownLoadResponseCallback implements OnResponseCallback {

        private final String mRemotePath;
        private final String mStoreTo;

        public DownLoadResponseCallback(String remotePath, String storeTo) {
            mRemotePath = remotePath;
            mStoreTo = storeTo;
        }

        @Override
        public void onResponseSuccess(Response response) {
            try {
                FileUtil.writeToPath(response.body().byteStream(), mStoreTo);
                synchronized (mLock) {
                    mDownloadingSet.remove(mRemotePath);
                    if (mDownloadingSet.size() == 0) {
                        stopSelf();
                    }
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


    public interface DownloadResolver {

        void onStateChange(String path, float percentage);

    }
}
