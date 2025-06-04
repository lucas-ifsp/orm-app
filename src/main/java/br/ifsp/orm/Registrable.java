package br.ifsp.orm;

import java.sql.SQLException;

public interface Registrable <T>{
    void save(T t) throws SQLException;
}
