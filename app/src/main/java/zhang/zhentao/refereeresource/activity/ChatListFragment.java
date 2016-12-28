package zhang.zhentao.refereeresource.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.service.LoginService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class ChatListFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button btnFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        view = inflater.inflate(R.layout.fragment_chat,viewGroup,false);
        btnFriend = (Button)view.findViewById(R.id.btn_fragment_chat_friend);
        btnFriend.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fragment_chat_friend:
                startActivity(new Intent(ContextUtil.getInstance(),MyFriendActivity.class));
                break;
        }
    }
}
