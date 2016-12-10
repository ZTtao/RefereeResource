package zhang.zhentao.refereeresource.listener;

import java.util.List;

import zhang.zhentao.refereeresource.entity.Post;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public interface GetPostListListener {
    void onSuccess(int errorCode, List<Post> list);
    void onError(int errorCode,String result);
}
