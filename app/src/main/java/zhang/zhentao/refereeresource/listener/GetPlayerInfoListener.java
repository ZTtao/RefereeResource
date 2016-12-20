package zhang.zhentao.refereeresource.listener;

import zhang.zhentao.refereeresource.entity.Player;

/**
 * Created by 张镇涛 on 2016/12/20.
 */

public interface GetPlayerInfoListener {
    void onSuccess(int errorCode, Player player);
    void onError(int errorCode,String result);
}
