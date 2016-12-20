package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.Player;
import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.listener.UpdatePlayerInfoListener;
import zhang.zhentao.refereeresource.service.PlayerService;
import zhang.zhentao.refereeresource.service.RefereeService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/20.
 */

public class PlayerActivity extends Activity implements View.OnClickListener{
    private Player player;
    private Button btnAdd;
    private Button btnSave;
    private Button btnCancel;
    private Button btnEdit;
    private LinearLayout linearLayout;
    private TextView tvStartTime;
    private DatePicker dpStartTime;
    private TextView tvExperience;
    private EditText etExperience;
    private TextView tvHonor;
    private EditText etHonor;
    private TextView tvTeam;
    private EditText etTeam;
    private TextView tvNote;
    private EditText etNote;
    private boolean isPlayer;
    private int year;
    private int month;
    private int day;
    private Handler handler;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_player_info);
        btnAdd = (Button)findViewById(R.id.btn_activity_player_info_add);
        linearLayout = (LinearLayout)findViewById(R.id.ly_activity_player);
        tvStartTime = (TextView)findViewById(R.id.tv_activity_player_start_time);
        dpStartTime = (DatePicker)findViewById(R.id.dp_activity_player_start_time);
        tvExperience = (TextView)findViewById(R.id.tv_activity_player_experience);
        etExperience = (EditText)findViewById(R.id.et_activity_player_experience);
        tvHonor = (TextView)findViewById(R.id.tv_activity_player_honor);
        etHonor = (EditText)findViewById(R.id.et_activity_player_honor);
        tvTeam = (TextView)findViewById(R.id.tv_activity_player_team);
        etTeam = (EditText)findViewById(R.id.et_activity_player_team);
        tvNote = (TextView)findViewById(R.id.tv_activity_player_note);
        etNote = (EditText)findViewById(R.id.et_activity_player_note);
        btnSave = (Button)findViewById(R.id.btn_activity_player_info_save);
        btnCancel = (Button)findViewById(R.id.btn_activity_player_info_cancel);
        btnEdit = (Button)findViewById(R.id.btn_activity_player_info_edit);
        btnAdd.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        player = ContextUtil.getPlayerInstance();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        dpStartTime.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int y, int m, int d) {
                year = y;
                month = m;
                day = d;
            }
        });

        if(player == null){
            isPlayer = false;
            btnAdd.setVisibility(View.VISIBLE);
        }else{
            isPlayer = true;
            updateData();
            changeVisible(View.VISIBLE,View.GONE);
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(PlayerActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                        player = ContextUtil.getPlayerInstance();
                        updateData();
                        changeVisible(View.VISIBLE,View.GONE);
                        break;
                    case 0x101:
                        Toast.makeText(PlayerActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102:
                        Toast.makeText(PlayerActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }
    private void updateData(){
        tvStartTime.setText(player.getStartPlayTime().toString());
        tvExperience.setText(player.getExperience());
        tvHonor.setText(player.getHonor());
        tvTeam.setText(player.getTeam());
        tvNote.setText(player.getNote());
    }
    private void changeVisible(int tvFlag,int etFlag){
        linearLayout.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(tvFlag);
        btnSave.setVisibility(etFlag);
        btnCancel.setVisibility(etFlag);
        tvStartTime.setVisibility(tvFlag);
        dpStartTime.setVisibility(etFlag);
        tvExperience.setVisibility(tvFlag);
        etExperience.setVisibility(etFlag);
        tvHonor.setVisibility(tvFlag);
        etHonor.setVisibility(etFlag);
        tvTeam.setVisibility(tvFlag);
        etTeam.setVisibility(etFlag);
        tvNote.setVisibility(tvFlag);
        etNote.setVisibility(etFlag);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_player_info_add:
                changeVisible(View.GONE,View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_activity_player_info_save:
                Calendar calendar = Calendar.getInstance();
                if(ContextUtil.getPlayerInstance() != null){
                    calendar.setTime(ContextUtil.getPlayerInstance().getStartPlayTime());
                }
                if(ContextUtil.getPlayerInstance() != null
                        && etExperience.getText().toString().equals(ContextUtil.getPlayerInstance().getExperience())
                        && etTeam.getText().toString().equals(ContextUtil.getPlayerInstance().getTeam())
                        && etHonor.getText().toString().equals(ContextUtil.getPlayerInstance().getHonor())
                        && etNote.getText().toString().equals(ContextUtil.getPlayerInstance().getNote())
                        && calendar.get(Calendar.YEAR)==year
                        && calendar.get(Calendar.MONTH)==month
                        && calendar.get(Calendar.DAY_OF_MONTH)==day){
                    Toast.makeText(PlayerActivity.this,"未修改",Toast.LENGTH_SHORT).show();
                    changeVisible(View.VISIBLE,View.GONE);
                }else{
                    final Player player = new Player();
                    Calendar cal = Calendar.getInstance();
                    cal.set(year,month,day);
                    player.setStartPlayTime(cal.getTime());
                    player.setNote(etNote.getText().toString());
                    player.setTeam(etTeam.getText().toString());
                    player.setExperience(etExperience.getText().toString());
                    player.setHonor(etHonor.getText().toString());
                    player.setRegisterTime(new Date());
                    player.setUserId(ContextUtil.getUserInstance().getUserId());
                    PlayerService playerService = new PlayerService();
                    playerService.setUpdatePlayerInfoListener(new UpdatePlayerInfoListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            ContextUtil.setPlayerInstance(player);
                            isPlayer = true;
                            handler.sendEmptyMessage(0x100);
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            handler.sendEmptyMessage(errorCode);
                        }
                    });
                    playerService.addOrUpdatePlayerInfo(player);
                }
                break;
            case R.id.btn_activity_player_info_cancel:
                if(!isPlayer){
                    linearLayout.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);
                }else{
                    changeVisible(View.VISIBLE,View.GONE);
                }
                break;
            case R.id.btn_activity_player_info_edit:
                changeVisible(View.GONE,View.VISIBLE);
                etNote.setText(ContextUtil.getPlayerInstance().getNote());
                etHonor.setText(ContextUtil.getPlayerInstance().getHonor());
                etTeam.setText(ContextUtil.getPlayerInstance().getTeam());
                etExperience.setText(ContextUtil.getPlayerInstance().getExperience());
                Calendar calendar1 = Calendar.getInstance();
                Date date = ContextUtil.getPlayerInstance().getStartPlayTime();
                calendar1.setTime(date);
                dpStartTime.updateDate(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
}
