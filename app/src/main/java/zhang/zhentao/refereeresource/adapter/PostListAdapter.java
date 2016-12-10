package zhang.zhentao.refereeresource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.Post;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class PostListAdapter extends ArrayAdapter<Post> {

    private int resource;
    private Context context;
    private List<Post> list;

    public PostListAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.list = objects;
    }
    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public Post getItem(int position){
        return list.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_post_list,null);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tv_mainpage_post_list_title);
            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tv_mainpage_post_list_content);
            viewHolder.tvAuthor = (TextView)convertView.findViewById(R.id.tv_mainpage_post_list_author);
            viewHolder.tvTime = (TextView)convertView.findViewById(R.id.tv_mainpage_post_list_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Post post = list.get(position);
        if(post != null){
            viewHolder.tvTitle.setText(post.getTitle());
            viewHolder.tvContent.setText(post.getContent());
            viewHolder.tvAuthor.setText(post.getUserName());
            viewHolder.tvTime.setText(post.getCreateTime().toString());
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvTitle;
        TextView tvContent;
        TextView tvAuthor;
        TextView tvTime;
    }
}
