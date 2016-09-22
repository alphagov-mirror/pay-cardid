package uk.gov.pay.card.filters;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.*;

public class LoggingFilter implements Filter {

    public static final String HEADER_REQUEST_ID = "X-Request-Id";
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Stopwatch stopwatch = Stopwatch.createStarted();

        String requestURL = ((HttpServletRequest) servletRequest).getRequestURI();
        String requestMethod = ((HttpServletRequest) servletRequest).getMethod();
        String requestId = defaultString(((HttpServletRequest) servletRequest).getHeader(HEADER_REQUEST_ID));


        logger.info(format("[%s] - %s to %s began", requestId, requestMethod, requestURL));
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Throwable throwable) {
            logger.error("Exception - cardid request - " + requestURL + " - exception - " + throwable.getMessage(), throwable);
        } finally {
            logger.info(format("[%s] - %s to %s ended - total time %dms", requestId, requestMethod, requestURL,
                    stopwatch.elapsed(TimeUnit.MILLISECONDS)));
            stopwatch.stop();
        }
    }

    @Override
    public void destroy() {
        logger.warn("Destroying logging filter");
    }
}
