package com.team3.weather.util;

import java.util.UUID;

public class UUIDGenerator {
    public static UUID generate() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }
}