package zhang.zhentao.refereeresource.listener;

import java.util.List;

import zhang.zhentao.refereeresource.entity.FriendListItem;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public interface SearchFriendListener {
    void onSuccess(int errorCode, List<FriendListItem> result);
    void onError(int errorCode,String result);
}
