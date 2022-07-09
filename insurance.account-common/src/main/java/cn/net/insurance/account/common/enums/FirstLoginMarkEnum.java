package cn.net.insurance.account.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FirstLoginMarkEnum {

    UNLOGIN (0,"未首次登录"),
    LOGIN(1,"已完成首次登录"),

    ;

    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String descr;

    public static FirstLoginMarkEnum findByCode(Integer code) {
        for (FirstLoginMarkEnum enumObj : FirstLoginMarkEnum.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj;
            }
        }
        return null;
    }
}
