package anhtester.com.enums;

import java.util.Random;

public enum RoomType {

    SINGLE("Single"), FAMILY("Family"), BUSINESS("Business");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }

    public static RoomType getRandom() {
        return values()[new Random().nextInt(values().length)];
    }

    @Override
    public String toString() {
        return value;
    }
}