package zhang.zhentao.refereeresource.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.adapter.PostListAdapter;
import zhang.zhentao.refereeresource.entity.Post;
import zhang.zhentao.refereeresource.listener.GetPostListListener;
import zhang.zhentao.refereeresource.service.GetPostListService;
import zhang.zhentao.refereeresource.util.ContextUtil;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class PostListFragment extends Fragment {

    private ListView listView;
    private List<Post> list;
    private PostListAdapter adapter;
    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_post,container,false);
        listView = (ListView) view.findViewById(R.id.fragment_post_listview);
        list = new ArrayList<>();
        adapter = new PostListAdapter(ContextUtil.getInstance(),R.layout.item_post_list,list);
        listView.setAdapter(adapter);
        GetPostListService service = new GetPostListService();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };
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
        return view;
    }
}
