package gov.faa.notam.developerportal.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.ChangePasswordRequest;
import gov.faa.notam.developerportal.model.api.ForgotPasswordRequest;
import gov.faa.notam.developerportal.model.api.RegisterAdminRequest;
import gov.faa.notam.developerportal.model.api.RegisterUserRequest;
import gov.faa.notam.developerportal.model.api.ResetPasswordRequest;
import gov.faa.notam.developerportal.model.api.SearchResponse;
import gov.faa.notam.developerportal.model.api.SearchUserRequest;
import gov.faa.notam.developerportal.model.api.UpdateUserRequest;
import gov.faa.notam.developerportal.model.api.UserModel;
import gov.faa.notam.developerportal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User management and security endpoints.
 * <p>
 *
 * </p>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    /**
     * User service.
     */
    private final UserService userService;

    @PostMapping("/user/register/apiUser")
    public void registerUser(@RequestBody @Valid RegisterUserRequest request) throws ApiException {
        userService.registerUser(request);
    }

    @PostMapping("/user/register/admin")
    public void registerAdmin(@RequestBody @Valid RegisterAdminRequest request) throws ApiException {
        userService.registerAdmin(request);
    }

    @DeleteMapping("/user/{id}")
    public void deleteAdmin(@PathVariable("id") @NotNull Long userId) throws ApiException {
        userService.deleteAdmin(userId);
    }

    @GetMapping("/user/{id}")
    public UserModel getUser(@PathVariable("id") @NotNull Long userId) throws ApiException {
        return userService.getUser(userId);
    }

    @PatchMapping("/user/{id}")
    public void updateUser(@PathVariable("id") @NotNull Long userId, @RequestBody @Valid UpdateUserRequest request)
            throws ApiException {
        userService.updateUser(userId, request);
    }

    @GetMapping("/user")
    public SearchResponse<UserModel> searchUser(SearchUserRequest request) throws ApiException {
        return userService.searchUser(request);
    }

    @PostMapping("/user/{id}/verifyEmail")
    public void verifyEmail(@PathVariable("id") @NotNull Long userId, @RequestParam("code") @NotBlank String code)
            throws ApiException {
        userService.verifyEmail(userId, code);
    }

    @PostMapping("/user/{id}/approve")
    public void approve(@PathVariable("id") @NotNull Long userId) throws ApiException {
        userService.approveUser(userId);
    }

    @PostMapping("/user/{id}/deny")
    public void deny(@PathVariable("id") @NotNull Long userId) throws ApiException {
        userService.denyUser(userId);
    }

    @PostMapping("/user/password/requestReset")
    public void forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) throws ApiException {
        userService.forgotPassword(request);
    }

    @PostMapping("/user/{id}/password/reset")
    public void resetPassword(@PathVariable("id") @NotNull Long userId,
            @RequestBody @Valid ResetPasswordRequest request) throws ApiException {
        userService.resetPassword(userId, request);
    }

    @PutMapping("/user/{id}/enable")
    public void enable(@PathVariable("id") @NotNull Long userId) throws ApiException {
        userService.enableUser(userId);
    }

    @PutMapping("/user/{id}/disable")
    public void disable(@PathVariable("id") @NotNull Long userId) throws ApiException {
        userService.disableUser(userId);
    }

    @PutMapping("/user/{id}/password")
    public void changePassword(@PathVariable("id") @NotNull Long userId,
            @RequestBody @Valid ChangePasswordRequest request) throws ApiException {
        userService.changePassword(userId, request);
    }
}
