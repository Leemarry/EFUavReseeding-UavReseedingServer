package com.bear.reseeding.config;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.utils.LogUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获
 *
 * @Auther: bear
 * @Date: 2023/7/26 09:05
 * @Description: null
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @return 返回给前端的错误信息提示
     * @author jinhaoxun
     * @description 统一的异常处理方法
     */
    @ExceptionHandler(value = Exception.class)
    public Result handleException(HttpServletRequest request, Exception ex) {
//        LogUtil.logError("全局异常：" + ex);
        if (request != null) {
            //换行符
            String lineSeparatorStr = System.getProperty("line.separator");

            StringBuilder exStr = new StringBuilder();
            StackTraceElement[] trace = ex.getStackTrace();
            //   获取堆栈信息并输出为打印的形式
            for (StackTraceElement s : trace) {
                exStr.append("\tat ").append(s).append("\r\n");
            }
            //打印error级别的堆栈日志
            LogUtil.logError(" ---------------------- 捕获异常 start ---------------------- ");
            LogUtil.logError("访问地址：" + request.getRequestURL() + ",请求方法：" + request.getMethod() + ",远程地址：" + request.getRemoteAddr() + lineSeparatorStr +
                    "错误堆栈信息:" + ex.toString() + lineSeparatorStr + exStr);
            LogUtil.logError(" ---------------------- 捕获异常 end ---------------------- ");
        } else {
            LogUtil.logError("全局异常：" + ex);
        }
        return ResultUtil.error("操作失败，请联系管理员！");
    }

//    // 全局异常拦截（拦截项目中的所有异常）
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Result handler500Exception(Exception e)throws Exception {
//        // 打印堆栈，以供调试
//        LogUtil.logError("全局异常信息: "+e);
//        // 返回给前端
//        return ResultUtil.error(e.getLocalizedMessage());
//    }


//    /**
//     * 重复请求的异常
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(RepeatSubmitException.class)
//    public Result onException(RepeatSubmitException ex) {
//        //打印日志
////        log.error(ex.getMessage());
//        //todo 日志入库等等操作
//
//        //统一结果返回
//        return ResultUtil.error("");
//    }


//    /**
//     * 自定义的业务上的异常
//     */
//    @ExceptionHandler(ServiceException.class)
//    public Result onException(ServiceException ex) {
//        //打印日志
//        LogUtil.logError("异常：" + ex);
//        //todo 日志入库等等操作
//        //统一结果返回
//        // return new ResultResponse(ResultCodeEnum.CODE_SERVICE_FAIL);
//        return ResultUtil.error("");
//    }

//
//
//    /**
//     * 捕获一些进入controller之前的异常，有些4xx的状态码统一设置为200
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler({HttpRequestMethodNotSupportedException.class,
//            HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class,
//            MissingPathVariableException.class, MissingServletRequestParameterException.class,
//            ServletRequestBindingException.class, ConversionNotSupportedException.class,
//            TypeMismatchException.class, HttpMessageNotReadableException.class,
//            HttpMessageNotWritableException.class,
//            MissingServletRequestPartException.class, BindException.class,
//            NoHandlerFoundException.class, AsyncRequestTimeoutException.class})
//    public Result onException(Exception ex) {
//        //打印日志
////        log.error(ex.getMessage());
//        //todo 日志入库等等操作
//
//        //统一结果返回
//        return ResultUtil.error("");
//    }
}
