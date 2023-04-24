package com.diego.prandini.exerciciotecnicoelotech.infra.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RepositoryConditionDatabase implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return "database".equals(context.getEnvironment().getProperty("application.inject.repository"));
    }
}
