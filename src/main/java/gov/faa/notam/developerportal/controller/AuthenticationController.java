package gov.faa.notam.developerportal.controller;

import javax.validation.Valid;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.LoginRequest;
import gov.faa.notam.developerportal.model.api.LoginResponse;
import gov.faa.notam.developerportal.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The authentication endpoint.
 * <p>
 * POST /auth/login - user login.
 * </p>
 */
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    /**
     * Authentication service.
     */
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) throws ApiException {
        return authenticationService.login(request);
    }
}
