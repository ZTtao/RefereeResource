package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.adapter.CommentListAdapter;
import zhang.zhentao.refereeresource.entity.Comment;
import zhang.zhentao.refereeresource.entity.Post;
import zhang.zhentao.refereeresource.listener.AddCommentListener;
import zhang.zhentao.refereeresource.listener.GetCommentListListener;
import zhang.zhentao.refereeresource.service.CommentService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/15.
 */

public class PostDetailActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private EditText etComment;
    private Button btnSubmit;
    private View headerView;
    private CommentListAdapter adapter;
    private List<Comment> list;
    private TextView tvHeaderUser;
    private TextView tvHeaderTime;
    private TextView tvHeaderTitle;
    private TextView tvHeaderContent;
    private Post post;
    private Handler handler;
    private CommentService commentService;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_post_detail);
        listView = (ListView)findViewById(R.id.lv_activity_post_detail);
        etComment = (EditText)findViewById(R.id.et_activity_post_detail_comment);
        btnSubmit = (Button)findViewById(R.id.btn_activity_post_detail_submit);
        btnSubmit.setOnClickListener(this);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_view_post_detail,null);

        commentService = new CommentService();
        tvHeaderTime = (TextView)headerView.findViewById(R.id.tv_header_view_time);
        tvHeaderTitle = (TextView)headerView.findViewById(R.id.tv_header_view_title);
        tvHeaderUser = (TextView)headerView.findViewById(R.id.tv_header_view_name);
        tvHeaderContent = (TextView)headerView.findViewById(R.id.tv_header_view_content);

        listView.addHeaderView(headerView);
        list = new ArrayList<>();
        adapter = new CommentListAdapter(this,R.layout.item_comment,list);
        listView.setAdapter(adapter);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(PostDetailActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        etComment.setText("");
                        updateListData();
                        break;
                    case 0x101:
                        Toast.makeText(PostDetailActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102:
                        Toast.makeText(PostDetailActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        updateHeaderData();
        updateListData();
    }
    private void updateHeaderData(){
        Intent intent = getIntent();
        post = (Post)intent.getSerializableExtra("post");
        tvHeaderUser.setText(post.getUserName());
        tvHeaderTime.setText(post.getCreateTime().toString());
        tvHeaderTitle.setText(post.getTitle());
        tvHeaderContent.setText(post.getContent());
    }
    private void updateListData(){
        commentService.setListListener(new GetCommentListListener() {
            @Override
            public void onSuccess(int errorCode, List<Comment> result) {
                Log.d("PostDetailActivity1","success"+errorCode);
                adapter.clearList();
                adapter.listAddAll(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int errorCode, String result) {
                Log.d("PostDetailActivity1",result);
                handler.sendEmptyMessage(0x101);
            }
        });
        commentService.getComment(post.getPostId());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_post_detail_submit:
                if(etComment.getText().toString().equals(""))
                    Toast.makeText(PostDetailActivity.this,"空评论",Toast.LENGTH_SHORT).show();
                else {
                    Comment comment = new Comment();
                    comment.setPostId(post.getPostId());
                    comment.setContent(etComment.getText().toString());
                    comment.setUserId(ContextUtil.getUserInstance().getUserId());
                    comment.setCreateTime(new Date());
                    commentService.setCommentListener(new AddCommentListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            handler.sendEmptyMessage(0x100);
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            if(errorCode==101)
                            handler.sendEmptyMessage(0x102);
                            else
                                handler.sendEmptyMessage(0x101);
                        }
                    });
                    commentService.addComment(comment);
                }
                break;
        }
    }
}
