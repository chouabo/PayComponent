package com.mifengs.order.component.util;


import com.mifengs.order.component.exception.YwServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;


/**
 * @author TangCai
 * @ClassName: AssertUtil
 * @Description: 判断是否需要终止(这里用一句话描述这个类的作用)
 * @date 2017年11月4日 上午9:03:22
 * 注意：本内容仅限于智慧树内部传阅，禁止外泄以及用于其他的商业目
 */

public class AssertUtil {
    
    /**
     * 等值断言
     *
     * @param expected
     * @param actual
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertEquals(Object expected, Object actual, String errorCode, String message) throws YwServiceException {
        if (expected != actual && (expected == null || !expected.equals(actual))) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    /**
     * NotTrue断言
     *
     * @param value
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertNotTrue(Boolean value, String errorCode, String message) throws YwServiceException {
        if (Boolean.TRUE.equals(value)) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    /**
     * NotFalse断言
     *
     * @param value
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertNotFalse(Boolean value, String errorCode, String message) throws YwServiceException {
        if (Boolean.FALSE.equals(value)) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    /**
     * 非等值断言
     *
     * @param expected
     * @param actual
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertNotEquals(Object expected, Object actual, String errorCode, String message) throws YwServiceException {
        if (expected == actual || (expected != null && expected.equals(actual)) || (actual != null && actual.equals(expected))) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    /**
     * 非空断言
     *
     * @param value
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertNotNull(Object value, String errorCode, String message) throws YwServiceException {
        if (value == null) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    /**
     * 字符串不为空断言
     *
     * @param value
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertNotEmpty(String value, String errorCode, String message) throws YwServiceException {
        if (StringUtils.isBlank(value)) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    
    /**
     * 包含断言
     *
     * @param collections
     * @param value
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertContain(Collection<?> collections, Object value, String errorCode, String message) throws YwServiceException {
        boolean exists = false;
        for (Object obj : collections) {
            if (obj == value || obj.equals(value)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    /**
     * 不包含断言
     *
     * @param collections
     * @param value
     * @param errorCode
     * @param message
     * @throws
     */
    public static void assertNotContain(Collection<?> collections, Object value, String errorCode, String message) throws YwServiceException {
        boolean exists = false;
        for (Object obj : collections) {
            if (obj == value || obj.equals(value)) {
                exists = true;
                break;
            }
        }
        if (exists) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    public static void assertNotBlank(Collection obj, String errorCode, String message) throws
            YwServiceException {
        if (obj == null || obj.size() <= 0) {
            throw new YwServiceException(errorCode, message);
        }
    }
    
    public static void assertNotBlank(Object objs, String errorCode, String message) throws
            YwServiceException {
        if (objs == null || "".equals(objs)) {
            throw new YwServiceException(errorCode, message);
        }
    }
}

