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
import zhang.zhentao.refereeresource.entity.GameReservation;
import zhang.zhentao.refereeresource.entity.Player;
import zhang.zhentao.refereeresource.listener.HasReplyListener;
import zhang.zhentao.refereeresource.listener.UpdateReplyListener;
import zhang.zhentao.refereeresource.service.PlayerService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/26.
 */

public class GameReservationDetailActivity extends Activity implements View.OnClickListener{
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvTeam;
    private TextView tvLevel;
    private Button btnReply;
    private GameReservation gameReservation;
    private Handler handler;
    private boolean hasReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_reservation_detail);
        gameReservation = (GameReservation)getIntent().getSerializableExtra("reservation");
        tvTime = (TextView)findViewById(R.id.tv_activity_game_reservation_detail_time);
        tvAddress = (TextView)findViewById(R.id.tv_activity_game_reservation_detail_address);
        tvTeam = (TextView)findViewById(R.id.tv_activity_game_reservation_detail_team);
        tvLevel = (TextView)findViewById(R.id.tv_activity_game_reservation_detail_level);
        btnReply = (Button)findViewById(R.id.btn_activity_game_reservation_detail_reply);
        btnReply.setOnClickListener(this);
        tvTime.setText(gameReservation.getGameTime().toString());
        tvAddress.setText(gameReservation.getAddress());
        tvTeam.setText(gameReservation.getTeam()==null?"": gameReservation.getTeam());
        tvLevel.setText(gameReservation.getLevel()==null?"": gameReservation.getLevel());

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
                        Toast.makeText(GameReservationDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x103:
                        Toast.makeText(GameReservationDetailActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GameReservationDetailActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                        btnReply.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        PlayerService refereeService = new PlayerService();
        refereeService.setHasReplyListener(new HasReplyListener() {
            @Override
            public void onSuccess(int errorCode, String result) {
                Log.d("GameRDetailActivity",result);
                if(errorCode == 100)
                    handler.sendEmptyMessage(0x100);
                else handler.sendEmptyMessage(0x101);
            }

            @Override
            public void onError(int errorCode, String result) {
                Log.d("GameRDetailActivity",result);
                handler.sendEmptyMessage(0x102);
            }
        });
        if (ContextUtil.getPlayerInstance() != null)
            refereeService.hasReply(ContextUtil.getPlayerInstance().getPlayerId(), gameReservation.getGameResId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_game_reservation_detail_reply:
                if(ContextUtil.getPlayerInstance() != null){
                    btnReply.setVisibility(View.GONE);
                    PlayerService playerService = new PlayerService();
                    playerService.setUpdateReplyListener(new UpdateReplyListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            Log.d("GameRDetailActivity",result);
                            if(errorCode == 100){
                                handler.sendEmptyMessage(0x103);
                            }
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            Log.d("GameRDetailActivity",result);
                            if (errorCode == 101)handler.sendEmptyMessage(0x104);
                            else handler.sendEmptyMessage(0x102);
                        }
                    });
                    playerService.updateReply(ContextUtil.getPlayerInstance().getPlayerId(), gameReservation.getGameResId(),hasReply);
                }else Toast.makeText(GameReservationDetailActivity.this,"not a player",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
