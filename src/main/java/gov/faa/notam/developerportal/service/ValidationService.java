package gov.faa.notam.developerportal.service;

import gov.faa.notam.developerportal.exception.ApiException;

public interface ValidationService {

    /**
     * This has the responsibility to validate a bean according to the defined javax.validation constrains
     *
     * @param request The bean instance to be validated
     * @param <T> The generic type to represent the bean
     * @throws ApiException when validation error
     */
    <T> void validateBean(T request) throws ApiException;

    /**
     * This has the responsibility to validate whether the user has got admin rights to execute the api
     * @throws ApiException
     */
    void validateAdminAccessRight() throws ApiException;

    /**
     * This has the responsibility to validate whether the current user ID is present or not
     * @throws ApiException
     */
    void validateCurrentUserID() throws ApiException;


}
