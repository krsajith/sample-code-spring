package com.taomish.msservice01;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeTest {
    public static void main(String[] args) {
        var time = "2024-04-22T23:09:46.832Z";
        var t= LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME).toInstant(ZoneOffset.UTC).atZone(ZoneId.of("Asia/Singapore"));
        System.out.println(t);
    }
}
