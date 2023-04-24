package com.diego.prandini.exerciciotecnicoelotech.infra.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RepositoryConditionMongo implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return "mongodb".equals(context.getEnvironment().getProperty("application.inject.repository"));
    }
}
