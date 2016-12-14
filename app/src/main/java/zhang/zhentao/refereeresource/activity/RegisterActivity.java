package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.HttpCallbackListener;
import zhang.zhentao.refereeresource.listener.RegisterListener;
import zhang.zhentao.refereeresource.service.RegisterService;
import zhang.zhentao.refereeresource.util.ContextUtil;
import zhang.zhentao.refereeresource.util.HttpURLConnectionUtil;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText etNickName;
    private EditText etRealName;
    private EditText etPassword;
    private EditText etPassword1;
    private EditText etHeight;
    private EditText etWeight;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etNote;
    private Button btnRegister;
    private RadioGroup rdGender;
    private Handler handler;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_register);
        etNickName = (EditText)findViewById(R.id.et_register_nickname);
        etRealName = (EditText)findViewById(R.id.et_register_realname);
        etPassword = (EditText)findViewById(R.id.et_register_password);
        etPassword1 = (EditText)findViewById(R.id.et_register_password1);
        etHeight = (EditText)findViewById(R.id.et_register_height);
        etWeight = (EditText)findViewById(R.id.et_register_weight);
        etEmail = (EditText)findViewById(R.id.et_register_email);
        etPhone = (EditText)findViewById(R.id.et_register_phone);
        etAddress = (EditText)findViewById(R.id.et_register_address);
        etNote = (EditText)findViewById(R.id.et_register_note);
        btnRegister = (Button)findViewById(R.id.btn_register_register);
        btnRegister.setOnClickListener(this);
        rdGender = (RadioGroup)findViewById(R.id.rd_register_gender);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x101:
                        Toast.makeText(RegisterActivity.this,"昵称已存在",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x104:
                        Toast.makeText(RegisterActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register_register:
                if(checkInfo()) {
                    final User user = new User();
                    user.setNickName(etNickName.getText().toString());
                    user.setRealName(etRealName.getText().toString());
                    user.setPassword(etPassword.getText().toString());
                    user.setHeight(Float.parseFloat(etHeight.getText().toString()));
                    user.setWeight(Float.parseFloat(etWeight.getText().toString()));
                    user.setEmail(etEmail.getText().toString());
                    user.setPhone(etPhone.getText().toString());
                    user.setAddress(etAddress.getText().toString());
                    user.setNote(etNote.getText().toString());
                    user.setRegisterTime(new Date());
                    user.setIsDelete(false);
                    switch (rdGender.getCheckedRadioButtonId()) {
                        case R.id.rb_register_male:
                            user.setGender("男");
                            break;
                        case R.id.rb_register_female:
                            user.setGender("女");
                            break;
                    }
                    RegisterService registerService = new RegisterService();
                    registerService.setListener(new RegisterListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            Log.d("RegisterActivity", result);
                            //注册成功
                            ContextUtil.setUserInstance(user);
                            handler.sendEmptyMessage(0x100);
                            finish();
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            Log.d("RegisterActivity", result);
                            //注册失败
                            if(errorCode == 101)
                                handler.sendEmptyMessage(0x101);
                            else if(errorCode == 104)
                                handler.sendEmptyMessage(0x104);
                        }
                    });
                    registerService.addUser(user);
                }
                break;
        }
    }
    @Override
    public void finish(){
        if(ContextUtil.getUserInstance() != null)
            setResult(RESULT_OK);
        else setResult(RESULT_CANCELED);
        super.finish();
    }
    private boolean checkInfo(){
        String nickName = etNickName.getText().toString().trim();
        String password1 = etPassword.getText().toString().trim();
        String password2 = etPassword1.getText().toString().trim();
        if(nickName == null || nickName.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入昵称",Toast.LENGTH_SHORT).show();
            return false;
        }else if(password1 == null || password2 == null || password1.equals("") || password2.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!password1.equals(password2)){
            Toast.makeText(RegisterActivity.this,"两次密码不匹配",Toast.LENGTH_SHORT).show();
            return false;
        }else if(password1.length()>20 || password1.length()<6){
            Toast.makeText(RegisterActivity.this,"密码长度:6-20",Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }
}
