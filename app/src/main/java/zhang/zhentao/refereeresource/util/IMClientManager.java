package zhang.zhentao.refereeresource.util;

import android.content.Context;

import net.openmob.mobileimsdk.android.ClientCoreSDK;
import net.openmob.mobileimsdk.android.conf.ConfigEntity;
import net.openmob.mobileimsdk.android.event.ChatBaseEvent;
import net.openmob.mobileimsdk.android.event.ChatTransDataEvent;
import net.openmob.mobileimsdk.android.event.MessageQoSEvent;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public class IMClientManager {

    private static String TAG = IMClientManager.class.getSimpleName();
    private static IMClientManager instance = null;
    private boolean init = false;

    private ChatBaseEventImpl baseEventListener = null;
    private ChatTransDataEventImpl transDataLinstener = null;
    private MessageQoSEventImpl messageQoSListener = null;

    private Context context = null;

    public static IMClientManager getInstance(Context context){
        if(instance == null){
            instance = new IMClientManager(context);
        }
        return instance;
    }
    private IMClientManager(Context context){
        this.context = context;
        initMobileIMSDK();
    }
    public void initMobileIMSDK(){
        if(!init){
            ConfigEntity.appKey = "abcd1234";
            ConfigEntity.serverIP = "10.0.2.2";
            ConfigEntity.serverUDPPort = 7901;

            ClientCoreSDK.getInstance().init(this.context);

            baseEventListener = new ChatBaseEventImpl();
            transDataLinstener = new ChatTransDataEventImpl();
            messageQoSListener = new MessageQoSEventImpl();
            ClientCoreSDK.getInstance().setChatBaseEvent(baseEventListener);
            ClientCoreSDK.getInstance().setChatTransDataEvent(transDataLinstener);
            ClientCoreSDK.getInstance().setMessageQoSEvent(messageQoSListener);

            init = true;
        }
    }
    public void release(){
    ClientCoreSDK.getInstance().release();
    }
    public ChatTransDataEventImpl getTransDataLinstener(){
        return transDataLinstener;
    }
    public ChatBaseEventImpl getBaseEventListener(){
        return baseEventListener;
    }
    public MessageQoSEventImpl getMessageQoSListener(){
        return messageQoSListener;
    }
}
