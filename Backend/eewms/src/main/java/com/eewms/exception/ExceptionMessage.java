package com.eewms.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMessage {
    public static Map<String, String> messages = new HashMap<>();
    public static String EXISTED_EMPLOYEE = "EXISTED_EMPLOYEE";

    static {
        messages.put(EXISTED_EMPLOYEE, "Nhân viên đã tồn tại");
    }
}
