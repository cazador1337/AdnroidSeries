package com.example.joao.projectfinal.banco.sql.projectfinal.banco.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface PrimaryKey {
    boolean isPrymaryKey = true;

    boolean auto_generated();
}
