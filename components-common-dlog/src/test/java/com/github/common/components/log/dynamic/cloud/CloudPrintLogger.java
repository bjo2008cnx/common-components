package com.github.common.components.log.dynamic.cloud;

import com.github.common.components.log.dynamic.common.CommonPrintLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Content :
 */
@Service
@Slf4j
public class CloudPrintLogger {

    @PostConstruct
    public void printLogger() throws Exception {
        CommonPrintLogger.print();
    }
}
