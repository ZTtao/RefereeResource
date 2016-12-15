package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.Date;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.Post;
import zhang.zhentao.refereeresource.listener.AddPostListener;
import zhang.zhentao.refereeresource.service.PostService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/15.
 */

public class AddPostActivity extends Activity implements View.OnClickListener{

    private Button btnAdd;
    private EditText etTitle;
    private EditText etContent;
    private Handler handler;
    @Override
    protected void onCreate(final Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_post);
        btnAdd = (Button)findViewById(R.id.btn_activity_add_post_add);
        etTitle = (EditText)findViewById(R.id.et_activity_add_post_title);
        etContent = (EditText)findViewById(R.id.et_activity_add_post_content);
        btnAdd.setOnClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(AddPostActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case 0x101:
                        Toast.makeText(AddPostActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102:
                        Toast.makeText(AddPostActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_add_post_add:
                if(etTitle.getText().toString().length() == 0 || etTitle.getText().toString().length() > 20){
                    Toast.makeText(AddPostActivity.this,"标题长度:1-20",Toast.LENGTH_SHORT).show();
                }else if(etContent.getText().toString().length() == 0 || etContent.getText().toString().length() > 255){
                    Toast.makeText(AddPostActivity.this,"内容长度:1-255",Toast.LENGTH_SHORT).show();
                }else {
                    Post post = new Post();
                    post.setTitle(etTitle.getText().toString());
                    post.setContent(etContent.getText().toString());
                    post.setUserId(ContextUtil.getUserInstance().getUserId());
                    post.setCreateTime(new Date());
                    post.setIsDelete(false);
                    PostService service = new PostService();
                    service.setAddPostListener(new AddPostListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            Log.d("AddPostActivity",result);
                            if(errorCode == 100)handler.sendEmptyMessage(0x100);
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            Log.d("AddPostActivity",result);
                            if(errorCode == 101)handler.sendEmptyMessage(0x101);
                            else handler.sendEmptyMessage(0x102);
                        }
                    });
                    service.addPostToServer(post);
                }
                break;
        }
    }
}
