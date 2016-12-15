package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.icu.text.IDNA;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;
import java.util.Date;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.InfoListener;
import zhang.zhentao.refereeresource.service.InfoService;
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
    private Handler handler;
    private User user;
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
        updateData();
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
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(InfoActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                        ContextUtil.setUserInstance(user);
                        updateData();
                        break;
                    case 0x101:
                        Toast.makeText(InfoActivity.this,"更新失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
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
                        InfoService infoService = new InfoService();
                        infoService.setListener(new InfoListener() {
                            @Override
                            public void onSuccess(int errorCode, String result) {
                                Log.d("InfoActivity",result);
                                handler.sendEmptyMessage(0x100);
                            }

                            @Override
                            public void onError(int errorCode, String result) {
                                Log.d("InfoActivity",result);
                                handler.sendEmptyMessage(0x101);
                            }
                        });
                        user = new User();
                        user.setNickName(ContextUtil.getUserInstance().getNickName());
                        user.setRealName(etRealName.getText().toString());
                        user.setHeight(Float.parseFloat(etHeight.getText().toString().equals("")?"0":etHeight.getText().toString()));
                        user.setWeight(Float.parseFloat(etWeight.getText().toString().equals("")?"0":etWeight.getText().toString()));
                        user.setEmail(etEmail.getText().toString());
                        user.setPhone(etPhone.getText().toString());
                        user.setAddress(etAddress.getText().toString());
                        user.setNote(etNote.getText().toString());
                        switch (rgGender.getCheckedRadioButtonId()) {
                            case R.id.rb_activity_info_male:
                                user.setGender("男");
                                break;
                            case R.id.rb_activity_info_female:
                                user.setGender("女");
                                break;
                        }
                        infoService.updateInfo(user);
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
    private void updateData(){
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
