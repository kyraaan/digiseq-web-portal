package com.digiseq.digiseqwebportal.controller.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({PARAMETER, FIELD, RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PersonnelIdValidator.class)
public @interface ValidPersonnelId {

  String message() default "Invalid personnelId";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
