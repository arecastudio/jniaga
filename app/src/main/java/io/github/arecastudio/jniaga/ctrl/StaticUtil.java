package io.github.arecastudio.jniaga.ctrl;

import android.content.Context;

/**
 * Created by android on 12/1/17.
 */

public class StaticUtil {
    private static boolean isLogin;
    private static Context context;
    private static final String webUrl="http://139.59.241.190/jniaga/";
    //Jayapura Niaga Group --> 905230449656779
    //Jayapura Dagang Group 731228637011358
    private static final String goupId="905230449656779";
    private static String UserId="";
    private static String UserName;

    public static void ResetAll(){
        UserId="";
        UserName="";
    }

    public StaticUtil(){
    }

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static String getGoupId() {
        return goupId;
    }

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static String getWebUrl() {
        //StaticUtil.webUrl="http://10.0.2.2/projects/jniaga/";
        return webUrl;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        StaticUtil.isLogin = isLogin;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        StaticUtil.context = context;
    }
}
