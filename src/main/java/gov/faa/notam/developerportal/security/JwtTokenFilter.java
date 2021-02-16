package gov.faa.notam.developerportal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.faa.notam.developerportal.exception.AuthenticationErrorPrinter;
import gov.faa.notam.developerportal.model.api.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        if (Boolean.TRUE.equals(StringUtils.hasText(token))) {
            try{
                boolean isTokenValid = jwtTokenProvider.validateToken(token);
                if(Boolean.TRUE.equals(isTokenValid)){
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                filterChain.doFilter(servletRequest, servletResponse);
            }
            catch (BadCredentialsException ex){
                sendAuthErrorResponse(servletResponse);
            }
        }
        else{
            AuthenticationErrorPrinter pw = new AuthenticationErrorPrinter();
            HttpServletResponse wrappedResp = new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {
                @Override
                public PrintWriter getWriter() {
                    return pw.getWriter();
                }

                @Override
                public ServletOutputStream getOutputStream() {
                    return pw.getStream();
                }

            };
            filterChain.doFilter(servletRequest, wrappedResp);
            convertAndSendAuthError(servletResponse,wrappedResp,pw);
        }
    }

    private void sendAuthErrorResponse(ServletResponse servletResponse){
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        ApiError apiError = new ApiError("JWT token is either expired or invalid");
        try {
            servletResponse.getOutputStream().print(objectMapper.writeValueAsString(apiError));
        } catch (IOException e) {
        }
    }

    private void convertAndSendAuthError(ServletResponse servletResponse, HttpServletResponse wrappedResp, AuthenticationErrorPrinter pw){
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if(httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()){
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            wrappedResp.setStatus(HttpStatus.UNAUTHORIZED.value());
            ApiError apiError = new ApiError("JWT token is missing");
            try {
                byte[] payloadBytes = objectMapper.writeValueAsString(apiError).getBytes(StandardCharsets.UTF_8);
                servletResponse.getOutputStream().write(payloadBytes);
                httpServletResponse.flushBuffer();
            } catch (IOException e) {
            }
        }
        else{
            try {
                servletResponse.getOutputStream().write(pw.toByteArray());
                httpServletResponse.flushBuffer();
            } catch (IOException e) {
            }
        }
    }
}
