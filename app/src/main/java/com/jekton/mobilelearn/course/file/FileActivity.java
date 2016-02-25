package com.jekton.mobilelearn.course.file;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;
import com.jekton.mobilelearn.course.file.FileDownloadService.DownloadServiceBinder;

import java.util.List;
import java.util.Set;

/**
 * @author Jekton
 */
public class FileActivity
        extends DialogEnabledActivity<FileActivityOps, FileActivityDocumentOps>
        implements FileActivityOps, FileListAdapter.OnButtonClicked, ListView.OnItemClickListener,
        FileDownloadService.DownloadObserver {

    private static final String INTENT_EXTRA_COURSE_ID = "INTENT_EXTRA_COURSE_ID";

    private String mCourseId;
    private List<CourseFile> mCourseFiles;
    private FileListAdapter mListAdapter;

    private boolean mBound;
    private FileDownloadService mService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DownloadServiceBinder binder = (DownloadServiceBinder) service;
            mService = binder.getService();
            mService.registerDownloadObserver(FileActivity.this);
            getDocument().setDownloadingSetAvailable(true);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDocument(this, FileActivityDocument.class);
        setContentView(R.layout.activity_file);

        initView();
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initView() {
        initActionBar();
        initCourseId();

        ListView listView = (ListView) findViewById(R.id.file_list);
        mListAdapter = new FileListAdapter(this, this);
        listView.setAdapter(mListAdapter);
    }

    private void initCourseId() {
        Intent intent = getIntent();
        mCourseId = intent.getStringExtra(INTENT_EXTRA_COURSE_ID);
        if (mCourseId == null) {
            throw new IllegalStateException("Intent used to start this activity must contains a key "
                                                    + INTENT_EXTRA_COURSE_ID);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = FileDownloadService.makeIntent(this);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDialog();
        getDocument().initFileList(mCourseId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            mService.unregisterDownloadObserver();
            getDocument().setDownloadingSetAvailable(false);
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onFilesChange(@NonNull final List<CourseFile> courseFiles) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeDialog();
                mCourseFiles = courseFiles;
                mListAdapter.updateCourseFiles(mCourseFiles);
            }
        });

    }

    @Override
    public void onGetFileListFail() {
        showToastAndDismissDialog(R.string.msg_fail_get_file_list);
    }

    @Override
    public void onNetworkError() {
        showToastAndDismissDialog(R.string.err_network_error);
    }

    @Override
    public void onLocalFileSystemError() {
        showToastAndDismissDialog(R.string.err_filesystem_error);
    }

    @Override
    public Set<String> getDownloadingSet() {
        return mService.getDownloadingSet();
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onButtonClicked(int position) {
        getDocument().performActionFor(mCourseFiles.get(position));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 2/25/2016 ask for download
        onButtonClicked(position);
    }

    @Override
    public void onStateChange(String path, int percent) {
        getDocument().onStateChange(path, percent);
    }

    public static Intent makeIntent(Activity activity, String courseId) {
        Intent intent = new Intent(activity, FileActivity.class);
        intent.putExtra(INTENT_EXTRA_COURSE_ID, courseId);
        return intent;
    }
}
