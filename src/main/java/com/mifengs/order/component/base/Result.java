package com.mifengs.order.component.base;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TangCai
 * @ClassName: Result
 * @Description: 统一的客户端返回对象(这里用一句话描述这个类的作用)
 * @date 2017年4月24日 上午11:02:53
 */
@Getter
@Setter
public class Result {
    private String status = MyConstants.RESULT.SC0000;//状态,默认成功
    private String info;//处理信息
    private Map<String, Object> paramer = new HashMap<String, Object>();//返回参数数据
    
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
    
}
