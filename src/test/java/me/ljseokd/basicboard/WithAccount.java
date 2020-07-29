package me.ljseokd.basicboard;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@WithSecurityContext(factory = WithAccountSecurityContextFactory.class)
public @interface WithAccount {
    String value();
}
