package zhang.zhentao.refereeresource.listener;

import java.util.List;

import zhang.zhentao.refereeresource.entity.Comment;

/**
 * Created by 张镇涛 on 2016/12/15.
 */

public interface GetCommentListListener {
    void onSuccess(int errorCode,List<Comment> result);
    void onError(int errorCode,String result);
}
