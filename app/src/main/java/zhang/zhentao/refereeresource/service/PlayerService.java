package zhang.zhentao.refereeresource.service;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.entity.Player;
import zhang.zhentao.refereeresource.listener.GetPlayerInfoListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.UpdatePlayerInfoListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/20.
 */

public class PlayerService {
    private GetPlayerInfoListener getPlayerInfoListener;
    private UpdatePlayerInfoListener updatePlayerInfoListener;
    public void setGetPlayerInfoListener(GetPlayerInfoListener listener){
        this.getPlayerInfoListener = listener;
    }
    public void setUpdatePlayerInfoListener(UpdatePlayerInfoListener listener){
        updatePlayerInfoListener = listener;
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
}
