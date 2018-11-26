package com.mifengs.order.component.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayTypeStatusEnum {
    
    YES(1),
    NO(0),;
    
    private Integer status;
}
