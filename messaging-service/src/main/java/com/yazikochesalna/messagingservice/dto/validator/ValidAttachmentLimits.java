package com.yazikochesalna.messagingservice.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = AttachmentLimitValidator.class)
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface ValidAttachmentLimits {
    String message() default "Invalid number of attachments";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
