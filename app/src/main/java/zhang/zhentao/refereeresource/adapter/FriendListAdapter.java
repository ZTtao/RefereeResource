package zhang.zhentao.refereeresource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.FriendListItem;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public class FriendListAdapter extends ArrayAdapter<FriendListItem> {
    private Context context;
    private int resource;
    private List<FriendListItem> list;

    public FriendListAdapter(Context context, int resource, List<FriendListItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }
    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public FriendListItem getItem(int position){
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
            convertView = inflater.inflate(R.layout.item_friend_list,null);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_item_friend_list);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        FriendListItem item = list.get(position);
        if(item != null){
            viewHolder.tvName.setText(item.getName());
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvName;
    }
}
