package zhang.zhentao.refereeresource.listener;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public interface AddFriendListener {
    void onSuccess(int errorCode,String result);
    void onError(int errorCode,String result);
}
