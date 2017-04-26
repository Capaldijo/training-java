package fr.ebiz.computerdatabase.interfaces;

import java.util.List;

public interface ServiceInterface<T> {

    /**
     * Get a Computer or Company by its ID given in parameter.
     * @param id of the T object to get.
     * @return a T object.
     */
    T get(String id);

    /**
     * Return a list of T objects.
     * @param numPage get the page the user wants to go on.
     * @param research given by the user.
     * @param nbLine number of line to print.
     * @return list of T.
     */
    List<T> getByPage(String numPage, String research, String nbLine);

    /**
     * Get all T object from db.
     * @return all T.
     */
    List<T> getAll();

    /**
     * Get the number of all entities into the db.
     * @return the number of computer following the research.
     */
    int count();

    /**
     * Get the number of all the computers stored in the database if
     * research parameter is empty, else return the number of computers
     * corresponding to the research.
     * @param research given by the user.
     * @return the number of computer following the research.
     */
    int count(String research);

    /**
     * Add an T object into db.
     * @param entity to add into db.
     * @return a int depending if inserted or not.
     */
    int add(T entity);

    /**
     * Update an entity into the db.
     * @param dto dto to update
     * @return a int depending if updated or not.
     */
    int update(T dto);

    /**
     * Delete a list of T object.
     * @param ids of object to delete
     * @return a int depending if deleted or not.
     */
    int delete(String...ids);

    /**
     * Delete a T object.
     * @param id of object to delete
     * @return a int depending if deleted or not.
     */
    int delete(String id);
}
