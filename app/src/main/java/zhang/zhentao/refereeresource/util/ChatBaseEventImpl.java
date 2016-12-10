package zhang.zhentao.refereeresource.util;

import android.util.Log;

import net.openmob.mobileimsdk.android.event.ChatBaseEvent;

import java.util.Observer;

import zhang.zhentao.refereeresource.activity.MainActivity;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class ChatBaseEventImpl implements ChatBaseEvent {

    private Observer loginOkForLaunchObserver = null;

    @Override
    public void onLoginMessage(int dwUserId, int dwErrorCode) {
        if(dwErrorCode == 0){
            Log.d("ChatBaseEventImpl","登录成功，userId:"+dwUserId);
        }else{
            Log.d("ChatBaseEventImpl","登录失败，错误代码："+dwErrorCode);
        }

        if(loginOkForLaunchObserver != null){
            loginOkForLaunchObserver.update(null,dwErrorCode);
            loginOkForLaunchObserver = null;
        }
    }

    @Override
    public void onLinkCloseMessage(int errorCode) {
        Log.d("ChatBaseEventImpl","网络连接出错，已断开，error:"+errorCode);
    }

    public void setLoginOkForLaunchObserver(Observer loginOkForLaunchObserver){
        this.loginOkForLaunchObserver = loginOkForLaunchObserver;
    }

}
