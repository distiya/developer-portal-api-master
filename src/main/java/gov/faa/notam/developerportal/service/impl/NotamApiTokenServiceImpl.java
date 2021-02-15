package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.configuration.PaginationConfig;
import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.repository.NotamApiTokenRepository;
import gov.faa.notam.developerportal.repository.UserRepository;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.NotamApiTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotamApiTokenServiceImpl implements NotamApiTokenService {
    private final NotamApiTokenRepository notamApiTokenRepository;

    private final UserRepository userRepository;

    private final PaginationConfig paginationConfig;

    @Override
    public NotamApiTokenModel createToken(CreateNotamApiTokenRequest request) throws ApiException {
        Optional<Long> userId = SecurityUtil.getCurrentUserId();
        if (userId.isEmpty()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Current user id not found.");
        }

        Optional<User> user = userRepository.findById(userId.get());
        if (user.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Current user not found.");
        }

        NotamApiToken token = new NotamApiToken();
        token.setUser(user.get());
        token.setName(request.getName());
        token.setKey(generateToken());
        token.setDeleted(false);
        token.setEnabledByAdmin(true);
        token.setEnabledByUser(true);
        token.setStatus(NotamApiTokenStatus.active);

        token = notamApiTokenRepository.save(token);

        return new NotamApiTokenModel(token);
    }

    @Override
    public void deleteToken(Long id) throws ApiException {
        NotamApiToken token = getTokenById(id);
        token.setDeleted(true);
        notamApiTokenRepository.save(token);
    }

    @Override
    public NotamApiTokenModel getToken(Long id) throws ApiException {
        NotamApiToken token = getTokenById(id);
        return new NotamApiTokenModel(token);
    }

    @Override
    public void updateToken(Long id, UpdateNotamApiTokenRequest request) throws ApiException {
        NotamApiToken token = getTokenById(id);
        if(request.getName() != null && Boolean.FALSE.equals(request.getName().isBlank())){
            token.setName(request.getName());
        }
        if(request.getStatus() != null){
            token.setStatus(request.getStatus());
        }
        notamApiTokenRepository.save(token);
    }

    @Override
    public void enable(Long id) throws ApiException {
        NotamApiToken token = getTokenById(id);
        switch (SecurityUtil.getCurrentUserRole()) {
            case USER:
                token.setEnabledByUser(true);
                break;
            case ADMIN:
                token.setEnabledByAdmin(true);
                if (isOwner(token)) {
                    token.setEnabledByUser(true);
                }
                break;
        }
        notamApiTokenRepository.save(token);
    }

    @Override
    public void disable(Long id) throws ApiException {
        NotamApiToken token = getTokenById(id);
        switch (SecurityUtil.getCurrentUserRole()) {
            case USER:
                token.setEnabledByUser(false);
                break;
            case ADMIN:
                token.setEnabledByAdmin(false);
                if (isOwner(token)) {
                    token.setEnabledByUser(false);
                }
                break;
        }
        notamApiTokenRepository.save(token);
    }

    @Override
    public SearchResponse<NotamApiTokenModel> searchToken(SearchNotamApiTokenRequest request) throws ApiException {
        UserRole userRole = SecurityUtil.getCurrentUserRole();
        if (userRole == UserRole.USER) {
            // Force user id to be current user id for regular user.
            request.setUserId(SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "Current user id not found.")));
        }

        Page<NotamApiToken> page = notamApiTokenRepository.findAll(new NotamApiTokenSpecification(request),
                paginationConfig.toPageRequest(request));

        SearchResponse<NotamApiTokenModel> response = new SearchResponse<>();
        response.setTotalCount(page.getTotalElements());
        response.setItems(page.get().map(NotamApiTokenModel::new).collect(Collectors.toList()));
        return response;
    }

    /**
     * Generate a base64 encoded token.
     *
     * @return - base64 encoded UUID.
     */
    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        byte[] data = new byte[16];
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits());
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Get token by the id, checking if the current user has permission to access this token.
     *
     * @param id - id of the token to get.
     * @return - the token.
     * @throws ApiException - if token is not found, or the current user don't have permission.
     */
    private NotamApiToken getTokenById(Long id) throws ApiException {
        Optional<NotamApiToken> retrieved = notamApiTokenRepository.findById(id);
        if (retrieved.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Token not found.");
        }
        checkPermission(retrieved.get());
        return retrieved.get();
    }

    /**
     * Check if the current user have permission to access the given token.
     * Admin has permission to all tokens; Regular user has permission to their own tokens.
     *
     * @param token - token to check.
     * @throws ApiException - if current user id couldn't be found, or they don't have permission.
     */
    private void checkPermission(NotamApiToken token) throws ApiException {
        Optional<Long> userId = SecurityUtil.getCurrentUserId();
        if (userId.isEmpty()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Current user id not found.");
        }
        UserRole userRole = SecurityUtil.getCurrentUserRole();
        if (userRole != UserRole.ADMIN || !token.getUser().getId().equals(userId.get())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Regular user can only access their own tokens.");
        }
    }

    /**
     * Check if the current user is the owner of the token.
     *
     * @param token - token to check.
     * @return true if token.user.id equals current user id.
     */
    private boolean isOwner(NotamApiToken token) {
        Optional<Long> userId = SecurityUtil.getCurrentUserId();
        return userId.isPresent() && userId.get().equals(token.getUser().getId());
    }
}
