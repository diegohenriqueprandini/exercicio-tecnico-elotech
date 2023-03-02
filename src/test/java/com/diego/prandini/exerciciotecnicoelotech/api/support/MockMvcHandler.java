package com.diego.prandini.exerciciotecnicoelotech.api.support;

import com.diego.prandini.exerciciotecnicoelotech.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.nio.charset.Charset;

public class MockMvcHandler<T> implements ResultHandler {

    private final TypeReference<T> valueTypeRef;

    private T output;

    public MockMvcHandler(TypeReference<T> valueTypeRef) {
        this.valueTypeRef = valueTypeRef;
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        String json = result.getResponse().getContentAsString(Charset.defaultCharset());
        output = JsonUtils.fromJson(json, valueTypeRef);
    }

    public T get() {
        return output;
    }
}