package com.mifengs.order.component.util;

/**
 * @author Zhaojiaheng
 * @ClassName: Arith
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月31日 下午 17:10:31
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
import java.math.BigDecimal;

public class Arith {
    private static final int DEF_DIV_SCALE = 10;
    
    private Arith() {
    }
    
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    
    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }
    
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, 4).doubleValue();
        }
    }
    
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(Double.toString(v));
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 4).doubleValue();
        }
    }
    
    public static double add(BigDecimal bigDecimal, BigDecimal bigDecimal2, BigDecimal bigDecimal3) {
        return bigDecimal.add(bigDecimal2).add(bigDecimal3).doubleValue();
    }
    
    public static double add(BigDecimal preDepositPrice, BigDecimal finalPrice) {
        return preDepositPrice.add(finalPrice).doubleValue();
    }
    
    public static void main(String[] args) {
        System.out.println(0.060000000000000005D);
        System.out.println(add(0.05D, 0.01D));
        System.out.println(0.5800000000000001D);
        System.out.println(sub(1.0D, 0.42D));
        System.out.println(401.49999999999994D);
        System.out.println(mul(4.015D, 100.0D));
        System.out.println(1.2329999999999999D);
        System.out.println(div(123.3D, 100.0D, 2));
    }
}
