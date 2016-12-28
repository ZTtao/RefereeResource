package zhang.zhentao.refereeresource.entity;

import java.io.Serializable;

/**
 * Created by 张镇涛 on 2016/12/28.
 */

public class FriendListItem implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
