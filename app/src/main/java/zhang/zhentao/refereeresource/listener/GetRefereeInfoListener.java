package zhang.zhentao.refereeresource.listener;

import zhang.zhentao.refereeresource.entity.Referee;

/**
 * Created by 张镇涛 on 2016/12/17.
 */

public interface GetRefereeInfoListener {
    void onSuccess(int errorCode, Referee referee);
    void onError(int errorCode,String result);
}
