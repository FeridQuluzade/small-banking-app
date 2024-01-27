package az.dev.smallbankingapp.util;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class RequestContextUtil {

    private RequestContextUtil() {
    }

    private static Optional<HttpServletRequest> getServletRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(RequestContextUtil::mapServletRequestAttributes)
                .map(RequestContextUtil::mapHttpServletRequest);
    }

    private static ServletRequestAttributes mapServletRequestAttributes(
            RequestAttributes requestAttributes) {
        return ((ServletRequestAttributes) requestAttributes);
    }

    private static HttpServletRequest mapHttpServletRequest(
            ServletRequestAttributes servletRequestAttributes) {
        return servletRequestAttributes.getRequest();
    }

    public static String getPath() {
        return getServletRequest()
                .map(HttpServletRequest::getServletPath)
                .orElse("/");
    }

    public static String getMethod() {
        return getServletRequest()
                .map(HttpServletRequest::getMethod)
                .orElse(RequestMethod.GET.name());
    }

}
