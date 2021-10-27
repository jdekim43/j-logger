package org.slf4j.impl;

import kr.jadekim.logger.integration.slf4j.LoggerFactoryImpl;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {

    public static String REQUESTED_API_VERSION = "1.7.28";

    private static StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    private static Object KEY = new Object();

    private final LoggerFactoryImpl factory = new LoggerFactoryImpl();

    private StaticLoggerBinder() {
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    static void reset() {
        SINGLETON = new StaticLoggerBinder();
    }

    public ILoggerFactory getLoggerFactory() {
        return factory;
    }

    public String getLoggerFactoryClassStr() {
        return LoggerFactoryImpl.class.getName();
    }
}
