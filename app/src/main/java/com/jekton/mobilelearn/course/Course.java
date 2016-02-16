package com.jekton.mobilelearn.course;

import java.util.Arrays;

/**
 * @author Jekton
 */
public class Course {

    public String _id;
    public String name;
    public String desc;
    public int[] categories;
    public String iconPath;
    public String createdBy;    // email of course creator
    public String[] managedBy;
    public int lectureNum;
    public Path[] lectures;
    public Path[] files;
    public boolean publish;
    public boolean taken;

    public class Path {
        public String _id;
        public String filename;
        public String path;

        @Override
        public String toString() {
            return "Path{" +
                    "_id='" + _id + '\'' +
                    ", filename='" + filename + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "Course{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", categories=" + Arrays.toString(categories) +
                ", iconPath='" + iconPath + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", managedBy=" + Arrays.toString(managedBy) +
                ", lectureNum=" + lectureNum +
                ", lectures=" + Arrays.toString(lectures) +
                ", files=" + Arrays.toString(files) +
                ", publish=" + publish +
                '}';
    }
}
