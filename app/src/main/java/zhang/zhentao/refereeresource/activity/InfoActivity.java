package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/14.
 */

public class InfoActivity extends Activity {

    private TextView tvNickName;
    private TextView tvRealName;
    private TextView tvGender;
    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvNote;
    private EditText etRealName;
    private EditText etHeight;
    private EditText etWeight;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etNote;
    private RadioGroup rgGender;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_info);
        tvNickName = (TextView)findViewById(R.id.tv_activity_info_nickname);
        tvRealName = (TextView)findViewById(R.id.tv_activity_info_realname);
        tvGender = (TextView)findViewById(R.id.tv_activity_info_gender);
        tvHeight = (TextView)findViewById(R.id.tv_activity_info_height);
        tvWeight = (TextView)findViewById(R.id.tv_activity_info_weight);
        tvEmail = (TextView)findViewById(R.id.tv_activity_info_email);
        tvPhone = (TextView)findViewById(R.id.tv_activity_info_phone);
        tvAddress = (TextView)findViewById(R.id.tv_activity_info_address);
        tvNote = (TextView)findViewById(R.id.tv_activity_info_note);
        tvNickName.setText(ContextUtil.getUserInstance().getNickName());
        tvRealName.setText(ContextUtil.getUserInstance().getRealName());
        tvGender.setText(ContextUtil.getUserInstance().getGender());
        tvHeight.setText(ContextUtil.getUserInstance().getHeight().toString());
        tvWeight.setText(ContextUtil.getUserInstance().getWeight().toString());
        tvEmail.setText(ContextUtil.getUserInstance().getEmail());
        tvPhone.setText(ContextUtil.getUserInstance().getPhone());
        tvAddress.setText(ContextUtil.getUserInstance().getAddress());
        tvNote.setText(ContextUtil.getUserInstance().getNote());
    }
}
