package zhang.zhentao.refereeresource.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.service.LoginService;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class ChatListFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        view = inflater.inflate(R.layout.fragment_chat,viewGroup,false);
        LoginService loginService = new LoginService();
        loginService.loginIM("zhentao","123");
        return view;
    }
}
