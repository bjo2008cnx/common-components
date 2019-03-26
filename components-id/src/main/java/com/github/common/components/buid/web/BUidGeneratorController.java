package com.github.common.components.buid.web;

import com.github.common.components.buid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Michael.Wang
 * @date 2017/4/21
 */
//@Controller
public class BUidGeneratorController {
    @Autowired
    private UidGenerator uidGenerator;

    /**
     * 生成Id
     *
     * @return
     */
    @RequestMapping(value = "/api/common/guid", method = RequestMethod.GET)
    @ResponseBody
    public String generate() {
        return String.valueOf(uidGenerator.getUID());
    }

    /**
     * 生成Id
     *
     * @return
     */
    @RequestMapping(value = "/api/common/guids/{size}", method = RequestMethod.GET)
    @ResponseBody
    public String generate(@PathVariable("size") int size) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            buffer.append(uidGenerator.getUID());
        }
        return buffer.toString();
    }
}
