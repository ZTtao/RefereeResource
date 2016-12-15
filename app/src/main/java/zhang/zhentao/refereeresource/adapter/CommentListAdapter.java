package zhang.zhentao.refereeresource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.Comment;
import zhang.zhentao.refereeresource.entity.Post;

/**
 * Created by 张镇涛 on 2016/12/15.
 */

public class CommentListAdapter extends ArrayAdapter<Comment> {
    private Context context;
    private int resource;
    private List<Comment> list;

    public CommentListAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }
    public void clearList(){
        list.clear();
    }
    public void listAddAll(List<Comment> list){
        if(list != null)
            this.list.addAll(list);
    }
    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public Comment getItem(int position){
        return list.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CommentListAdapter.ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new CommentListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_comment,null);
            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tv_item_comment_content);
            viewHolder.tvAuthor = (TextView)convertView.findViewById(R.id.tv_item_comment_user);
            viewHolder.tvTime = (TextView)convertView.findViewById(R.id.tv_item_comment_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (CommentListAdapter.ViewHolder)convertView.getTag();
        }
        Comment comment = list.get(position);
        if(comment != null){
            viewHolder.tvContent.setText(comment.getContent());
            viewHolder.tvAuthor.setText(comment.getUserName());
            viewHolder.tvTime.setText(comment.getCreateTime().toString());
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvContent;
        TextView tvAuthor;
        TextView tvTime;
    }
}
