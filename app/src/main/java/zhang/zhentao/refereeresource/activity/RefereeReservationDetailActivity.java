package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.RefereeReservation;
import zhang.zhentao.refereeresource.listener.HasReplyListener;
import zhang.zhentao.refereeresource.listener.UpdateReplyListener;
import zhang.zhentao.refereeresource.service.RefereeService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/24.
 */

public class RefereeReservationDetailActivity extends Activity implements View.OnClickListener{
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvState;
    private TextView tvCount;
    private TextView tvRequire;
    private TextView tvReward;
    private Button btnReply;
    private RefereeReservation refereeReservation;
    private Handler handler;
    private boolean hasReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee_reservation_detail);
        refereeReservation = (RefereeReservation)getIntent().getSerializableExtra("reservation");
        tvTime = (TextView)findViewById(R.id.tv_activity_referee_reservation_detail_time);
        tvAddress = (TextView)findViewById(R.id.tv_activity_referee_reservation_detail_address);
        tvState = (TextView)findViewById(R.id.tv_activity_referee_reservation_detail_state);
        tvCount = (TextView)findViewById(R.id.tv_activity_referee_reservation_detail_count);
        tvRequire = (TextView)findViewById(R.id.tv_activity_referee_reservation_detail_require);
        tvReward = (TextView)findViewById(R.id.tv_activity_referee_reservation_detail_reward);
        btnReply = (Button)findViewById(R.id.btn_activity_referee_reservation_reply);
        btnReply.setOnClickListener(this);
        tvTime.setText(refereeReservation.getGameTime().toString());
        tvAddress.setText(refereeReservation.getAddress());
        tvState.setText(refereeReservation.getGameState()==null?"":refereeReservation.getGameState());
        tvCount.setText(refereeReservation.getRefereeCount()==null?"":refereeReservation.getRefereeCount());
        tvRequire.setText(refereeReservation.getRefereeRequire()==null?"":refereeReservation.getRefereeRequire());
        tvReward.setText(refereeReservation.getReward()==null?"":refereeReservation.getReward());

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:
                        hasReply = true;
                        btnReply.setText("cancel");
                        btnReply.setVisibility(View.VISIBLE);
                        break;
                    case 0x101:
                        hasReply = false;
                        btnReply.setText("reply");
                        btnReply.setVisibility(View.VISIBLE);
                        break;
                    case 0x102:
                        Toast.makeText(RefereeReservationDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x103:
                        Toast.makeText(RefereeReservationDetailActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
                        if(hasReply == true){
                            hasReply = false;
                            btnReply.setText("reply");
                        }
                        else {
                            hasReply = true;
                            btnReply.setText("cancel");
                        }
                        btnReply.setVisibility(View.VISIBLE);
                        break;
                    case 0x104:
                        Toast.makeText(RefereeReservationDetailActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                        btnReply.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        RefereeService refereeService = new RefereeService();
        refereeService.setHasReplyListener(new HasReplyListener() {
            @Override
            public void onSuccess(int errorCode, String result) {
                Log.d("RefereeRDetailActivity",result);
                if(errorCode == 100)
                    handler.sendEmptyMessage(0x100);
                else handler.sendEmptyMessage(0x101);
            }

            @Override
            public void onError(int errorCode, String result) {
                Log.d("RefereeRDetailActivity",result);
                handler.sendEmptyMessage(0x102);
            }
        });
        if (ContextUtil.getRefereeInstance() != null)
            refereeService.hasReply(ContextUtil.getRefereeInstance().getRefereeId(),refereeReservation.getRefResId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_referee_reservation_reply:
                if(ContextUtil.getRefereeInstance() != null){
                btnReply.setVisibility(View.GONE);
                RefereeService refereeService = new RefereeService();
                refereeService.setUpdateReplyListener(new UpdateReplyListener() {
                    @Override
                    public void onSuccess(int errorCode, String result) {
                        Log.d("RefereeRDetailActivity",result);
                        if(errorCode == 100){
                            handler.sendEmptyMessage(0x103);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String result) {
                        Log.d("RefereeRDetailActivity",result);
                        if (errorCode == 101)handler.sendEmptyMessage(0x104);
                        else handler.sendEmptyMessage(0x102);
                    }
                });
                    refereeService.updateReply(ContextUtil.getRefereeInstance().getRefereeId(),refereeReservation.getRefResId(),hasReply);
                }else Toast.makeText(RefereeReservationDetailActivity.this,"not a referee",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
