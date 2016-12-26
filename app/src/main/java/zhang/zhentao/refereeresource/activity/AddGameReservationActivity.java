package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.GameReservation;
import zhang.zhentao.refereeresource.listener.AddGameReservationListener;
import zhang.zhentao.refereeresource.listener.AddRefereeReservationListener;
import zhang.zhentao.refereeresource.service.PlayerService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/26.
 */

public class AddGameReservationActivity extends Activity implements View.OnClickListener{
    private DatePicker dpDate;
    private TimePicker tpTime;
    private EditText etAddress;
    private EditText etTeam;
    private EditText etLevel;
    private Button btnAdd;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_reservation);
        dpDate = (DatePicker)findViewById(R.id.dp_activity_add_game_reservation);
        tpTime = (TimePicker)findViewById(R.id.tp_activity_add_game_reservation);
        etAddress = (EditText)findViewById(R.id.et_activity_add_game_reservation_address);
        etTeam = (EditText)findViewById(R.id.et_activity_add_game_reservation_team);
        etLevel = (EditText)findViewById(R.id.et_activity_add_game_reservation_level);
        btnAdd = (Button)findViewById(R.id.btn_activity_add_game_reservation_add);
        btnAdd.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        dpDate.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int y, int m, int d) {
                year = y;
                month = m;
                day = d;
            }
        });
        tpTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int h, int m) {
                hour = h;
                minute = m;
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(AddGameReservationActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case 0x101:
                        Toast.makeText(AddGameReservationActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102:
                        Toast.makeText(AddGameReservationActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_add_game_reservation_add:
                String address = etAddress.getText().toString();
                String team = etTeam.getText().toString();
                String level = etLevel.getText().toString();
                StringBuilder builder = new StringBuilder();
                builder.append(year).append("-").append(month).append("-").append(day).append(" ")
                        .append(hour).append(":").append(minute);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day,hour,minute);
                if(address.trim().equals("")){
                    Toast.makeText(AddGameReservationActivity.this,"请填写地址",Toast.LENGTH_SHORT).show();
                }else{
                    GameReservation gameReservation = new GameReservation();
                    gameReservation.setAddress(address);
                    gameReservation.setGameTime(calendar.getTime());
                    gameReservation.setTeam(team);
                    gameReservation.setLevel(level);
                    if (ContextUtil.getPlayerInstance() != null)
                        gameReservation.setPlayerId(ContextUtil.getPlayerInstance().getPlayerId());
                    gameReservation.setCreateTime(new Date());
                    PlayerService playerService = new PlayerService();
                    playerService.setAddGameReservationListener(new AddGameReservationListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            Log.d("AddGameRActivity",result);
                            if(errorCode == 100)handler.sendEmptyMessage(0x100);
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            Log.d("AddGameRActivity",result);
                            if(errorCode == 101)handler.sendEmptyMessage(0x101);
                            else if(errorCode == 102)handler.sendEmptyMessage(0x102);
                        }
                    });
                    playerService.addGameReservation(gameReservation);
                }
                break;
        }
    }
}
