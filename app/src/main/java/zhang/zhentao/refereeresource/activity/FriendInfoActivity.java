package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.Player;
import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.entity.User;
import zhang.zhentao.refereeresource.listener.AddFriendListener;
import zhang.zhentao.refereeresource.listener.GetFriendInfoByIdListener;
import zhang.zhentao.refereeresource.service.UserService;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public class FriendInfoActivity extends Activity implements View.OnClickListener{
    private TextView tvNickName;
    private TextView tvGender;
    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvEmail;
    private TextView tvNote;

    private LinearLayout lyReferee;
    private TextView tvRefereeStartTime;
    private TextView tvRefereeExperience;
    private TextView tvRefereeHonor;
    private TextView tvRefereeRank;
    private TextView tvRefereeNote;

    private LinearLayout lyPlayer;
    private TextView tvPlayerStartTime;
    private TextView tvPlayerExperience;
    private TextView tvPlayerHonor;
    private TextView tvPlayerTeam;
    private TextView tvPlayerNote;

    private Button btnBottom;
    private Handler handler;
    private boolean isFri;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        userId = getIntent().getIntExtra("userId",-1);
        tvNickName = (TextView)findViewById(R.id.tv_activity_friend_info_nickname);
        tvGender = (TextView)findViewById(R.id.tv_activity_friend_info_gender);
        tvHeight = (TextView)findViewById(R.id.tv_activity_friend_info_height);
        tvWeight = (TextView)findViewById(R.id.tv_activity_friend_info_weight);
        tvEmail = (TextView)findViewById(R.id.tv_activity_friend_info_email);
        tvNote = (TextView)findViewById(R.id.tv_activity_friend_info_note);
        lyReferee = (LinearLayout)findViewById(R.id.ly_activity_friend_info_referee);
        tvRefereeStartTime = (TextView)findViewById(R.id.tv_activity_friend_info_referee_start_time);
        tvRefereeExperience = (TextView)findViewById(R.id.tv_activity_friend_info_referee_experience);
        tvRefereeHonor = (TextView)findViewById(R.id.tv_activity_friend_info_referee_honor);
        tvRefereeRank = (TextView)findViewById(R.id.tv_activity_friend_info_referee_rank);
        tvRefereeNote = (TextView)findViewById(R.id.tv_activity_friend_info_referee_note);
        lyPlayer = (LinearLayout)findViewById(R.id.ly_activity_friend_info_player);
        tvPlayerStartTime = (TextView)findViewById(R.id.tv_activity_friend_info_player_start_time);
        tvPlayerExperience = (TextView)findViewById(R.id.tv_activity_friend_info_player_experience);
        tvPlayerHonor = (TextView)findViewById(R.id.tv_activity_friend_info_player_honor);
        tvPlayerTeam = (TextView)findViewById(R.id.tv_activity_friend_info_player_team);
        tvPlayerNote = (TextView)findViewById(R.id.tv_activity_friend_info_player_note);
        btnBottom = (Button)findViewById(R.id.btn_activity_friend_info);
        btnBottom.setOnClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100:

                        break;
                    case 0x102:
                        Toast.makeText(FriendInfoActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        UserService userService = new UserService();
        userService.setGetFriendInfoByIdListener(new GetFriendInfoByIdListener() {
            @Override
            public void onSuccess(int errorCode, final User user, final Referee referee, final Player player,final boolean isFriend) {
                Log.d("FriendInfoActivity",errorCode+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(user != null){
                            tvNickName.setText(user.getNickName());
                            tvGender.setText(user.getGender());
                            tvHeight.setText(user.getHeight()+"");
                            tvWeight.setText(user.getWeight()+"");
                            tvEmail.setText(user.getEmail()==null?"":user.getEmail());
                            tvNote.setText(user.getNote()==null?"":user.getNote());
                        }
                        if(referee != null){
                            lyReferee.setVisibility(View.VISIBLE);
                            tvRefereeStartTime.setText(referee.getStartJudgeTime().toString());
                            tvRefereeExperience.setText(referee.getExperience()==null?"":referee.getExperience());
                            tvRefereeHonor.setText(referee.getHonor()==null?"":referee.getHonor());
                            tvRefereeRank.setText(referee.getCertificate()==null?"":referee.getCertificate());
                            tvRefereeNote.setText(referee.getNote()==null?"":referee.getNote());
                        }
                        if(player != null){
                            lyPlayer.setVisibility(View.VISIBLE);
                            tvPlayerStartTime.setText(player.getStartPlayTime().toString());
                            tvPlayerExperience.setText(player.getExperience()==null?"":player.getExperience());
                            tvPlayerHonor.setText(player.getHonor()==null?"":player.getHonor());
                            tvPlayerTeam.setText(player.getTeam()==null?"":player.getTeam());
                            tvPlayerNote.setText(player.getNote()==null?"":player.getNote());
                        }
                        isFri = isFriend;
                        if(isFriend){
                            btnBottom.setText("send message");
                            btnBottom.setVisibility(View.VISIBLE);
                        }else{
                            btnBottom.setText("add friend");
                            btnBottom.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onError(int errorCode, String result) {
                Log.d("FriendInfoActivity",result);
                handler.sendEmptyMessage(0x102);
            }
        });
        userService.getFriendInfoById(userId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_friend_info:
                if(isFri){

                }else{
                    btnBottom.setVisibility(View.GONE);
                    UserService userService = new UserService();
                    userService.setAddFriendListener(new AddFriendListener() {
                        @Override
                        public void onSuccess(int errorCode, String result) {
                            Log.d("FriendInfoActivity",result);
                            if(errorCode == 100){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isFri = true;
                                        btnBottom.setText("send message");
                                        btnBottom.setVisibility(View.VISIBLE);
                                        Toast.makeText(FriendInfoActivity.this,"add success",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(int errorCode, String result) {
                            Log.d("FriendInfoActivity",result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FriendInfoActivity.this,"add friend faild",Toast.LENGTH_SHORT).show();
                                    btnBottom.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                    userService.addFriend(userId);
                }
                break;
        }
    }
}
