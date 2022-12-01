package com.shop.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SuccessHandlerImpl implements AuthenticationSuccessHandler {

    //시간 참조 설정.
    public final Integer SESSION_TIMEOUT_IN_SECONDS = 60 * 1;//1 minute

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
        response.sendRedirect("/");
    }
}
