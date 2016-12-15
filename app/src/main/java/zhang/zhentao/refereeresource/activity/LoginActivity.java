package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.openmob.mobileimsdk.android.ClientCoreSDK;
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.LoginListener;
import zhang.zhentao.refereeresource.service.LoginService;
import zhang.zhentao.refereeresource.util.ContextUtil;
import zhang.zhentao.refereeresource.util.DBOpenHelper;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private Handler handler;
    private String nickName;
    private String password;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        etAccount = (EditText)findViewById(R.id.et_login_account);
        etPassword = (EditText)findViewById(R.id.et_login_password);
        btnLogin = (Button)findViewById(R.id.btn_login_login);
        btnLogin.setOnClickListener(this);
        btnRegister = (Button)findViewById(R.id.btn_login_register);
        btnRegister.setOnClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x122:
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivityForResult(intent,300);
                        break;
                    case 0x123:
                        Toast.makeText(LoginActivity.this,"账户名或密码错误",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x124:
                        Toast.makeText(LoginActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_login:
                nickName = etAccount.getText().toString();
                password = etPassword.getText().toString();
                if(checkAccountAndPassword()){
                LoginService service = new LoginService();
                service.setListener(new LoginListener() {
                    @Override
                    public void onSuccess(int errorCode, User user) {
                        if(errorCode==100){     //登录成功
                            Log.d("LoginActivity","登录成功,user:"+user.getNickName());
                            ContextUtil.setUserInstance(user);
                            handler.sendEmptyMessage(0x122);
                        }else{      //账户名或密码错误
                            Log.d("LoginActivity","账户名或密码错误");
                            handler.sendEmptyMessage(0x123);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String result) {     //网络错误
                        Log.d("LoginActivity",result);
                        handler.sendEmptyMessage(0x124);
                    }
                });
                service.Login(nickName,password);
            }
                break;
            case R.id.btn_login_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,200);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        switch (requestCode){
            case 200:   //RegisterActivity传回的数据
                switch (resultCode){
                    case RESULT_OK:
                        //注册成功
                        if (ContextUtil.getUserInstance() != null){     //再次确认是否注册成功
                            Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
                            startActivityForResult(intent1,300);
                        }
                        break;
                    case RESULT_CANCELED:
                        //未注册
                        break;
                }
                break;
            case 300:   //MainActivity传回的数据
                switch (resultCode){
                    case RESULT_OK:
                        //logout
                        Log.d("LoginActivity","logout");
                        break;
                    default:
                        //直接退出程序
                        Log.d("LoginActivity","finish");
                        finish();
                        break;
                }
                break;
        }
    }
    @Override
    public void finish(){
        doLogout();
        super.finish();
        System.exit(0);
    }
    private void doLogout(){
        new AsyncTask<Object,Integer,Integer>(){
            @Override
            protected Integer doInBackground(Object... params) {
                int code = -1;
                try{
                    code = LocalUDPDataSender.getInstance(LoginActivity.this).sendLoginout();
                }catch (Exception e){
                    Log.d("LoginActivity",e.getMessage());
                }
                return code;
            }
            @Override
            protected void onPostExecute(Integer code){
                if(code == 0)
                    Log.d("LoginActivity","注销登陆成功");
                else
                    Log.d("LoginActivity","注销登陆失败："+code);
            }
        }.execute();
    }
    private boolean checkAccountAndPassword(){
        String strAccount = nickName.trim();
        String strPassword = password.trim();
        if(strAccount == null || strAccount.equals("")){
            Toast.makeText(LoginActivity.this,"请输入昵称",Toast.LENGTH_SHORT).show();
            return false;
        }else if(strPassword == null || strPassword.equals("")){
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }
}
