package cn.net.insurance.account.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonStateEnum {

    DISENABLE(0,"禁用"),
    ENABLE(1,"启用"),
    ;

    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String descr;

    public static CommonStateEnum findByCode(Integer code) {
        for (CommonStateEnum enumObj : CommonStateEnum.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj;
            }
        }
        return null;
    }
}
