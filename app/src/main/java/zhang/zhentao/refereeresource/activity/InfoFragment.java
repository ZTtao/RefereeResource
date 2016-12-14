package zhang.zhentao.refereeresource.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class InfoFragment extends Fragment implements View.OnClickListener{

    private LinearLayout lyInfo;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        view = inflater.inflate(R.layout.fragment_info,viewGroup,false);
        lyInfo = (LinearLayout)view.findViewById(R.id.ly_fragment_info_info);
        lyInfo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_fragment_info_info:
                Intent intent = new Intent(ContextUtil.getInstance(),InfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
