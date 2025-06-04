package br.ifsp.orm;

import java.util.Optional;

public interface Retrievable <T, K>{
    Optional<T> findOne(K k);
}
