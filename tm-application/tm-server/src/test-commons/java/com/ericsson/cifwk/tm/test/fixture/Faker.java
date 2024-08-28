package com.ericsson.cifwk.tm.test.fixture;

import java.util.Random;
import java.util.UUID;

public class Faker {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final Random random = new Random();
    private static Long globalId = (long) 0;

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String randomString() {
        return randomString(26);
    }

    public static String randomString(int length) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < length; i++) {
            double index = random.nextDouble() * CHARACTERS.length();
            buffer.append(CHARACTERS.charAt((int) index));
        }
        return buffer.toString();
    }

    public static int randomInt() {
        return randomInt(100000);
    }

    public static int randomInt(int n) {
        return random.nextInt(n);
    }

    public static <T extends Enum<T>> T enumRandomValue(Class<T> enumType) {
        return enumNthValue(enumType, randomInt());
    }

    public static <T extends Enum<T>> T enumNthValue(Class<T> enumType, int n) {
        T[] constants = enumType.getEnumConstants();
        return constants[n % constants.length];
    }

    synchronized public static Long autoIncrement() {
        Long uniqueId = globalId;
        globalId++;
        return uniqueId;
    }
}
