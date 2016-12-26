package zhang.zhentao.refereeresource.listener;

/**
 * Created by 张镇涛 on 2016/12/24.
 */

public interface UpdateReplyListener {
    void onSuccess(int errorCode,String result);
    void onError(int errorCode,String result);
}
