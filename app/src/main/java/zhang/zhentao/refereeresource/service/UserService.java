package zhang.zhentao.refereeresource.service;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.entity.FriendListItem;
import zhang.zhentao.refereeresource.entity.Player;
import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.AddFriendListener;
import zhang.zhentao.refereeresource.listener.GetFriendInfoByIdListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.SearchFriendListener;
import zhang.zhentao.refereeresource.util.ContextUtil;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public class UserService {
    private SearchFriendListener searchFriendListener;
    private GetFriendInfoByIdListener getFriendInfoByIdListener;
    private AddFriendListener addFriendListener;
    public void setSearchFriendListener(SearchFriendListener listener){
        searchFriendListener = listener;
    }
    public void setGetFriendInfoByIdListener(GetFriendInfoByIdListener listener){
        getFriendInfoByIdListener = listener;
    }
    public void setAddFriendListener(AddFriendListener listener){
        addFriendListener = listener;
    }
    public void searchFriend(String str){
        String url = "user/searchFriend";
        Map<String,String> map = new HashMap<>();
        map.put("condition",str);
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("UserService",response);
                response = response.substring(1,response.length()-1);
                response = response.replace("\\","");
                if(response.equals("nodata"))
                    searchFriendListener.onSuccess(100,null);
                else
                    searchFriendListener.onSuccess(100, JSON.parseArray(response, FriendListItem.class));
            }

            @Override
            public void onError(Exception e) {
                Log.d("UserService",e.getMessage());
                searchFriendListener.onError(102,"网络异常");
            }
        });
    }
    public void getFriendInfoById(int id){
        Map<String,String> map = new HashMap<>();
        map.put("userId", ContextUtil.getUserInstance().getUserId()+"");
        map.put("friendId",id+"");
        String url = "user/getFriendInfo";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("UserService",response);
                response = response.substring(1,response.length()-1);
                response = response.replace("\\","");
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject userJson = jsonObject.getJSONObject("user");
                User user = new User();
                user.setNickName(userJson.getString("nickName"));
                user.setNote(userJson.getString("note"));
                user.setEmail(userJson.getString("email"));
                user.setGender(userJson.getString("gender"));
                user.setHeight(Float.parseFloat(userJson.getString("height")));
                user.setUserId(Integer.parseInt(userJson.getString("userId")));
                user.setWeight(Float.parseFloat(userJson.getString("weight")));
                Player player = null;
                Referee referee = null;
                JSONObject playerJson = jsonObject.getJSONObject("player");
                if(playerJson != null){
                    player = new Player();
                    player.setTeam(playerJson.getString("team"));
                    player.setHonor(playerJson.getString("honor"));
                    player.setExperience(playerJson.getString("experience"));
                    player.setNote(playerJson.getString("note"));
                    player.setStartPlayTime(new Date(Long.parseLong(playerJson.getString("startPlayTime"))));
                }
                JSONObject refereeJson = jsonObject.getJSONObject("referee");
                if(refereeJson != null){
                    referee = new Referee();
                    referee.setHonor(refereeJson.getString("honor"));
                    referee.setNote(refereeJson.getString("note"));
                    referee.setExperience(refereeJson.getString("experience"));
                    referee.setCertificate(refereeJson.getString("certificate"));
                    referee.setStartJudgeTime(new Date(Long.parseLong(refereeJson.getString("startJudgeTime"))));
                }
                boolean isFriend = jsonObject.getBoolean("isFriend");
                getFriendInfoByIdListener.onSuccess(100,user,referee,player,isFriend);
            }

            @Override
            public void onError(Exception e) {
                Log.d("UserService",e.getMessage());
                getFriendInfoByIdListener.onError(102,"网络异常");
            }
        });
    }
    public void addFriend(int friendId){
        Map<String,String> map = new HashMap<>();
        map.put("userId",ContextUtil.getUserInstance().getUserId()+"");
        map.put("friendId",friendId+"");
        String url = "user/addFriend";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("UserService",response);
                response = response.replace("\"","");
                if(response.equals("success")){
                    addFriendListener.onSuccess(100,"success");
                }else{
                    addFriendListener.onError(101,"faild");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("UserService",e.getMessage());
                addFriendListener.onError(102,"网络异常");
            }
        });
    }
}
