package com.dmaker.exception;

import com.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {
    // 예외 처리가 필요한 이유
    // 에러 메세지가 낮은 수준에 이유를 포함하여 뜬다면 화면쪽에서는 이유를 알 수 없는 에러가 생기기 시작하면서
    // 버그를 픽스할때 어려움이 생길수 있다.`
    // 또한 에러 메세지에 너무 자세한 에러가 표시된다면 보안에 취약해질 수 있다.
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleExeption(
            DMakerException e,
            HttpServletRequest request
    ) {
        log.error("errorCode : {}, url : {}, message : {}",
                e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    // controller 안에 들어오기전 발생하는 오류들을 처리해준다.
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url : {}, message : {}",
                request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(DMakerErrorCode.INVALID_REQUEST)
                .errorMessage(DMakerErrorCode.INVALID_REQUEST.getMessage())
                .build();
    }

    // 예상하지 못한 문제들이 발생했을때 처리해준다.
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("url : {}, message : {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(DMakerErrorCode.INTERNAL_SERVER_ERROR)
                .errorMessage(DMakerErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

}
