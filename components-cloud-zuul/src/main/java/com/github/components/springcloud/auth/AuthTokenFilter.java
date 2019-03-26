package com.github.components.springcloud.auth;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

//@Component
public class AuthTokenFilter extends ZuulFilter {

    public static final int STATUS_CODE_NOT_AUTHED = 401;
    public static final int STATUS_CODE_NOT_SUCCESS = 200;

    private static Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);
    private static String token = "xLCde8Dad93OxLMYuX8LdT9Lx8Ix4XKl82x9dDL";

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
        Object accessToken = context.getRequest().getParameter("token");
        if (!token.equals(accessToken)) {
            log.warn("Wrong access token,{}", accessToken);
            context.setResponseStatusCode(STATUS_CODE_NOT_AUTHED);
            context.setSendZuulResponse(false);
            try {
                HttpServletResponse response = context.getResponse();
                response.getWriter().write("Please provide right token!");
            } catch (Exception e) {
                log.error("fail to write response", e);
            }
            return null;
        }else{
            context.setResponseStatusCode(STATUS_CODE_NOT_SUCCESS);
        }
        return null;
    }
}