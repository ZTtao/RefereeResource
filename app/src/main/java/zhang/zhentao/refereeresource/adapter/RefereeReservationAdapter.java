package zhang.zhentao.refereeresource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import zhang.zhentao.refereeresource.R;
import zhang.zhentao.refereeresource.entity.RefereeReservation;

/**
 * Created by 张镇涛 on 2016/12/21.
 */

public class RefereeReservationAdapter extends ArrayAdapter<RefereeReservation> {
    private Context context;
    private int resource;
    private List<RefereeReservation> list;

    public RefereeReservationAdapter(Context context, int resource, List<RefereeReservation> objects) {
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
    public RefereeReservation getItem(int position){
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
            convertView = inflater.inflate(R.layout.item_referee_reservation,null);
            viewHolder.tvUser = (TextView)convertView.findViewById(R.id.tv_activity_referee_reservation_user);
            viewHolder.tvAddress = (TextView)convertView.findViewById(R.id.tv_activity_referee_reservation_address);
            viewHolder.tvGTime = (TextView)convertView.findViewById(R.id.tv_activity_referee_reservation_gtime);
            viewHolder.tvTime = (TextView)convertView.findViewById(R.id.tv_activity_referee_reservation_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        RefereeReservation refereeReservation = list.get(position);
        if(refereeReservation != null){
            viewHolder.tvUser.setText(refereeReservation.getPlayerName());
            viewHolder.tvAddress.setText(refereeReservation.getAddress());
            viewHolder.tvGTime.setText(refereeReservation.getGameTime().toString());
            viewHolder.tvTime.setText(refereeReservation.getCreateTime().toString());
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvUser;
        TextView tvTime;
        TextView tvAddress;
        TextView tvGTime;
    }
}
