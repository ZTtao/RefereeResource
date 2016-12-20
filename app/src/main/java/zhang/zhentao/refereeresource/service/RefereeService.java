package zhang.zhentao.refereeresource.service;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.listener.GetRefereeInfoListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.UpdateRefereeInfoListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/17.
 */

public class RefereeService {
    private GetRefereeInfoListener getRefereeInfoListener;
    private UpdateRefereeInfoListener updateRefereeInfoListener;
    public void setGetRefereeInfoListener(GetRefereeInfoListener listener){
        this.getRefereeInfoListener = listener;
    }
    public void setUpdateRefereeInfoListener(UpdateRefereeInfoListener listener){
        updateRefereeInfoListener = listener;
    }
    public void getRefereeInfo(String nickName){
        Map<String,String> map = new HashMap<>();
        map.put("nickName",nickName);
        String url = "referee/getRefereeAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("RefereeService",response);
                response = response.substring(1,response.length()-1);
                response = response.replace("\\","");
                if(!response.equals("nodata")){
                    getRefereeInfoListener.onSuccess(100,JSON.parseObject(response,Referee.class));
                }else
                    getRefereeInfoListener.onError(101,"not a referee");
            }

            @Override
            public void onError(Exception e) {
                Log.d("RefereeService",e.getMessage());
                getRefereeInfoListener.onError(102,e.getMessage());
            }
        });
    }
    public void addOrUpdateRefereeInfo(Referee referee){
        Map<String,String> map = new HashMap<>();
        map.put("userId",referee.getUserId()+"");
        map.put("startJudgeTime",referee.getStartJudgeTime().getTime()+"");
        if(referee.getExperience() != null)
            map.put("experience",referee.getExperience());
        if(referee.getHonor() != null)
            map.put("honor",referee.getHonor());
        if(referee.getCertificate() != null)
            map.put("certificate",referee.getCertificate());
        if(referee.getNote() != null)
            map.put("note",referee.getNote());
        map.put("isDelete","false");
        if(referee.getOther() != null)
            map.put("other",referee.getOther());
        map.put("registerTime",referee.getRegisterTime().getTime()+"");
        String url = "referee/updateRefereeAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("RefereeService",response);
                response = response.replace("\"","");
                if(response.equals("success")){
                    updateRefereeInfoListener.onSuccess(100,"更新成功");
                }else{
                    updateRefereeInfoListener.onError(101,"更新失败");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("RefereeService",e.getMessage());
                updateRefereeInfoListener.onError(102,"网络错误");
            }
        });
    }
}
