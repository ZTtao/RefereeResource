package zhang.zhentao.refereeresource.listener;

import java.util.List;

import zhang.zhentao.refereeresource.entity.GameReservation;

/**
 * Created by 张镇涛 on 2016/12/26.
 */

public interface GetGameReservationListener {
    void onSuccess(int errorCode, List<GameReservation> list);
    void onError(int errorCode,String result);
}
