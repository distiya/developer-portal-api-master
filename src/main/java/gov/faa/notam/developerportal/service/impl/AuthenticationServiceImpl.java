package gov.faa.notam.developerportal.service.impl;

import java.util.Optional;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.LoginRequest;
import gov.faa.notam.developerportal.model.api.LoginResponse;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.repository.UserRepository;
import gov.faa.notam.developerportal.security.JwtTokenProvider;
import gov.faa.notam.developerportal.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the authentication service.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String AUTHENTICATION_FAILED =
            "Authentication failed, either the provided email does not exist, or the password is wrong.";

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for checking the password match the saved hash.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * For generating the JWT token.
     */
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request) throws ApiException {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()
                || !user.get().isEnabled()
                || user.get().getApproved() == null
                || !user.get().getApproved()
                || !user.get().isEmailConfirmed()
                || !passwordEncoder.matches(request.getPassword(), user.get().getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, AUTHENTICATION_FAILED);
        }
        return new LoginResponse(jwtTokenProvider.generateToken(user.get()));
    }
}
