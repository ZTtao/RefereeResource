package zhang.zhentao.refereeresource.listener;

import zhang.zhentao.refereeresource.entity.Player;
import zhang.zhentao.refereeresource.entity.Referee;
import zhang.zhentao.refereeresource.entity.User;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public interface GetFriendInfoByIdListener {
    void onSuccess(int errorCode, User user, Referee referee, Player player,boolean isFriend);
    void onError(int errorCode,String result);
}
