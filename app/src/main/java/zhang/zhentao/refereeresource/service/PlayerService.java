package zhang.zhentao.refereeresource.service;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.entity.GameReservation;
import zhang.zhentao.refereeresource.entity.Player;
import zhang.zhentao.refereeresource.listener.AddGameReservationListener;
import zhang.zhentao.refereeresource.listener.GetGameReservationListener;
import zhang.zhentao.refereeresource.listener.GetPlayerInfoListener;
import zhang.zhentao.refereeresource.listener.HasReplyListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.UpdatePlayerInfoListener;
import zhang.zhentao.refereeresource.listener.UpdateReplyListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/20.
 */

public class PlayerService {
    private GetPlayerInfoListener getPlayerInfoListener;
    private UpdatePlayerInfoListener updatePlayerInfoListener;
    private GetGameReservationListener getGameReservationListener;
    private AddGameReservationListener addGameReservationListener;
    private HasReplyListener hasReplyListener;
    private UpdateReplyListener updateReplyListener;

    public void setGetPlayerInfoListener(GetPlayerInfoListener listener){
        this.getPlayerInfoListener = listener;
    }
    public void setUpdatePlayerInfoListener(UpdatePlayerInfoListener listener){
        updatePlayerInfoListener = listener;
    }
    public void setGetGameReservationListener(GetGameReservationListener listener){
        getGameReservationListener = listener;
    }
    public void setHasReplyListener(HasReplyListener hasReplyListener) {
        this.hasReplyListener = hasReplyListener;
    }
    public void setUpdateReplyListener(UpdateReplyListener updateReplyListener) {
        this.updateReplyListener = updateReplyListener;
    }
    public void setAddGameReservationListener(AddGameReservationListener addGameReservationListener) {
        this.addGameReservationListener = addGameReservationListener;
    }

    public void getPlayerInfo(String nickName){
        Map<String,String> map = new HashMap<>();
        map.put("nickName",nickName);
        String url = "player/getPlayerAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("PlayerService",response);
                response = response.substring(1,response.length()-1);
                response = response.replace("\\","");
                if(!response.equals("nodata")){
                    getPlayerInfoListener.onSuccess(100, JSON.parseObject(response,Player.class));
                }else
                    getPlayerInfoListener.onError(101,"not a player");
            }

            @Override
            public void onError(Exception e) {
                Log.d("PlayerService",e.getMessage());
                getPlayerInfoListener.onError(102,e.getMessage());
            }
        });
    }
    public void addOrUpdatePlayerInfo(Player player){
        Map<String,String> map = new HashMap<>();
        map.put("userId",player.getUserId()+"");
        map.put("startPlayTime",player.getStartPlayTime().getTime()+"");
        if(player.getExperience() != null)
            map.put("experience",player.getExperience());
        if(player.getHonor() != null)
            map.put("honor",player.getHonor());
        if(player.getTeam() != null)
            map.put("team",player.getTeam());
        if(player.getNote() != null)
            map.put("note",player.getNote());
        map.put("isDelete","false");
        if(player.getOther() != null)
            map.put("other",player.getOther());
        map.put("registerTime",player.getRegisterTime().getTime()+"");
        String url = "player/updatePlayerAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("PlayerService",response);
                response = response.replace("\"","");
                if(response.equals("success")){
                    updatePlayerInfoListener.onSuccess(100,"更新成功");
                }else{
                    updatePlayerInfoListener.onError(101,"更新失败");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("PlayerService",e.getMessage());
                updatePlayerInfoListener.onError(102,"网络错误");
            }
        });
    }

    public void getGameReservation(){
        String url = "gameReservation/getReservation";
        HttpURLConnectionUtil.sendRequest(url, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("GameService",response);
                response = response.substring(1,response.length()-1);
                if(response.equals("nodata"))getGameReservationListener.onSuccess(101,null);
                else if(response.equals("faild"))getGameReservationListener.onError(102,"获取数据失败");
                else getGameReservationListener.onSuccess(100,JSON.parseArray(response, GameReservation.class));
            }

            @Override
            public void onError(Exception e) {
                Log.d("GameService",e.getMessage());
                getGameReservationListener.onError(103,"网络错误");
            }
        });
    }

    public void addGameReservation(GameReservation gameReservation){
        Map<String,String> map = new HashMap<>();
        map.put("playerId",gameReservation.getPlayerId()+"");
        map.put("gameTime",gameReservation.getGameTime().getTime()+"");
        map.put("address",gameReservation.getAddress());
        map.put("team",gameReservation.getTeam());
        map.put("level",gameReservation.getLevel());
        map.put("createTime",gameReservation.getCreateTime().getTime()+"");
        String url = "gameReservation/addReservation";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("PlayerService",response);
                response = response.replace("\"","");
                if(response.equals("success"))
                    addGameReservationListener.onSuccess(100,"success");
                else
                    addGameReservationListener.onError(101,"faild");
            }

            @Override
            public void onError(Exception e) {
                Log.d("PalyerService",e.getMessage());
                addGameReservationListener.onError(102,"网络异常");
            }
        });
    }
    public void hasReply(int playerId,int reservationId){
        Map<String,String> map = new HashMap<>();
        map.put("playerId",playerId+"");
        map.put("reservationId",reservationId+"");
        String url = "gameReservation/hasReply";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("PlayerService",response);
                response = response.replace("\"","");
                if(response.equals("true"))
                    hasReplyListener.onSuccess(100,"true");
                else
                    hasReplyListener.onSuccess(101,"false");
            }

            @Override
            public void onError(Exception e) {
                Log.d("PlayerService",e.getMessage());
                hasReplyListener.onError(102,"网络错误");
            }
        });
    }
    public void updateReply(int playerId,int reservationId,boolean isDelete){
        Map<String,String> map = new HashMap<>();
        map.put("playerId",playerId+"");
        map.put("reservationId",reservationId+"");
        map.put("isDelete",isDelete+"");
        String url = "gameReservation/updateReply";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("PlayerService",response);
                response = response.replace("\"","");
                if(response.equals("success"))
                    updateReplyListener.onSuccess(100,"success");
                else updateReplyListener.onError(101,"faild");
            }

            @Override
            public void onError(Exception e) {
                Log.d("PlayerService",e.getMessage());
                updateReplyListener.onError(102,"网络异常");
            }
        });
    }
}
