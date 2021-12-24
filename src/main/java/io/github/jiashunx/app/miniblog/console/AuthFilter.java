package io.github.jiashunx.app.miniblog.console;

import io.github.jiashunx.app.miniblog.service.ArgumentService;
import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.app.miniblog.service.UserService;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.filter.Filter;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilter;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilterChain;
import io.github.jiashunx.masker.rest.framework.util.StringUtils;
import io.github.jiashunx.app.miniblog.model.entity.LoginUserEntity;
import io.github.jiashunx.masker.rest.jjwt.MRestJWTHelper;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.Objects;

/**
 * @author jiashunx
 */
@Filter(order = Integer.MIN_VALUE)
public class AuthFilter implements MRestFilter {

    private static final String COOKIE_PATH = "/";
    private static final String COOKIE_KEY = "MINI-BLOG";
    private static final long COOKIE_TIMEOUT_MILLIS = 60*60*1000L;
    private static final String CONSOLE_LOGIN_URL = "/console/login";
    private static final String CONSOLE_LOGIN_URL_HTML = "/console/login.html";
    private static final String CONSOLE_URL_PREFIX = "/console/";
    private static final String WEBJARS_URL_PREFIX = "/webjars/";
    private static final String[] IGNORE_URL_PREFIXES = new String[]{
            WEBJARS_URL_PREFIX
            , FileManageServlet.FILE_DOWNLOAD_URL_PREFIX
            , FileManageServlet.FILE_OVERVIEW_URL_PREFIX
    };

    private final ServiceBus serviceBus;

    public AuthFilter(ServiceBus serviceBus) {
        this.serviceBus = Objects.requireNonNull(serviceBus);
    }

    @Override
    public void doFilter(MRestRequest request, MRestResponse response, MRestFilterChain filterChain) {
        String requestUrl = request.getUrl();
        if (!requestUrl.startsWith(CONSOLE_URL_PREFIX) || requestUrl.equals(CONSOLE_LOGIN_URL_HTML)) {
            filterChain.doFilter(request, response);
            return;
        }
        for (String prefix: IGNORE_URL_PREFIXES) {
            if (requestUrl.startsWith(prefix)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        ArgumentService argumentService = serviceBus.getArgumentService();
        UserService userService = serviceBus.getUserService();
        MRestJWTHelper jwtHelper = new MRestJWTHelper(argumentService.getJwtSecretKey());
        if (requestUrl.equals(CONSOLE_LOGIN_URL) && HttpMethod.POST.equals(request.getMethod())) {
            LoginUserEntity userVo = request.parseBodyToObj(LoginUserEntity.class);
            if (userService.checkValidation(userVo.getUsername(), userVo.getPassword())) {
                String jwtToken = jwtHelper.newToken();
                Cookie jwtCookie = new DefaultCookie(COOKIE_KEY, jwtToken);
                jwtCookie.setPath(COOKIE_PATH);
                jwtCookie.setMaxAge(COOKIE_TIMEOUT_MILLIS);
                response.setCookie(jwtCookie);
                response.writeString("success");
            } else {
                response.write(HttpResponseStatus.UNAUTHORIZED);
            }
            return;
        }
        Cookie cookie = request.getCookie(COOKIE_KEY);
        String jwtToken = cookie == null ? null : cookie.value();
        if (StringUtils.isEmpty(jwtToken)
                || StringUtils.isNotEmpty(jwtToken) && (jwtHelper.isTokenTimeout(jwtToken) || !jwtHelper.isTokenValid(jwtToken))) {
            response.redirect(CONSOLE_LOGIN_URL_HTML);
            return;
        } else {
            // 校验请求的server标识
            Cookie cookie1 = request.getCookie(Constants.HTTP_HEADER_SERVER_IDENTIFIER);
            if (cookie1 != null) {
                if (!request.getRestContext().getRestServer().getIdentifier().equals(cookie1.value())) {
                    response.redirect(CONSOLE_LOGIN_URL_HTML);
                    return;
                }
            }
            String newToken = jwtHelper.updateToken(jwtToken);
            Cookie jwtCookie = new DefaultCookie(COOKIE_KEY, newToken);
            jwtCookie.setPath(COOKIE_PATH);
            jwtCookie.setMaxAge(COOKIE_TIMEOUT_MILLIS);
            response.setCookie(jwtCookie);
        }
        filterChain.doFilter(request, response);
    }

}
