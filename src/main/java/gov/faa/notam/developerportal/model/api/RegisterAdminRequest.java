package gov.faa.notam.developerportal.model.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Request to register a new Admin user.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterAdminRequest extends AbstractRegisterRequest { }
