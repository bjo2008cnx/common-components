package com.github.components.springcloud.auth;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.github.common.components.auth.client.AuthConstant;
import com.github.common.components.auth.common.WebSignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthSignFilter extends ZuulFilter {

    public static final int STATUS_CODE_NOT_AUTHED = 401;
    public static final int STATUS_CODE_SUCCESS = 200;
    /**
     * sign有效时间，默认5分钟
     */
    public static final int EXPIRE_INTERVAL = 60 * 5;

    //TODO move to apollo
    private static String SECRET_SEED = "xLCde8Dad93OxLMYuX8LdT9Lx8Ix4XKl82x9dDL";

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String sign = request.getParameter(AuthConstant.HEADER_SIGN);
        String timestamp = request.getParameter(AuthConstant.HEADER_TIMESTAMP);
        String appId = request.getParameter(AuthConstant.HEADER_APP_ID);

        //过期校验
        //if (WebSignUtil.isExpired(timestamp, EXPIRE_INTERVAL)) {
            //respondError(context);
        //} else {
            //验证签名
            String signLocal = WebSignUtil.sign(appId, SECRET_SEED, null, timestamp);
            if (!signLocal.equals(sign)) {
                respondError(context);
            } else {
                context.setResponseStatusCode(STATUS_CODE_SUCCESS);
            }
       // }
        return null;
    }

    private void respondError(RequestContext context) {
        context.setResponseStatusCode(STATUS_CODE_NOT_AUTHED);
        context.setSendZuulResponse(false);
        HttpServletResponse response = context.getResponse();
        try {
            response.getWriter().write("Please provide right sign!");
        } catch (IOException e) {
            log.error("fail to write response", e);
        }
    }

}