package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/14.
 */

public class InfoActivity extends Activity implements View.OnClickListener{

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
    private Button btnEdit;
    private Button btnCancel;
    private RadioButton rbMale;
    private RadioButton rbFemale;
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
        etRealName = (EditText)findViewById(R.id.et_activity_info_realname);
        etHeight = (EditText)findViewById(R.id.et_activity_info_height);
        etWeight = (EditText)findViewById(R.id.et_activity_info_weight);
        etEmail = (EditText)findViewById(R.id.et_activity_info_email);
        etPhone = (EditText)findViewById(R.id.et_activity_info_phone);
        etAddress = (EditText)findViewById(R.id.et_activity_info_address);
        etNote = (EditText)findViewById(R.id.et_activity_info_note);
        rgGender = (RadioGroup)findViewById(R.id.rd_activity_info_gender);
        rbFemale = (RadioButton)findViewById(R.id.rb_activity_info_female);
        rbMale = (RadioButton)findViewById(R.id.rb_activity_info_male);
        btnEdit = (Button)findViewById(R.id.btn_activity_info_edit);
        btnEdit.setOnClickListener(this);
        btnCancel = (Button)findViewById(R.id.btn_activity_info_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_info_edit:
                if(tvRealName.getVisibility() == View.VISIBLE){
                    //修改
                    changeVisible(View.GONE, View.VISIBLE);
                    etRealName.setText(ContextUtil.getUserInstance().getRealName());
                    etHeight.setText(ContextUtil.getUserInstance().getHeight().toString());
                    etWeight.setText(ContextUtil.getUserInstance().getWeight().toString());
                    etEmail.setText(ContextUtil.getUserInstance().getEmail());
                    etPhone.setText(ContextUtil.getUserInstance().getPhone());
                    etAddress.setText(ContextUtil.getUserInstance().getAddress());
                    etNote.setText(ContextUtil.getUserInstance().getNote());
                    if(ContextUtil.getUserInstance().getGender().equals("男"))
                        rbMale.setChecked(true);
                    else rbFemale.setChecked(true);
                } else {
                    //保存
                    changeVisible(View.VISIBLE, View.GONE);
                    if(etRealName.getText().toString().equals(ContextUtil.getUserInstance().getRealName())
                            && etHeight.getText().toString().equals(ContextUtil.getUserInstance().getHeight().toString())
                            && etWeight.getText().toString().equals(ContextUtil.getUserInstance().getWeight().toString())
                            && etEmail.getText().toString().equals(ContextUtil.getUserInstance().getEmail())
                            && etPhone.getText().toString().equals(ContextUtil.getUserInstance().getPhone())
                            && etAddress.getText().toString().equals(ContextUtil.getUserInstance().getAddress())
                            && etNote.getText().toString().equals(ContextUtil.getUserInstance().getNote())
                            && ((RadioButton)findViewById(rgGender.getCheckedRadioButtonId())).getText().equals(ContextUtil.getUserInstance().getGender())){
                        //未修改
                        Toast.makeText(InfoActivity.this,"未修改",Toast.LENGTH_SHORT).show();
                    }else {
                        //已修改

                    }
                }
                break;
            case R.id.btn_activity_info_cancel:
                changeVisible(View.VISIBLE,View.GONE);
                break;
        }
    }
    private void changeVisible(int tvFlag,int etFlag){
        tvRealName.setVisibility(tvFlag);
        tvGender.setVisibility(tvFlag);
        tvHeight.setVisibility(tvFlag);
        tvWeight.setVisibility(tvFlag);
        tvEmail.setVisibility(tvFlag);
        tvPhone.setVisibility(tvFlag);
        tvAddress.setVisibility(tvFlag);
        tvNote.setVisibility(tvFlag);

        etRealName.setVisibility(etFlag);
        etHeight.setVisibility(etFlag);
        etWeight.setVisibility(etFlag);
        etEmail.setVisibility(etFlag);
        etPhone.setVisibility(etFlag);
        etAddress.setVisibility(etFlag);
        etNote.setVisibility(etFlag);
        rgGender.setVisibility(etFlag);
        btnCancel.setVisibility(etFlag);

        if(tvFlag == View.VISIBLE){
            btnEdit.setText("修改");
        }else btnEdit.setText("保存");
    }
}
