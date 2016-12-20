package zhang.zhentao.refereeresource.util;

import android.app.Application;

import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.entity.User;


/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class ContextUtil extends Application {
    private static ContextUtil instance;
    private static User userInstance = null;
    private static Referee refereeInstance = null;

    public static ContextUtil getInstance(){
        return instance;
    }

    public static User getUserInstance(){
        return userInstance;
    }
    public static void setUserInstance(User user){
        userInstance = user;
    }
    public static void clearData(){
        userInstance = null;
        refereeInstance = null;
    }
    public static Referee getRefereeInstance(){
        return refereeInstance;
    }
    public static void setRefereeInstance(Referee referee){
        refereeInstance = referee;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }
}
