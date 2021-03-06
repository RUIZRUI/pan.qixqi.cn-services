package cn.qixqi.pan.authentication.util;

import cn.qixqi.pan.authentication.config.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Autowired
    private ServiceConfig config;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        UserContextHolder.get().setTraceId(
                httpServletRequest.getHeader(config.getCtxKeyTraceId()));

        // UserContextHolder.get().setTraceId("000000-001212-2232323");
        logger.debug("UserContext trace_id: " + UserContextHolder.get().getTraceId());

        chain.doFilter(httpServletRequest, response);
    }
}
