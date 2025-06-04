package br.ifsp.orm;

import java.sql.SQLException;
import java.util.Optional;

public interface Retrievable <T, K>{
    Optional<T> findOne(K k) throws SQLException;
}
