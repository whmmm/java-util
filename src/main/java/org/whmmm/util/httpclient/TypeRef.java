package org.whmmm.util.httpclient;

import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * ParameterizedTypeReference 这个类没法直接指定 Type,
 * 只好手动指定继承喽
 */
class TypeRef extends ParameterizedTypeReference<Object> {
    private final Type type;

    public TypeRef(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }
}