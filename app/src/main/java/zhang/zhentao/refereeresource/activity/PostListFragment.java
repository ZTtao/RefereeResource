package zhang.zhentao.refereeresource.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.adapter.PostListAdapter;
import zhang.zhentao.refereeresource.entity.Post;
import zhang.zhentao.refereeresource.listener.GetPostListListener;
import zhang.zhentao.refereeresource.service.PostService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class PostListFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private ListView listView;
    private List<Post> list;
    private PostListAdapter adapter;
    private Handler handler;
    private Runnable runnable;
    private Button btnAdd;
    private Button btnReferee;
    private Button btnGame;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_post,container,false);
        listView = (ListView) view.findViewById(R.id.fragment_post_listview);
        btnAdd = (Button)view.findViewById(R.id.btn_fragment_post_add);
        btnAdd.setOnClickListener(this);
        btnReferee = (Button)view.findViewById(R.id.btn_fragment_post_referee_reservation);
        btnReferee.setOnClickListener(this);
        btnGame = (Button)view.findViewById(R.id.btn_fragment_post_game_reservation);
        btnGame.setOnClickListener(this);
        list = new ArrayList<>();
        adapter = new PostListAdapter(ContextUtil.getInstance(),R.layout.item_post_list,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };
        updateData();

        return view;
    }
    private void updateData(){
        PostService service = new PostService();

        service.setListListener(new GetPostListListener() {
            @Override
            public void onSuccess(int errorCode,List<Post> l) {
                if(errorCode == 100) {
                    list.clear();
                    list.addAll(l);
                    handler.post(runnable);
                }
                Log.d("PostListFragment:",errorCode+l.toString());
            }

            @Override
            public void onError(int errorCode,String result) {
                Log.d("PostListFragment:",errorCode+result);
            }
        });
        service.getPostListFormServer();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fragment_post_add:
                Intent intent = new Intent(ContextUtil.getInstance(),AddPostActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.btn_fragment_post_referee_reservation:
                startActivity(new Intent(ContextUtil.getInstance(),RefereeReservationActivity.class));
                break;
            case R.id.btn_fragment_post_game_reservation:

                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 100:
                switch (resultCode){
                    case -1:
                        //发布成功，刷新数据
                        Log.d("PostListFragment","发布成功，刷新数据");
                        updateData();
                        break;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.fragment_post_listview:
                Intent intent = new Intent(ContextUtil.getInstance(),PostDetailActivity.class);
                intent.putExtra("post",list.get(position));
                startActivity(intent);
                break;
        }
    }
}
