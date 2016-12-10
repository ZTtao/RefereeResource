package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.listener.LoginListener;
import zhang.zhentao.refereeresource.service.LoginService;
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
                        startActivity(intent);
                        finish();
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
                    public void onSuccess(int errorCode, String result) {
                        if(errorCode==100){
                            Log.d("LoginActivity",result);
//                            DBOpenHelper helper = new DBOpenHelper(LoginActivity.this,"db_referee",null,1);
//                            SQLiteDatabase db_referee = helper.getWritableDatabase();
//                            db_referee.execSQL("delete from user;");
//                            ContentValues values = new ContentValues();
//                            values.put("nick_name",nickName);
//                            db_referee.insert("user","nick_name",values);
                            handler.sendEmptyMessage(0x122);
                        }else{
                            Log.d("LoginActivity",result);
                            handler.sendEmptyMessage(0x123);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String result) {
                        Log.d("LoginActivity",result);
                        handler.sendEmptyMessage(0x124);
                    }
                });
                service.Login(nickName,password);
            }
                break;
            case R.id.btn_login_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
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
