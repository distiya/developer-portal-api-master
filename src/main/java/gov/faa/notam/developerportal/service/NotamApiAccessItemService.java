package gov.faa.notam.developerportal.service;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.CreateNotamAPIAccessItemRequest;
import gov.faa.notam.developerportal.model.api.NotamApiAccessItemModel;
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


}
