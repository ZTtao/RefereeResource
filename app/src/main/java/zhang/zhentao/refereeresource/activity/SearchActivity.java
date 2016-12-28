package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.adapter.FriendListAdapter;
import zhang.zhentao.refereeresource.entity.FriendListItem;
import zhang.zhentao.refereeresource.listener.SearchFriendListener;
import zhang.zhentao.refereeresource.service.UserService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public class SearchActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private EditText etSearch;
    private Button btnSearch;
    private ListView lvSearch;
    private FriendListAdapter adapter;
    private List<FriendListItem> list;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        etSearch = (EditText)findViewById(R.id.et_activity_search);
        btnSearch = (Button)findViewById(R.id.btn_activity_search);
        btnSearch.setOnClickListener(this);
        lvSearch = (ListView)findViewById(R.id.lv_activity_search);
        lvSearch.setOnItemClickListener(this);
        list = new ArrayList<>();
        adapter = new FriendListAdapter(SearchActivity.this,R.layout.item_friend_list,list);
        lvSearch.setAdapter(adapter);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(SearchActivity.this,"搜索成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102:
                        Toast.makeText(SearchActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_search:
                String str = etSearch.getText().toString().trim();
                if(str.equals("")){
                    Toast.makeText(SearchActivity.this,"请输入搜索条件",Toast.LENGTH_SHORT).show();
                }else {
                    UserService userService = new UserService();
                    userService.setSearchFriendListener(new SearchFriendListener() {
                        @Override
                        public void onSuccess(int errorCode, final List<FriendListItem> result) {
                            Log.d("SearchActivity",errorCode+"");
                            if(errorCode == 100){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        if(result != null)
                                            list.addAll(result);
                                        adapter.notifyDataSetChanged();
                                        handler.sendEmptyMessage(0x100);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            Log.d("SearchActivity",result);
                            handler.sendEmptyMessage(0x102);
                        }
                    });
                    userService.searchFriend(str);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lv_activity_search:
                if(list.get(position).getId() == ContextUtil.getUserInstance().getUserId()){
                    startActivity(new Intent(SearchActivity.this,InfoActivity.class));
                }else{
                    Intent intent = new Intent(SearchActivity.this,FriendInfoActivity.class);
                    intent.putExtra("userId",list.get(position).getId());
                    startActivity(intent);
                }
                break;
        }
    }
}
