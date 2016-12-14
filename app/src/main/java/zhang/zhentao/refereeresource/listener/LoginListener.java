package zhang.zhentao.refereeresource.listener;

import zhang.zhentao.refereeresource.entity.User;

/**
 * Created by 张镇涛 on 2016/12/5.
 */

public interface LoginListener {
    void onSuccess(int errorCode, User user);
    void onError(int errorCode,String result);
}
