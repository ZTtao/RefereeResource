package zhang.zhentao.refereeresource.util;

import android.app.Application;

import zhang.zhentao.refereeresource.entity.User;


/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class ContextUtil extends Application {
    private static ContextUtil instance;
    private static User userInstance = null;

    public static ContextUtil getInstance(){
        return instance;
    }

    public static User getUserInstance(){
        return userInstance;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }
}
