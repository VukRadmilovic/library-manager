package rs.ac.uns.ftn.db.dao;

import java.util.List;

public interface Dao<T> {
    
    T get(int id);
    
    List<T> getAll();
    
    void add(T t);
    
    void update(T newT);
    
    void delete(int id);
}