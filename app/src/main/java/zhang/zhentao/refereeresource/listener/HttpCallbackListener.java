package zhang.zhentao.refereeresource.listener;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
