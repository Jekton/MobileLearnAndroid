package com.jekton.mobilelearn;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @author Jekton
 */
public class MyApplication extends Application {

    private static final String LOG_TAG = MyApplication.class.getSimpleName();

    private static volatile Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        initImageLoader();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        builder.denyCacheImageMultipleSizesInMemory();
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        builder.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        builder.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(builder.build());
    }

    public static Application getInstance() {
        return sApplication;
    }
}
