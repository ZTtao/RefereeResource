package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.service.LoginService;
import zhang.zhentao.refereeresource.util.ContextUtil;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button btnPost;
    private Button btnChat;
    private Button btnInfo;
    private PostListFragment postListFragment;
    private ChatListFragment chatListFragment;
    private InfoFragment infoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPost = (Button)findViewById(R.id.mainpage_post_btn);
        btnChat = (Button)findViewById(R.id.mainpage_chat_btn);
        btnInfo = (Button)findViewById(R.id.mainpage_info_btn);
        btnPost.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        LoginService loginService = new LoginService();
        loginService.loginIM(ContextUtil.getUserInstance().getNickName());
        setDefaultFragment();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    private void setDefaultFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        postListFragment = new PostListFragment();
        transaction.replace(R.id.fragment_main,postListFragment);
        transaction.commit();
    }
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.mainpage_post_btn:
                if(postListFragment == null){
                    postListFragment = new PostListFragment();
                }
                transaction.replace(R.id.fragment_main,postListFragment);
                break;
            case R.id.mainpage_chat_btn:
                if(chatListFragment == null){
                    chatListFragment = new ChatListFragment();
                }
                transaction.replace(R.id.fragment_main,chatListFragment);
                break;
            case R.id.mainpage_info_btn:
                if(infoFragment == null){
                    infoFragment = new InfoFragment();
                }
                transaction.replace(R.id.fragment_main,infoFragment);
                break;
        }
        transaction.commit();
    }
}
