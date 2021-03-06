package gov.faa.notam.developerportal.service;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

/**
 * API access item management service.
 */
@Transactional
public interface NotamApiAccessItemService {

    /**
     * Create a new access item
     *
     * @param file The file contains the content of access item.
     * @param request - access item creation request.
     * @return the access item
     * @throws ApiException - if any error occurs.
     */
    NotamApiAccessItemModel createAccessItem(MultipartFile file, CreateNotamAPIAccessItemRequest request) throws ApiException;

    /**
     * Delete access item
     *
     * @param id the record id of the access item
     * @return the access item
     * @throws ApiException - if any error occurs.
     */
    void deleteAccessItem(Long id) throws ApiException;

    /**
     * Get access item
     *
     * @param id the record id of the access item
     * @return the access item
     * @throws ApiException - if any error occurs.
     */
    NotamApiAccessItemModel getAccessItem(Long id) throws ApiException;

    /**
     * Get access item file
     *
     * @param id the record id of the access item
     * @return the access item file
     * @throws ApiException - if any error occurs.
     */
    byte[] getAccessItemFile(Long id) throws ApiException;

    /**
     * Update access item
     *
     * @param id the record id of the access item
     * @param request the object holding to be updated values
     * @return the access item
     * @throws ApiException - if any error occurs.
     */
    void updateAccessItem(Long id, UpdateNotamApiAccessItemModel request) throws ApiException;

    /**
     * Update access item
     *
     * @param request the object holding query parameters
     * @return the search result of access item
     * @throws ApiException - if any error occurs.
     */
    NotamApiAccessItemModelSearchResult searchAccessItem(SearchNotamApiAccessItemRequest request) throws ApiException;


}
