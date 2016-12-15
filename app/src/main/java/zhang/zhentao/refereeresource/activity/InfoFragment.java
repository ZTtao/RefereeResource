package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    private Button btnLogout;
    private Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        view = inflater.inflate(R.layout.fragment_info,viewGroup,false);
        lyInfo = (LinearLayout)view.findViewById(R.id.ly_fragment_info_info);
        lyInfo.setOnClickListener(this);
        btnLogout = (Button)view.findViewById(R.id.btn_fragment_info_logout);
        btnLogout.setOnClickListener(this);
        return view;
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_fragment_info_info:
                Intent intent = new Intent(ContextUtil.getInstance(),InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_fragment_info_logout:
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
                break;
        }
    }
}
