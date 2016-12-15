package zhang.zhentao.refereeresource.service;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.InfoListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/15.
 */

public class InfoService {
    private InfoListener listener;

    public void setListener(InfoListener listener){
        this.listener = listener;
    }
    public void updateInfo(User user){
        Map<String,String> map = new HashMap<>();
        map.put("nickName", user.getNickName());
        if(user.getRealName() != null & !user.getRealName().equals(""))
            map.put("realName", user.getRealName());
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
        String url = "user/updateInfoAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("InfoService",response);
                response = response.replace("\"","");
                if(response.equals("success")){
                    listener.onSuccess(100,"更新成功");
                }else{
                    listener.onError(101,"更新失败");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("InfoService",e.getMessage());
                listener.onError(102,"网络错误");
            }
        });
    }
}
