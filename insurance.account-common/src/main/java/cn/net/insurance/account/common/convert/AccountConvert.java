package cn.net.insurance.account.common.convert;


import cn.net.insurance.account.common.constant.AccountConstant;
import cn.net.insurance.account.common.enums.PermissionTypeEnum;

public class AccountConvert {

    public static boolean isAdmin(String accountId) {
        if (accountId == null) {
            return false;
        }
        return AccountConstant.ADMIN_ACCOUNT_ID.equals(accountId);
    }

    public static boolean isRootPermission(String permissionId) {
        if (permissionId == null) {
            return false;
        }
        return PermissionTypeEnum.ROOT.getPermissionId().equals(permissionId);
    }

    public static boolean isSystemPermission(String permissionId) {
        if (permissionId == null) {
            return false;
        }
        return PermissionTypeEnum.OMS_SYSTEM.getPermissionId().equals(permissionId)
                || PermissionTypeEnum.SMS_SYSTEM.getPermissionId().equals(permissionId)
                || PermissionTypeEnum.SUB_SYSTEM.getPermissionId().equals(permissionId);
    }
}
