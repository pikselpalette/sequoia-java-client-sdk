package com.piksel.sequoia.clientsdk.endpoint;

import org.hibernate.validator.constraints.NotBlank;

import com.piksel.sequoia.clientsdk.validation.Validatable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Password implements Validatable {

    private String username;

    private String resetToken;

    @NotBlank
    private String newPassword;

}
