package com.eewms.constant;

public enum SettingType {
    UNIT(1),
    CATEGORY(2),
    BRAND(3);

    private final int value;

    SettingType(int value) {
        this.value = value;
    }
//Lấy ra int để lưu xuống DB (type_id)
    public int getValue() {
        return value;
    }
//Convert ngược từ DB về enum để xử lý logic
    public static SettingType fromValue(int value) {
        for (SettingType type : SettingType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SettingType value: " + value);
    }
}
