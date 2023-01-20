package org.slf4j.impl;

import kr.jadekim.logger.integration.slf4j.JLoggerMdcAdapter;
import org.slf4j.spi.MDCAdapter;

public class StaticMDCBinder {

    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    public MDCAdapter getMDCA() {
        return new JLoggerMdcAdapter();
    }

    public String getMDCAdapterClassStr() {
        return JLoggerMdcAdapter.class.getName();
    }
}
