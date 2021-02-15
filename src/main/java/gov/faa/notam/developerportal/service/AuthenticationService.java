package gov.faa.notam.developerportal.service;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.LoginRequest;
import gov.faa.notam.developerportal.model.api.LoginResponse;

import javax.transaction.Transactional;

/**
 * The authentication service responsible for authenticate a user and issue access tokens.
 */
@Transactional
public interface AuthenticationService {
    /**
     * Handles user login request.
     *
     * @param request - the request with email and password.
     * @return response object with access token if login success.
     * @throws ApiException - if any error occurs.
     */
    LoginResponse login(LoginRequest request) throws ApiException;
}
