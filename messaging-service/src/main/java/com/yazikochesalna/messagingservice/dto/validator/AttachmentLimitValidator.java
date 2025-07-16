package com.yazikochesalna.messagingservice.dto.validator;

import com.yazikochesalna.messagingservice.dto.kafka.AttachmentDTO;
import com.yazikochesalna.messagingservice.dto.kafka.AttachmentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class AttachmentLimitValidator implements ConstraintValidator<ValidAttachmentLimits, List<AttachmentDTO>> {
    @Override
    public void initialize(ValidAttachmentLimits constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<AttachmentDTO> attachments, ConstraintValidatorContext context) {
        if (attachments == null) {
            return true;
        }

        long fileCount = attachments.stream()
                .filter(attachment -> attachment.getType() == AttachmentType.FILE)
                .count();

        if (fileCount > 10) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Cannot have more than 10 FILE attachments")
                    .addConstraintViolation();
            return false;
        }

        long replyCount = attachments.stream()
                .filter(attachment -> attachment.getType() == AttachmentType.REPLY)
                .count();


        if (replyCount > 1) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Cannot have more than 1 REPLY attachment")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
