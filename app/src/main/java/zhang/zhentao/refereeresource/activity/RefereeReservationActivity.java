package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.adapter.RefereeReservationAdapter;
import zhang.zhentao.refereeresource.entity.RefereeReservation;
import zhang.zhentao.refereeresource.listener.GetRefereeReservationListener;
import zhang.zhentao.refereeresource.service.RefereeService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/21.
 */

public class RefereeReservationActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private ListView listView;
    private Button btnAdd;
    private RefereeReservationAdapter adapter;
    private List<RefereeReservation> list;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee_reservation);
        btnAdd = (Button)findViewById(R.id.btn_activity_referee_reservation_add);
        btnAdd.setOnClickListener(this);
        list = new ArrayList<>();
        listView = (ListView)findViewById(R.id.lv_activity_referee_reservation);
        adapter = new RefereeReservationAdapter(RefereeReservationActivity.this,R.layout.item_referee_reservation,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x100: //更新数据成功，有数据
                        Toast.makeText(RefereeReservationActivity.this,"数据更新成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x101: //更新数据成功，无数据
                        list.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(RefereeReservationActivity.this,"数据更新成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x102: //获取数据失败
                        Toast.makeText(RefereeReservationActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 0x103: //网络错误
                        Toast.makeText(RefereeReservationActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        updateData();
    }
    private void updateData(){
        RefereeService refereeService = new RefereeService();
        refereeService.setGetRefereeReservationListener(new GetRefereeReservationListener() {
            @Override
            public void onSuccess(int errorCode, final List<RefereeReservation> l) {
                Log.d("RefereeRActivity","success");
                if(errorCode == 100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.clear();
                            list.addAll(l);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    handler.sendEmptyMessage(0x100);
                }else if(errorCode == 101){
                    handler.sendEmptyMessage(0x101);
                }
            }

            @Override
            public void onError(int errorCode, String result) {
                Log.d("RefereeRActivity",result);
                if(errorCode == 102){
                    handler.sendEmptyMessage(0x102);
                }else {
                    handler.sendEmptyMessage(0x103);
                }
            }
        });
        refereeService.getRefereeReservation();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_referee_reservation_add:
                if(ContextUtil.getPlayerInstance() != null)
                    startActivityForResult(new Intent(RefereeReservationActivity.this,AddRefereeReservationActivity.class),100);
                else
                    Toast.makeText(RefereeReservationActivity.this,"not a player",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RefereeReservationActivity.this,RefereeReservationDetailActivity.class);
        intent.putExtra("reservation",list.get(position));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 100:
                switch (resultCode){
                    case RESULT_OK:
                        updateData();
                        break;
                }
                break;
        }
    }
}
