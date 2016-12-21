package zhang.zhentao.refereeresource.listener;

/**
 * Created by 张镇涛 on 2016/12/20.
 */

public interface LikedListener {
    public void onSuccess(int errorCode,String result);
    public void onError(int errorCode,String result);
}
