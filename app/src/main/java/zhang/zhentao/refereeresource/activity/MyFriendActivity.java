package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zhang.zhentao.refereeresource.R;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public class MyFriendActivity extends Activity implements View.OnClickListener{
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        btnSearch = (Button)findViewById(R.id.btn_activity_my_friend_search);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_my_friend_search:
                startActivity(new Intent(MyFriendActivity.this,SearchActivity.class));
                break;
        }
    }
}
