package org.slf4j.impl;

import kr.jadekim.logger.integration.slf4j.JLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

@SuppressWarnings("deprecation")
public class StaticLoggerBinder implements LoggerFactoryBinder {

    public static String REQUESTED_API_VERSION = "2.0.5";

    private static StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    @SuppressWarnings("FieldMayBeFinal")
    private static Object KEY = new Object();

    private final JLoggerFactory factory = new JLoggerFactory();

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
        return JLoggerFactory.class.getName();
    }
}
