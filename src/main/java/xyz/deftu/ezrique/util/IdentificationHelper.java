package xyz.deftu.ezrique.util;

import java.util.UUID;

public class IdentificationHelper {

    public static String generateUuid(int size) {
        if (size > 16)
            throw new IllegalArgumentException("Size must be lower than 16!");
        return UUID.randomUUID().toString().replace("-", "").substring(0, size);
    }

    public static String generateUuid() {
        return generateUuid(4);
    }

}