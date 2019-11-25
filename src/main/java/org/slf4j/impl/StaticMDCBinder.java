package org.slf4j.impl;

import kr.jadekim.logger.slf4j.MdcAdapterImpl;
import org.slf4j.spi.MDCAdapter;

public class StaticMDCBinder {

    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    public MDCAdapter getMDCA() {
        return new MdcAdapterImpl();
    }

    public String getMDCAdapterClassStr() {
        return MdcAdapterImpl.class.getName();
    }
}
