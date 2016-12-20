package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.listener.UpdateRefereeInfoListener;
import zhang.zhentao.refereeresource.service.RefereeService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/17.
 */

public class RefereeActivity extends Activity implements View.OnClickListener{

    private Referee referee;
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
    private TextView tvRank;
    private EditText etRank;
    private TextView tvNote;
    private EditText etNote;
    private boolean isReferee;
    private int year;
    private int month;
    private int day;
    private Handler handler;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_referee_info);
        btnAdd = (Button)findViewById(R.id.btn_activity_referee_info_add);
        linearLayout = (LinearLayout)findViewById(R.id.ly_activity_referee);
        tvStartTime = (TextView)findViewById(R.id.tv_activity_referee_start_time);
        dpStartTime = (DatePicker)findViewById(R.id.dp_activity_referee_start_time);
        tvExperience = (TextView)findViewById(R.id.tv_activity_referee_experience);
        etExperience = (EditText)findViewById(R.id.et_activity_referee_experience);
        tvHonor = (TextView)findViewById(R.id.tv_activity_referee_honor);
        etHonor = (EditText)findViewById(R.id.et_activity_referee_honor);
        tvRank = (TextView)findViewById(R.id.tv_activity_referee_rank);
        etRank = (EditText)findViewById(R.id.et_activity_referee_rank);
        tvNote = (TextView)findViewById(R.id.tv_activity_referee_note);
        etNote = (EditText)findViewById(R.id.et_activity_referee_note);
        btnSave = (Button)findViewById(R.id.btn_activity_referee_info_save);
        btnCancel = (Button)findViewById(R.id.btn_activity_referee_info_cancel);
        btnEdit = (Button)findViewById(R.id.btn_activity_referee_info_edit);
        btnAdd.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        referee = ContextUtil.getRefereeInstance();
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

        if(referee == null){
            isReferee = false;
            btnAdd.setVisibility(View.VISIBLE);
        }else{
            isReferee = true;
            updateData();
            changeVisible(View.VISIBLE,View.GONE);
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        Toast.makeText(RefereeActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                        referee = ContextUtil.getRefereeInstance();
                        updateData();
                        changeVisible(View.VISIBLE,View.GONE);
                        break;
                    case 0x101:
                        Toast.makeText(RefereeActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102:
                        Toast.makeText(RefereeActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }
    private void updateData(){
        tvStartTime.setText(referee.getStartJudgeTime().toString());
        tvExperience.setText(referee.getExperience());
        tvHonor.setText(referee.getHonor());
        tvRank.setText(referee.getCertificate());
        tvNote.setText(referee.getNote());
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
        tvRank.setVisibility(tvFlag);
        etRank.setVisibility(etFlag);
        tvNote.setVisibility(tvFlag);
        etNote.setVisibility(etFlag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_referee_info_add:
                changeVisible(View.GONE,View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_activity_referee_info_save:
                Calendar calendar = Calendar.getInstance();
                if(ContextUtil.getRefereeInstance() != null){
                    calendar.setTime(ContextUtil.getRefereeInstance().getStartJudgeTime());
                }
                if(ContextUtil.getRefereeInstance() != null
                        && etExperience.getText().toString().equals(ContextUtil.getRefereeInstance().getExperience())
                        && etRank.getText().toString().equals(ContextUtil.getRefereeInstance().getCertificate())
                        && etHonor.getText().toString().equals(ContextUtil.getRefereeInstance().getHonor())
                        && etNote.getText().toString().equals(ContextUtil.getRefereeInstance().getNote())
                        && calendar.get(Calendar.YEAR)==year
                        && calendar.get(Calendar.MONTH)==month
                        && calendar.get(Calendar.DAY_OF_MONTH)==day){
                    Toast.makeText(RefereeActivity.this,"未修改",Toast.LENGTH_SHORT).show();
                    changeVisible(View.VISIBLE,View.GONE);
                }else{
                    final Referee referee = new Referee();
                    Calendar cal = Calendar.getInstance();
                    cal.set(year,month,day);
                    referee.setStartJudgeTime(cal.getTime());
                    referee.setNote(etNote.getText().toString());
                    referee.setCertificate(etRank.getText().toString());
                    referee.setExperience(etExperience.getText().toString());
                    referee.setHonor(etHonor.getText().toString());
                    referee.setRegisterTime(new Date());
                    referee.setUserId(ContextUtil.getUserInstance().getUserId());
                    RefereeService refereeService = new RefereeService();
                    refereeService.setUpdateRefereeInfoListener(new UpdateRefereeInfoListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            ContextUtil.setRefereeInstance(referee);
                            handler.sendEmptyMessage(0x100);
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            handler.sendEmptyMessage(errorCode);
                        }
                    });
                    refereeService.addOrUpdateRefereeInfo(referee);
                }
                break;
            case R.id.btn_activity_referee_info_cancel:
                if(!isReferee){
                    linearLayout.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);
                }else{
                    changeVisible(View.VISIBLE,View.GONE);
                }
                break;
            case R.id.btn_activity_referee_info_edit:
                changeVisible(View.GONE,View.VISIBLE);
                etNote.setText(ContextUtil.getRefereeInstance().getNote());
                etHonor.setText(ContextUtil.getRefereeInstance().getHonor());
                etRank.setText(ContextUtil.getRefereeInstance().getCertificate());
                etExperience.setText(ContextUtil.getRefereeInstance().getExperience());
                Calendar calendar1 = Calendar.getInstance();
                Date date = ContextUtil.getRefereeInstance().getStartJudgeTime();
                calendar1.setTime(date);
                dpStartTime.updateDate(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH));
                break;
        }

    }
}