package zhang.zhentao.refereeresource.service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zhang.zhentao.refereeresource.entity.Post;
import zhang.zhentao.refereeresource.listener.AddPostListener;
import zhang.zhentao.refereeresource.listener.GetPostListListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class PostService {

    private GetPostListListener listListener;
    private AddPostListener addPostListener;

    public void setListListener(GetPostListListener listListener){
        this.listListener = listListener;
    }
    public void setAddPostListener(AddPostListener listener){
        this.addPostListener = listener;
    }
    public void getPostListFormServer(){
        String url = "post/getPostAPI";
        HttpURLConnectionUtil.sendRequest(url, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("PostService",response);
                listListener.onSuccess(100,jsonToList(response));
            }

            @Override
            public void onError(Exception e) {
                Log.d("PostService",e.getMessage());
            }
        });
    }
    public void addPostToServer(Post post){
        Map<String,String> map = new HashMap<>();
        map.put("userId",post.getUserId().toString());
        map.put("title",post.getTitle());
        map.put("content",post.getContent());
        map.put("createTime",post.getCreateTime().getTime()+"");
        String url = "post/addPostAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("PostService",response);
                response = response.replace("\"","");
                if(response.equals("success")){
                    addPostListener.onSuccess(100,response);
                }else {
                    addPostListener.onError(101,response);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("PostService",e.getMessage());
                addPostListener.onError(102,e.getMessage());
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
