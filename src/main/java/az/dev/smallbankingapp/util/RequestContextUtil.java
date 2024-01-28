package az.dev.smallbankingapp.util;

import az.dev.smallbankingapp.entity.UserType;
import az.dev.smallbankingapp.security.UserPrincipal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class RequestContextUtil {

    private RequestContextUtil() {
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

    public static UserPrincipal getPrincipal() {
        Optional<Authentication> authentication =
                Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        return authentication
                .map(Authentication::getPrincipal)
                .filter(UserPrincipal.class::isInstance)
                .map(UserPrincipal.class::cast)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Principal not found in authentication"));
    }

    public static String getUsername() {
        return getPrincipal().getUsername();
    }

    public static UserType getUserType() {
        return getPrincipal().getUserType();
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

}
