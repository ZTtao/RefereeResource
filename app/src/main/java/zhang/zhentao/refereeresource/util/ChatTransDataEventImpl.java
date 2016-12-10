package zhang.zhentao.refereeresource.util;

import android.util.Log;

import net.openmob.mobileimsdk.android.event.ChatTransDataEvent;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class ChatTransDataEventImpl implements ChatTransDataEvent{

    @Override
    public void onTransBuffer(String fingerPrint, int userId, String content) {
        Log.d("ChatTransDataEventImpl","收到"+userId+"的消息："+content);
    }

    @Override
    public void onErrorResponse(int errorCode, String errorMsg) {
        Log.d("ChatTransDataEventImpl","收到服务端错误,"+errorCode+":"+errorMsg);
    }
}
