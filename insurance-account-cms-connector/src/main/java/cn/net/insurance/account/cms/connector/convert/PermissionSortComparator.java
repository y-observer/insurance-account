package cn.net.insurance.account.cms.connector.convert;

import cn.net.insurance.account.cms.connector.entity.SysPermission;
import java.util.Comparator;
import java.util.Objects;

public class PermissionSortComparator implements Comparator<SysPermission> {

    @Override
    public int compare(SysPermission o1, SysPermission o2) {
        if(Objects.isNull(o1.getSort())){
            return -1;
        }
        if(Objects.isNull(o2.getSort())){
            return 1;
        }
        return o1.getSort().compareTo(o2.getSort());
    }
}
