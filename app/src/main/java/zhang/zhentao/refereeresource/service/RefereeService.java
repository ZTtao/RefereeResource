package zhang.zhentao.refereeresource.service;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.entity.RefereeReservation;
import zhang.zhentao.refereeresource.listener.AddRefereeReservationListener;
import zhang.zhentao.refereeresource.listener.GetRefereeInfoListener;
import zhang.zhentao.refereeresource.listener.GetRefereeReservationListener;
import zhang.zhentao.refereeresource.listener.HasReplyListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.UpdateRefereeInfoListener;
import zhang.zhentao.refereeresource.listener.UpdateReplyListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/17.
 */

public class RefereeService {
    private GetRefereeInfoListener getRefereeInfoListener;
    private UpdateRefereeInfoListener updateRefereeInfoListener;
    private GetRefereeReservationListener getRefereeReservationListener;
    private AddRefereeReservationListener addRefereeReservationListener;
    private HasReplyListener hasReplyListener;
    private UpdateReplyListener updateReplyListener;

    public void setGetRefereeInfoListener(GetRefereeInfoListener listener){
        this.getRefereeInfoListener = listener;
    }
    public void setUpdateRefereeInfoListener(UpdateRefereeInfoListener listener){
        updateRefereeInfoListener = listener;
    }
    public void setGetRefereeReservationListener(GetRefereeReservationListener listener){
        getRefereeReservationListener = listener;
    }
    public void setAddRefereeReservationListener(AddRefereeReservationListener listener){
        addRefereeReservationListener = listener;
    }
    public void setHasReplyListener(HasReplyListener listener){
        hasReplyListener = listener;
    }
    public void setUpdateReplyListener(UpdateReplyListener listener){
        updateReplyListener = listener;
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
    public void getRefereeReservation(){
        String url = "refereeReservation/getReservation";
        HttpURLConnectionUtil.sendRequest(url, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("RefereeService",response);
                response = response.substring(1,response.length()-1);
                if(response.equals("nodata"))getRefereeReservationListener.onSuccess(101,null);
                else if(response.equals("faild"))getRefereeReservationListener.onError(102,"获取数据失败");
                else getRefereeReservationListener.onSuccess(100,JSON.parseArray(response, RefereeReservation.class));
            }

            @Override
            public void onError(Exception e) {
                Log.d("RefereeService",e.getMessage());
                getRefereeReservationListener.onError(103,"网络错误");
            }
        });
    }
    public void addRefereeReservation(RefereeReservation refereeReservation){
        Map<String,String> map = new HashMap<>();
        map.put("playerId",refereeReservation.getPlayerId()+"");
        map.put("gameTime",refereeReservation.getGameTime().getTime()+"");
        map.put("address",refereeReservation.getAddress());
        map.put("gameState",refereeReservation.getGameState());
        map.put("refereeCount",refereeReservation.getRefereeCount());
        map.put("refereeRequire",refereeReservation.getRefereeRequire());
        map.put("reward",refereeReservation.getReward());
        map.put("createTime",refereeReservation.getCreateTime().getTime()+"");
        String url = "refereeReservation/addReservation";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("RefereeService",response);
                response = response.replace("\"","");
                if(response.equals("success"))
                    addRefereeReservationListener.onSuccess(100,"success");
                else
                    addRefereeReservationListener.onError(101,"faild");
            }

            @Override
            public void onError(Exception e) {
                Log.d("RefereeService",e.getMessage());
                addRefereeReservationListener.onError(102,"网络异常");
            }
        });
    }
    public void hasReply(int refereeId,int refereeReservationId){
        Map<String,String> map = new HashMap<>();
        map.put("refereeId",refereeId+"");
        map.put("reservationId",refereeReservationId+"");
        String url = "refereeReservation/hasReply";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("RefereeService",response);
                response = response.replace("\"","");
                if(response.equals("true"))
                    hasReplyListener.onSuccess(100,"true");
                else
                    hasReplyListener.onSuccess(101,"false");
            }

            @Override
            public void onError(Exception e) {
                Log.d("RefereeService",e.getMessage());
                hasReplyListener.onError(102,"网络错误");
            }
        });
    }
    public void updateReply(int refereeId, int reservationId, boolean isDelete){
        Map<String,String> map = new HashMap<>();
        map.put("refereeId",refereeId+"");
        map.put("reservationId",reservationId+"");
        map.put("isDelete",isDelete+"");
        String url = "refereeReservation/updateReply";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("RefereeService",response);
                response = response.replace("\"","");
                if(response.equals("success"))
                    updateReplyListener.onSuccess(100,"success");
                else updateReplyListener.onError(101,"faild");
            }

            @Override
            public void onError(Exception e) {
                Log.d("RefereeService",e.getMessage());
                updateReplyListener.onError(102,"网络异常");
            }
        });
    }
}
