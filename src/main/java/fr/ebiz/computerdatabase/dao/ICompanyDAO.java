package fr.ebiz.computerdatabase.dao;

import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.exception.DAOException;
import fr.ebiz.computerdatabase.model.Company;

import java.sql.SQLException;
import java.util.List;

public interface ICompanyDAO {

    /**
     * Get a model by its ID.
     * @param id id model
     * @return a model
     * @throws DAOException Error on getting data.
     */
    Company find(Long id) throws DAOException;

    /**
     * Get all the model from the db.
     * @return All models.
     * @throws DAOException error on getting data.
     */
    List<Company> findAll() throws DAOException;

    /**
     * Following the parameters, build a query, depending the research, that get
     * only 10nth lines of the model's table and return them.
     * @param numPage the page the user wants to go on.
     * @param nbLine number of line to print.
     * @return a list of 10 model.
     * @throws DAOException error on getting data.
     */
    List<CompanyDTO> findByPage(int numPage, int nbLine) throws DAOException;

    /**
     * According to the given id in parameter, build the query, find and delete
     * it from the database.
     * @param id the model id to delete.
     * @return int depending the delete is done or not.
     * @throws SQLException error on co to db.
     * @throws DAOException Error on accessing data.
     */
    int delete(String id) throws SQLException, DAOException;
}
