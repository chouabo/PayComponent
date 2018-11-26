package com.mifengs.order.component.base;


import com.mifengs.order.component.exception.YwException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.UndeclaredThrowableException;


/**
 * @author TangCai
 * @ClassName: BaseAction
 * @Description: Action的共类(这里用一句话描述这个类的作用)
 * @date 2017年4月24日 上午11:02:53
 */
@Slf4j
public abstract class BaseAction {
    
    protected ModelAndView modelAndView;
    
    /**
     * @param @param  e
     * @param @return 设定文件
     * @return WJException    返回类型
     * @throws
     * @Title: 异常处理
     * @Description: 系统中抛出的业务异常捕获(这里用一句话描述这个方法的作用)
     */
    @Deprecated
    protected void exceptionHandling(Exception e) {
        Result result = new Result();
        YwException wj = null;//业务异常捕获
        if (e instanceof YwException) wj = (YwException) e;
        else if (e instanceof UndeclaredThrowableException) {
            Throwable thr = ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause();
            if (thr instanceof YwException) wj = (YwException) thr;
        }
        if (wj != null) {
            result.setStatus(wj.getCode());
            result.setInfo(wj.getInfo());
        } else result.setStatus(MyConstants.RESULT.EX9999);//未知异常
    }
    
    /**
     * 基于@ExceptionHandler异常处理
     */
    @ExceptionHandler
    @ResponseBody
    public Result handleAndReturnData(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error(e.getMessage());
        YwException wj = null;//业务异常捕获
        if (e instanceof YwException) wj = (YwException) e;
        else if (e instanceof UndeclaredThrowableException) {
            Throwable thr = ((UndeclaredThrowableException) e).getUndeclaredThrowable().getCause();
            if (thr instanceof YwException) wj = (YwException) thr;
        }
        Result result = new Result();
        if (wj != null) {
            result.setStatus(wj.getCode());
            result.setInfo(wj.getInfo());
        } else {
            e.printStackTrace();
            result.setStatus(MyConstants.RESULT.EX9999);//未知异常
        }
        
        return result;
    }
}
