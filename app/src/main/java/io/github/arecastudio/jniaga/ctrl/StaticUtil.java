package io.github.arecastudio.jniaga.ctrl;

import android.content.Context;

/**
 * Created by android on 12/1/17.
 */

public class StaticUtil {
    private static boolean isLogin;
    private static Context context;
    private static String webUrl="http://139.59.241.190/api/jniaga/";

    public StaticUtil(){
    }

    public static String getWebUrl() {
        StaticUtil.webUrl="http://10.0.2.2/projects/jniaga/";
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
