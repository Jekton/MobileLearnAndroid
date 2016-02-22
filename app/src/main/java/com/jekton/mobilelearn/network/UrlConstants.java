package com.jekton.mobilelearn.network;

/**
 * @author Jekton
 */
public class UrlConstants {

    public static final String HOST = "http://192.168.1.100:3000";

    public static final String REGISTER = HOST + "/api/register";
    public static final String LOGIN = HOST + "/api/login";
    public static final String LOGOUT = HOST + "/api/logout";

    public static final String GET_TAKEN_COURSES = HOST + "/api/takencourses";
    public static final String GET_ALL_COURSES = HOST + "/api/allcourses";
    public static final String TAKE_COURSE_TEMPLATE = HOST + "/api/takecourse/%s";

}
