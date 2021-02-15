package gov.faa.notam.developerportal.service;

import javax.transaction.Transactional;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.CreateNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.api.NotamApiTokenModel;
import gov.faa.notam.developerportal.model.api.SearchNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.api.SearchResponse;
import gov.faa.notam.developerportal.model.api.UpdateNotamApiTokenRequest;

/**
 * API token management service.
 */
@Transactional
public interface NotamApiTokenService {
    /**
     * Create a new token. Generate a GUID and base64 encode it as the key.
     *
     * @param request - token creation request.
     * @return the token
     * @throws ApiException - if any error occurs.
     */
    NotamApiTokenModel createToken(CreateNotamApiTokenRequest request) throws ApiException;

    /**
     * Delete a token by the id. Set the token's is_deleted flag to true.
     * Admins can delete any token; Regular user can only tokens created by themselves.
     *
     * @param id - id of the token to delete.
     * @throws ApiException - if any error occurs.
     */
    void deleteToken(Long id) throws ApiException;

    /**
     * Get token by the id.
     * Admins can get any token; Regular user can only see tokens created by themselves.
     *
     * @param id - id of the token to get.
     * @return the token.
     * @throws ApiException - if any error occurs.
     */
    NotamApiTokenModel getToken(Long id) throws ApiException;

    /**
     * Update token by the id.
     * Admins can update any token; Regular user can only update tokens created by themselves.
     *
     * @param id      - id of the token to update.
     * @param request - the token update request.
     * @throws ApiException - if any error occurs.
     */
    void updateToken(Long id, UpdateNotamApiTokenRequest request) throws ApiException;

    /**
     * Enable token by the id.
     * Admins can enable any token; Regular user can only enable tokens created by themselves.
     * Special case: when an admin enables their own tokens, should set both enabledByUser and enabledByAdmin to true.
     *
     * @param id - id of the token to enable.
     * @throws ApiException - if any error occurs.
     */
    void enable(Long id) throws ApiException;

    /**
     * Disable token by the id.
     * Admins can disable any token; Regular user can only disable tokens created by themselves.
     * Special case: when an admin disables their own tokens, should set both enabledByUser and enabledByAdmin to false.
     *
     * @param id - id of the token to disable.
     * @throws ApiException - if any error occurs.
     */
    void disable(Long id) throws ApiException;

    /**
     * Search token by user id and name.
     * For regular user, the user id must always the current user id.
     *
     * @param request - the search request.
     * @return a search response with total count and current page items.
     * @throws ApiException - if any error occurs.
     */
    SearchResponse<NotamApiTokenModel> searchToken(SearchNotamApiTokenRequest request) throws ApiException;
}
