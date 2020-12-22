package io.github.jiashunx.miniblog.console;

import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilter;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilterChain;
import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.masker.rest.framework.util.StringUtils;
import io.github.jiashunx.miniblog.MiniBlogBoot;
import io.github.jiashunx.miniblog.database.DatabaseClient;
import io.github.jiashunx.miniblog.model.LoginUserVo;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.Objects;

/**
 * @author jiashunx
 */
public class AuthFilter implements MRestFilter {

    private static final String COOKIE_PATH = "/";
    private static final String COOKIE_KEY = "MINI-BLOG";
    private static final long COOKIE_TIMEOUT_MILLIS = 60*60*1000L;

    private final MiniBlogBoot blogBoot;
    private final DatabaseClient databaseClient;

    public AuthFilter(MiniBlogBoot blogBoot) {
        this.blogBoot = Objects.requireNonNull(blogBoot);
        this.databaseClient = this.blogBoot.getDatabaseClient();
    }

    @Override
    public void doFilter(MRestRequest request, MRestResponse response, MRestFilterChain filterChain) {
        String requestUrl = request.getUrl();
        if (!requestUrl.startsWith("/console")) {
            filterChain.doFilter(request, response);
            return;
        }
        MRestJWTHelper jwtHelper = databaseClient.getConfigVo().getCacheObj().getJwtHelper();
        if (requestUrl.equals("/console/login") && HttpMethod.POST.equals(request.getMethod())) {
            LoginUserVo userVo = request.parseBodyToObj(LoginUserVo.class);
            LoginUserVo loginUserVo = databaseClient.getConfigVo().getCacheObj().getLoginUserVo();
            if (loginUserVo.getUsername().equals(userVo.getUsername()) && loginUserVo.getPassword().equals(userVo.getPassword())) {
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
            response.redirect("/login.html");
            return;
        } else {
            String newToken = jwtHelper.updateToken(jwtToken);
            Cookie jwtCookie = new DefaultCookie(COOKIE_KEY, newToken);
            jwtCookie.setPath(COOKIE_PATH);
            jwtCookie.setMaxAge(COOKIE_TIMEOUT_MILLIS);
            response.setCookie(jwtCookie);
        }
        filterChain.doFilter(request, response);
    }

}
