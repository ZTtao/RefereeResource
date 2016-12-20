package zhang.zhentao.refereeresource.listener;

/**
 * Created by 张镇涛 on 2016/12/17.
 */

public interface UpdateRefereeInfoListener {
    void onSuccess(int errorCode,String result);
    void onError(int errorCode,String result);
}
