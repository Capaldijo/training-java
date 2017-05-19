package fr.ebiz.computerdatabase.interfaces;

import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.models.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ICompanyDAO {

    /**
     * Get a model by its ID.
     * @param id id model
     * @return a model
     * @throws DAOException Error on getting data.
     */
    Company find(int id) throws DAOException;

    /**
     * Get a model by its ID.
     * @param idComp id model.
     * @return A model.
     * @throws DAOException error on getting data.
     */
    Company find(Long idComp) throws DAOException;

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

    /**
     * Build a model from the different data from the ResultSet pass in
     * parameter.
     * @param resultat contains a model
     * @return a model object
     * @throws DAOException error on mapping model
     */
    Company toModel(ResultSet resultat) throws DAOException;

    /**
     * Build a List of model from the different data from the ResultSet pass
     * in parameter.
     * @param resultat contains all models
     * @return a List of model object
     * @throws DAOException error on mapping model
     */
    List<Company> toModels(ResultSet resultat) throws DAOException;

    /**
     * Build a modelCompanyDTO directly from the db.
     * @param resultat contains a model
     * @return a modelCompanyDTO
     * @throws DAOException Error on mapping data
     */
    CompanyDTO toDTO(ResultSet resultat) throws DAOException;

    /**
     * Build a list of modelCompanyDTO directly from the db.
     * @param resultat contains all the model from db
     * @return a list of modelCompanyDTO
     * @throws DAOException Error on mapping data
     */
    List<CompanyDTO> toDTOs(ResultSet resultat) throws DAOException;
}
