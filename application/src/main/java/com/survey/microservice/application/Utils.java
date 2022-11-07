package com.survey.microservice.application;

import java.util.Optional;

public class Utils {
    @FunctionalInterface
    public interface TriConsumer<T, U> {
        void accept(T t, U u);
    }

    public static <T, U> void allOf(Optional<T> o1, Optional<U> o2,
                                       TriConsumer<T, U> consumer) {
        o1.ifPresent(t -> o2.ifPresent(u -> consumer.accept(t, u)));
    }
}
