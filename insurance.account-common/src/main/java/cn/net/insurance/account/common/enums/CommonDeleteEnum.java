package cn.net.insurance.account.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description :
 * @Author : panxf
 * @Date : 2021/11/27 10:19
 */
@Getter
@AllArgsConstructor
public enum CommonDeleteEnum {

    NORMAL(0,"正常"),
    DELETED(1,"删除"),
    ;

    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String descr;

    public static CommonDeleteEnum findByCode(Integer code) {
        for (CommonDeleteEnum enumObj : CommonDeleteEnum.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj;
            }
        }
        return null;
    }
}
