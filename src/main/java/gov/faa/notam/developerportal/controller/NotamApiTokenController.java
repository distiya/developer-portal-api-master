package gov.faa.notam.developerportal.controller;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import gov.faa.notam.developerportal.service.NotamApiTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Notam API token endpoints.
 * <p>
 * POST /notamApiToken - create token.
 * GET /notamApiToken - search token.
 * GET /notamApiToken/{id} - get token by the id.
 * PATCH /notamApiToken/{id} - update token.
 * DELETE /notamApiToken/{id} - delete token by the id.
 * PUT /notamApiToken/{id}/enable - enable token by the id.
 * PUT /notamApiToken/{id}/disable - disable token by the id.
 * </P>
 */
@RestController
@RequiredArgsConstructor
public class NotamApiTokenController {
    private final NotamApiTokenService notamApiTokenService;

    @PostMapping("/notamApiToken")
    public NotamApiTokenModel createToken(@RequestBody @Valid CreateNotamApiTokenRequest request) throws ApiException {
        return notamApiTokenService.createToken(request);
    }

    @GetMapping("/notamApiToken")
    public SearchResponse<NotamApiTokenModel> searchToken(SearchNotamApiTokenRequest request)
            throws ApiException {
        return notamApiTokenService.searchToken(request);
    }

    @GetMapping("/notamApiToken/{id}")
    public NotamApiTokenModel getToken(@PathVariable("id") @NotNull Long id) throws ApiException {
        return notamApiTokenService.getToken(id);
    }

    @PatchMapping("/notamApiToken/{id}")
    public void updateToken(@PathVariable("id") @NotNull Long id,
            @RequestBody @Valid UpdateNotamApiTokenRequest request) throws ApiException {
        notamApiTokenService.updateToken(id, request);
    }

    @DeleteMapping("/notamApiToken/{id}")
    public void deleteToken(@PathVariable("id") @NotNull Long id) throws ApiException {
        notamApiTokenService.deleteToken(id);
    }

    @PutMapping("/notamApiToken/{id}/enable")
    public void enableToken(@PathVariable("id") @NotNull Long id) throws ApiException {
        notamApiTokenService.enable(id);
    }

    @PutMapping("/notamApiToken/{id}/disable")
    public void disableToken(@PathVariable("id") @NotNull Long id) throws ApiException {
        notamApiTokenService.disable(id);
    }
}
