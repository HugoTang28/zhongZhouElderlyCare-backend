package com.zzyl.framework.web;

import com.zzyl.common.core.R;
import com.zzyl.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public R<Void> serviceException(ServiceException e) {
        log.error("业务异常：{}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public R<Void> badCredentials(BadCredentialsException e) {
        return R.fail("账号或密码错误");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R<Void> accessDenied(AccessDeniedException e) {
        return R.fail(403, "没有权限访问，请联系管理员");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public R<Void> validException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + error.getDefaultMessage())
                .orElse("参数校验失败");
        return R.fail(message);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> exception(Exception e) {
        log.error("系统异常", e);
        return R.fail("系统异常，请稍后再试");
    }
}
