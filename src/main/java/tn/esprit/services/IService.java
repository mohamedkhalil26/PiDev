package tn.esprit.services;

import java.sql.SQLException;
import java.util.List;
public interface IService<T> {

    void ajouter(T t) throws SQLException;
    void ajouterP(T t) throws SQLException;
    List<T> returnList() throws SQLException;
    void supprimer(T t);
    void modifier(T t);
}
