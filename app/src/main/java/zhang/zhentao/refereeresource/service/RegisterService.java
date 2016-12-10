package zhang.zhentao.refereeresource.service;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.RegisterListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class RegisterService {

    private RegisterListener listener;

    public void setListener(RegisterListener listener){
        this.listener = listener;
    }

    public void addUser(User user) {
        Map<String, String> map = new HashMap<>();
        map.put("nickName", user.getNickName());
        if(user.getRealName() != null & !user.getRealName().equals(""))
            map.put("realName", user.getRealName());
        map.put("password", user.getPassword());
        if(user.getHeight() != null)
            map.put("height", user.getHeight().toString());
        else
            map.put("height", "0");
        if(user.getWeight() != null)
            map.put("weight", user.getWeight().toString());
        else
            map.put("weight", "0");
        if(user.getEmail() != null & !user.getEmail().equals(""))
            map.put("email", user.getEmail());
        if(user.getPhone() != null & !user.getPhone().equals(""))
            map.put("phone", user.getPhone());
        if(user.getAddress() != null & !user.getAddress().equals(""))
            map.put("address", user.getAddress());
        if(user.getNote() != null & !user.getNote().equals(""))
            map.put("note", user.getNote());
        map.put("gender",user.getGender());
        map.put("registerTime",  String.valueOf(user.getRegisterTime().getTime()));
        map.put("isDelete",user.getIsDelete().toString());
        String url = "register/addUser";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                response = response.replace("\"","");
                if(response.equals("昵称已存在")){
                    Log.d("RegisterService",response);
                    listener.onError(101,response);
                }if(response.equals("注册成功")){
                    Log.d("RegisterService",response);
                    listener.onSuccess(100,response);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("RegisterService",e.getMessage());
                listener.onError(104,e.getMessage());
            }
        });
    }
}
