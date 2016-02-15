package com.jekton.mobilelearn.course.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.course.Course;
import com.jekton.mobilelearn.network.UrlConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author Jekton
 */
public class AllCourseListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Course> mCourses;

    public AllCourseListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCourses == null ? 0 : mCourses.size();
    }

    @Override
    public Course getItem(int position) {
        return mCourses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.widget_all_courses_list_item,
                             parent,
                             false);
            ViewHolder holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.image);
            holder.courseNameTextView = (TextView) convertView.findViewById(R.id.name);
            holder.courseDescTextView = (TextView) convertView.findViewById(R.id.desc);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        Course course = getItem(position);
        holder.courseNameTextView.setText(course.name);
        holder.courseDescTextView.setText(course.desc);
        ImageLoader.getInstance().displayImage(UrlConstants.HOST + course.iconPath,
                                               holder.iconImageView);

        return convertView;
    }

    public void updateCourseList(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView iconImageView;
        TextView courseNameTextView;
        TextView courseDescTextView;
    }

}
