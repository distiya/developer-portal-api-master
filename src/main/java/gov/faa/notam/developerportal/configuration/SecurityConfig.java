package gov.faa.notam.developerportal.configuration;

import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.security.JwtConfigurer;
import gov.faa.notam.developerportal.security.JwtTokenProvider;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Main security config.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String AUTH_LOGIN = "/auth/login";
    private static final String REGISTER_API_USER = "/user/register/apiUser";
    private static final String REGISTER_ADMIN = "/user/register/admin";
    private static final String VERIFY_USER_EMAIL = "/user/*/verifyEmail";
    private static final String RESET_PASSWORD_REQUEST = "/user/password/requestReset";
    private static final String RESET_PASSWORD = "/user/*/password/reset";
    private static final String USER_APPROVE = "/user/*/approve";
    private static final String USER_DENY = "/user/*/deny";
    private static final String USER_DISABLE = "/user/*/disable";
    private static final String USER_ENABLE = "/user/*/enable";
    private static final String USER_BY_ID = "/user/*";

    /**
     * The jwt token provider.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Password hash secret.
     */
    @Value("${security.secret.pbkdf2}")
    private String pbkdf2Secret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        
        // Setup URL authorization settings.
        http.cors().and().authorizeRequests()
                .antMatchers(HttpMethod.POST, REGISTER_API_USER, AUTH_LOGIN, VERIFY_USER_EMAIL, RESET_PASSWORD,
                        RESET_PASSWORD_REQUEST)
                .permitAll();
        http.cors().and().authorizeRequests()
                .antMatchers(HttpMethod.POST, REGISTER_ADMIN, USER_APPROVE, USER_DENY)
                .hasRole(UserRole.ADMIN.toString());
        http.cors().and().authorizeRequests()
                .antMatchers(HttpMethod.PUT, USER_ENABLE, USER_DISABLE)
                .hasRole(UserRole.ADMIN.toString());
        http.cors().and().authorizeRequests().antMatchers(HttpMethod.DELETE, USER_BY_ID).hasRole(UserRole.ADMIN.toString());
        http.cors().and().authorizeRequests().anyRequest().authenticated();

        // Setup JWT authentication filter.
        http.apply(new JwtConfigurer(jwtTokenProvider));
    }
    
    /**
     * Cors configuration bean
     * 
     * @return cors sourse
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * The password encoder bean.
     *
     * @return the password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder(pbkdf2Secret);
    }
}
