package zhang.zhentao.refereeresource.service;

import android.util.Log;

import net.openmob.mobileimsdk.android.core.LocalUDPDataSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import zhang.zhentao.refereeresource.listener.GetPostListListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.LoginListener;
import zhang.zhentao.refereeresource.util.ContextUtil;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;
import zhang.zhentao.refereeresource.util.IMClientManager;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class LoginService {

    private LoginListener listener;
    private Observer onLoginSuccessObserver = null;

    public void setListener(LoginListener listener){
        this.listener = listener;
    }

    public void Login(String account,String password){
        String url = "login/loginAPI";
        Map<String,String> map = new HashMap<>();
        map.put("account",account);
        map.put("password",password);
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("LoginService",response);
                response = response.replace("\"","");
                if(response.equals("登录成功"))
                    listener.onSuccess(100,response);
                else listener.onSuccess(101,response);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(102,"网络连接异常");
            }
        });
    }

    public void loginIM(String account,String password){
        IMClientManager.getInstance(ContextUtil.getInstance()).initMobileIMSDK();
        onLoginSuccessObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                int code = (Integer)arg;
                if(code == 0){
                    Log.d("LoginService","登录IM成功");
                }else{
                    Log.d("LoginService","登录失败"+code);
                }
            }
        };

        IMClientManager.getInstance(ContextUtil.getInstance()).getBaseEventListener().setLoginOkForLaunchObserver(onLoginSuccessObserver);
        new LocalUDPDataSender.SendLoginDataAsync(ContextUtil.getInstance(),account,password){
            @Override
            protected void fireAfterSendLogin(int code){
                if(code == 0){
                    Log.d("LoginService","登录信息已发出");
                }else{
                    Log.d("LoginService","登录信息发送失败："+code);
                }
            }
        }.execute();
    }

}
