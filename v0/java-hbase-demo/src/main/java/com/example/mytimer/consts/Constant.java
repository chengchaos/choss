package com.example.mytimer.consts;

import java.util.regex.Pattern;

public class Constant {


    public static final Pattern REGEXP_PATTERN_COMMA = Pattern.compile(",");
    public static final Pattern REGEXP_PATTERN_COLON = Pattern.compile(":");

    public static final String REDIS_CACHE_CLUSTER_NODES =
            "192.168.88.241:7000," +
            "192.168.88.241:7001," +
            "192.168.88.241:7002," +
            "192.168.88.241:7003," +
            "192.168.88.241:7004," +
            "192.168.88.241:7005"
            ;

    public static final int REDIS_CACHE_COMMANDTIMEOUT = 30000;

    public static final int REDIS_CACHE_SOTIMEOUT = 1000;

    public static final int REDIS_CACHE_MAXATTEMPTS = 10;

    public static final String REDIS_CACHE_CLUSTER_PASSWORD = "";
}
