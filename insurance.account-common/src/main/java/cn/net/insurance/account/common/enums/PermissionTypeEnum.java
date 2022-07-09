package cn.net.insurance.account.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionTypeEnum {

    ROOT(1, "20211024", "根菜单id"),
    OMS_SYSTEM(2, "20211111", "运营管理系统根菜单id"),
    SMS_SYSTEM(3, "20211212", "服务支撑系统根菜单id"),
    SUB_SYSTEM(4, "20211314", "订户系统根菜单id"),;
    /**
     * 编码
     */
    private Integer code;
    /**
     * 权限记录对应的 id
     */
    private String permissionId;
    /**
     * 系统名称
     */
    private String permissionName;

    public static PermissionTypeEnum findByCode(Integer code) {
        for (PermissionTypeEnum enumObj : PermissionTypeEnum.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj;
            }
        }
        return null;
    }

    public static PermissionTypeEnum findByPermissionId(String permissionId) {
        for (PermissionTypeEnum enumObj : PermissionTypeEnum.values()) {
            if (enumObj.getPermissionId().equals(permissionId)) {
                return enumObj;
            }
        }
        return null;
    }
}
