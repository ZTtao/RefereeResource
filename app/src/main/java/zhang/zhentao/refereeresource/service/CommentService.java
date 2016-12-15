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

import zhang.zhentao.refereeresource.entity.Comment;
import zhang.zhentao.refereeresource.entity.Post;
import zhang.zhentao.refereeresource.listener.AddCommentListener;
import zhang.zhentao.refereeresource.listener.GetCommentListListener;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/15.
 */

public class CommentService {
    private GetCommentListListener listListener;
    private AddCommentListener commentListener;

    public void setListListener(GetCommentListListener listListener){
        this.listListener = listListener;
    }
    public void setCommentListener(AddCommentListener listener){
        this.commentListener = listener;
    }
    public void getComment(int postId){
        Map<String,String> map = new HashMap<>();
        map.put("postId",postId+"");
        String url = "comment/getCommentAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("CommentService",response);
                if(response.equals("\"nodata\""))
                    listListener.onSuccess(100,null);
                else
                    listListener.onSuccess(100,jsonToCommentList(response));
            }

            @Override
            public void onError(Exception e) {
                Log.d("CommentService",e.getMessage());
                listListener.onError(101,"网络错误");
            }
        });
    }
    public void addComment(Comment comment){
        Map<String,String> map = new HashMap<>();
        map.put("postId",comment.getPostId()+"");
        map.put("userId",comment.getUserId()+"");
        map.put("content",comment.getContent());
        map.put("createTime",comment.getCreateTime().getTime()+"");
        String url = "comment/addCommentAPI";
        HttpURLConnectionUtil.sendRequest(url, map, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("CommentService",response);
                if(response.equals("\"success\""))commentListener.onSuccess(100,"发布成功");
                else commentListener.onError(101,"发布失败");
            }

            @Override
            public void onError(Exception e) {
                Log.d("CommentService",e.getMessage());
                commentListener.onError(102,"网络错误");
            }
        });
    }
    private List<Comment> jsonToCommentList(String json){
        List<Comment> list = new ArrayList<>();
        try {
            json=json.substring(1,json.length()-1);
            JSONArray jsonArray = new JSONArray(json);
            int index = 0;
            while (index < jsonArray.length()){
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                Comment comment = new Comment();
                comment.setIsDelete(false);
                comment.setCreateTime(new Date(jsonObject.getLong("createTime")));
                comment.setUserId(jsonObject.getInt("userId"));
                comment.setCommentId(jsonObject.getInt("commentId"));
                comment.setContent(jsonObject.getString("content"));
                comment.setPostId(jsonObject.getInt("postId"));
                comment.setUserName(jsonObject.getString("userName"));
                list.add(comment);
                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
