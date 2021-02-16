package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationServiceImpl implements ValidationService {

    private final Validator validator;

    @Override
    public <T> void validateBean(T request) throws ApiException {
        Set<ConstraintViolation<T>> validationErrors = validator.validate(request);
        if(Boolean.FALSE.equals(CollectionUtils.isEmpty(validationErrors))){
            ConstraintViolation<T> constraintViolation = validationErrors.stream().findFirst().orElse(null);
            throw new ApiException(HttpStatus.BAD_REQUEST, constraintViolation.getMessage());
        }
    }

    @Override
    public void validateAdminAccessRight() throws ApiException {
        validateCurrentUserID();
        UserRole userRole = SecurityUtil.getCurrentUserRole();
        if (userRole == null || userRole != UserRole.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Admin user right is required");
        }
    }

    @Override
    public void validateCurrentUserID() throws ApiException {
        Optional<Long> userId = SecurityUtil.getCurrentUserId();
        if (userId.isEmpty()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Current user id is not found");
        }
    }

}
