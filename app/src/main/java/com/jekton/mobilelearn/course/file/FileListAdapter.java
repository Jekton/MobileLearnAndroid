package com.jekton.mobilelearn.course.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jekton.mobilelearn.R;

import java.util.List;

/**
 * @author Jekton
 */
class FileListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private OnButtonClicked mOnButtonClicked;
    private List<CourseFile> mCourseFiles;

    public FileListAdapter(Context context, OnButtonClicked onButtonClicked) {
        mContext = context;
        mOnButtonClicked = onButtonClicked;
    }

    @Override
    public int getCount() {
        return mCourseFiles == null ? 0 : mCourseFiles.size();
    }

    @Override
    public CourseFile getItem(int position) {
        return mCourseFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.widget_file_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.filename);
            holder.mButton = (Button) convertView.findViewById(R.id.button);
            holder.mButton.setOnClickListener(this);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        CourseFile file = getItem(position);
        holder.mTextView.setText(file.filename);
        holder.mButton.setText(makeBtnText(file));
        holder.mButton.setClickable(file.state != CourseFile.STATE_DOWNLOADING);
        // use to find that which item clicked
        holder.mButton.setTag(position);
        return convertView;
    }


    private String makeBtnText(CourseFile file) {
        switch (file.state) {
            case CourseFile.STATE_NOT_DOWNLOAD:
                return mContext.getString(R.string.file_list_btn_not_download);
            case CourseFile.STATE_DOWNLOADING:
                return String.format(mContext.getString(R.string.file_list_btn_downloading),
                                     file.downloadProgress);
            case CourseFile.STATE_DOWNLOADED:
                return mContext.getString(R.string.file_list_btn_downloaded);
            default:
                return "";
        }
    }

    public void updateCourseFiles(List<CourseFile> courseFiles) {
        mCourseFiles = courseFiles;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        mOnButtonClicked.onButtonClicked(position);
    }


    private static class ViewHolder {
        TextView mTextView;
        Button mButton;
    }

    public interface OnButtonClicked {
        void onButtonClicked(int position);
    }
}
