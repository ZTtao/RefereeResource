package zhang.zhentao.refereeresource.listener;

import java.util.List;

import zhang.zhentao.refereeresource.entity.RefereeReservation;

/**
 * Created by 张镇涛 on 2016/12/21.
 */

public interface GetRefereeReservationListener {
    void onSuccess(int errorCode, List<RefereeReservation> list);
    void onError(int errorCode,String result);
}
