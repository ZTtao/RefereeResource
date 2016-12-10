package zhang.zhentao.refereeresource.util;

import android.util.Log;

import net.openmob.mobileimsdk.android.event.MessageQoSEvent;
import net.openmob.mobileimsdk.server.protocal.Protocal;

import java.util.ArrayList;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class MessageQoSEventImpl implements MessageQoSEvent{
    @Override
    public void messagesLost(ArrayList<Protocal> lostMessages) {
        Log.d("MessageQosEventImpl","未实时送达，共"+lostMessages.size());
    }

    @Override
    public void messagesBeReceived(String fingerPrint) {
        if(fingerPrint != null){
            Log.d("MessageQosEventImpl","对方已收到消息，fp:"+fingerPrint);
        }
    }
}
