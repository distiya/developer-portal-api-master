package gov.faa.notam.developerportal.controller;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.CreateNotamAPIAccessItemRequest;
import gov.faa.notam.developerportal.model.api.NotamApiAccessItemModel;
import gov.faa.notam.developerportal.service.NotamApiAccessItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Notam API access item endpoints.
 * <p>
 * DELETE /notamApiAccessItem/{id} - create token.
 * GET /notamApiAccessItem/{id} - search token.
 * PATCH /notamApiAccessItem/{id} - update token.
 * GET /notamApiAccessItem/{id}/file - search token.
 * GET /notamApiAccessItem - search token.
 * POST /notamApiAccessItem - search token.
 * </P>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class NotamApiAccessItemController {

    private final NotamApiAccessItemService apiAccessItemService;

    @PostMapping(path = "/notamApiAccessItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotamApiAccessItemModel createNotamApiAccessItem(@RequestParam("file") MultipartFile file, CreateNotamAPIAccessItemRequest request) throws ApiException{
        return apiAccessItemService.createAccessItem(file,request);
    }

    @DeleteMapping(path = "/notamApiAccessItem/{id}")
    public void deleteNotamApiAccessItem(@PathVariable("id") Long id) throws ApiException{
        apiAccessItemService.deleteAccessItem(id);
    }

    @GetMapping(path = "/notamApiAccessItem/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public NotamApiAccessItemModel getNotamApiAccessItem(@PathVariable("id") Long id) throws ApiException{
        return apiAccessItemService.getAccessItem(id);
    }

}
