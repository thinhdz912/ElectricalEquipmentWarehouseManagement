package com.eewms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NAME_EMPTY("NAME_EMPTY","Tên không thể để trống", HttpStatus.BAD_REQUEST),
    PURCHASE_ORDER_EMPTY("PURCHASE_ORDER_EMPTY", "Sản phẩm trong đơn hàng rỗng", HttpStatus.BAD_REQUEST),;
    ErrorCode(String codeMessage, String message, HttpStatus code) {
        this.codeMessage = codeMessage;
        this.message = message;
        this.statusCode = code;
    }
    private final String codeMessage;
    private final String message;
    private final HttpStatus statusCode;
}
