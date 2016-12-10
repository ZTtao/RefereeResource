package zhang.zhentao.refereeresource.service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zhang.zhentao.refereeresource.entity.Post;
import zhang.zhentao.refereeresource.listener.GetPostListListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class GetPostListService {

    private GetPostListListener listListener;

    public void setListListener(GetPostListListener listListener){
        this.listListener = listListener;
    }

    public void getPostListFormServer(){
        String url = "post/getPostAPI";
        HttpURLConnectionUtil.sendRequest(url, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("GetPostListService",response);
                listListener.onSuccess(100,jsonToList(response));
            }

            @Override
            public void onError(Exception e) {
                Log.d("GetPostListService",e.getMessage());
            }
        });
    }

    private List<Post> jsonToList(String json){
        List<Post> list = new ArrayList<>();
        try {
            json=json.substring(1,json.length()-1);
            JSONArray jsonArray = new JSONArray(json);
            int index = 0;
            while (index < jsonArray.length()){
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                Post post = new Post();
                post.setTitle(jsonObject.getString("title"));
                post.setContent(jsonObject.getString("content"));
                post.setCreateTime(new Date(jsonObject.getLong("createTime")));
                post.setIsDelete(jsonObject.getBoolean("isDelete"));
                post.setNote(jsonObject.getString("note"));
                post.setOther(jsonObject.getString("other"));
                post.setPostId(jsonObject.getInt("postId"));
                post.setUserId(jsonObject.getInt("userId"));
                post.setUserName(jsonObject.getString("userName"));
                list.add(post);
                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
