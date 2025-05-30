package hello.exception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("logFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        log.info("logFilter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST [{}][{}][{}]", uuid, servletRequest.getDispatcherType(),requestURI);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch(Exception e){
            throw e;
        }finally {
            log.info("REQUEST [{}][{}][{}]", uuid, servletRequest.getDispatcherType(),requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("logFilter destroy");
    }


}
