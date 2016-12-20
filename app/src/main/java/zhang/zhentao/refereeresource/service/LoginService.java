package zhang.zhentao.refereeresource.service;

import android.util.Log;

import net.openmob.mobileimsdk.android.core.LocalUDPDataSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.GetPostListListener;
import zhang.zhentao.refereeresource.listener.GetRefereeInfoListener;
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
                response = response.substring(1,response.length()-1);
                response = response.replace("\\","");
                if(!response.equals("账号或密码错误")) {   //登录成功
                    listener.onSuccess(100, jsonToUser(response));
                }
                else listener.onSuccess(101,null);      //登录失败
            }

            @Override
            public void onError(Exception e) {
                listener.onError(102,"网络连接异常");
            }
        });
    }

    public void loginIM(String account){
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
        new LocalUDPDataSender.SendLoginDataAsync(ContextUtil.getInstance(),account,"0"){
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
    private User jsonToUser(String json){
        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(json);
            user.setUserId(jsonObject.getInt("userId"));
            user.setNickName(jsonObject.getString("nickName"));
            user.setGender(jsonObject.getString("gender"));
            user.setHeight(Float.parseFloat(jsonObject.getString("height")));
            user.setWeight(Float.parseFloat(jsonObject.getString("weight")));
            user.setRegisterTime(new Date(jsonObject.getLong("registerTime")));
            user.setIsDelete(jsonObject.getBoolean("isDelete"));
            user.setPassword(jsonObject.getString("password"));
            if(jsonObject.has("realName"))
                user.setRealName(jsonObject.getString("realName"));
            if(jsonObject.has("email"))
                user.setEmail(jsonObject.getString("email"));
            if(jsonObject.has("phone"))
                user.setPhone(jsonObject.getString("phone"));
            if(jsonObject.has("address"))
                user.setAddress(jsonObject.getString("address"));
            if(jsonObject.has("note"))
                user.setNote(jsonObject.getString("note"));
            if(jsonObject.has("other"))
                user.setOther(jsonObject.getString("other"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
