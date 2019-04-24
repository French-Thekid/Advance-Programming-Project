package database;

import communication.Response;

import java.util.List;

public interface TableManager<T> {

    SQLProvider sql = SQLProvider.getInstance();

    boolean create(T item, byte[] image);

    T retrieve(int id);

    List<T> retrieveAll();

    boolean delete(int id);

    boolean update(T item);
}
