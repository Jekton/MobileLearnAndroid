package com.jekton.mobilelearn.course.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.course.Course;

/**
 * @author Jekton
 */
public class LectureListAdapter extends BaseAdapter {

    private Context mContext;
    private Course mCourse;

    public LectureListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCourse == null ? 0 : mCourse.lectures.length;
    }

    @Override
    public Course.Path getItem(int position) {
        return mCourse.lectures[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.widget_lecture_list_item,
                             parent,
                             false);
            ViewHolder holder = new ViewHolder();
            holder.lectureName = (TextView) convertView.findViewById(R.id.lecture_name);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.lectureName.setText(getItem(position).filename);

        return convertView;
    }

    public void onCourseChange(Course course) {
        mCourse = course;
        notifyDataSetChanged();
    }


    private static class ViewHolder {
        TextView lectureName;
    }


}
