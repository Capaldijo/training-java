package fr.ebiz.computerdatabase.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.models.PaginationFilters;

public interface IComputerDAO {

    /**
     * Get a model by its ID.
     * @param idComp id model.
     * @return A model.
     * @throws DAOException error on getting data.
     */
    Computer find(Long idComp) throws DAOException;

    /**
     * Following the parameters, build a query, depending the research, that get
     * only 10nth lines of the model's table and return them.
     * @param filters given by the user.
     * @param numPage the page the user wants to go on.
     * @param nbLine number of line to print.
     * @return a list of 10 model.
     * @throws DAOException error on getting data.
     */
    List<ComputerDTO> findByPage(PaginationFilters filters, int numPage, int nbLine) throws DAOException;

    /**
     * Count number of model in DB.
     * @return The number of model.
     * @throws DAOException error on getting data.
     */
    int count() throws DAOException;

    /**
     * Count number of model in DB following the research given.
     * @param search type in by the user.
     * @return The number of model depending the research.
     * @throws DAOException error on getting data.
     */
    int countAfterSearch(String search) throws DAOException;

    /**
     * Insert the model given in parameter into the database. Get each of its
     * parameters in order to build the query.
     * @param comp the model to insert
     * @return the result of the query.
     * @throws DAOException error on getting data.
     */
    int insert(Computer comp) throws DAOException;

    /**
     * Update the model given in parameter into the database. Get each of its
     * parameters in order to build the query.
     * @param comp the model to update.
     * @return the result of the query.
     * @throws DAOException error on getting data.
     */
    int update(Computer comp) throws DAOException;

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
     * Delete all model contained in parameter.
     * @param modelsId list of model's id to deletes
     * @throws DAOException error on accessing data.
     * @return res.
     */
    int delete(String...modelsId) throws DAOException;

    /**
     * Delete all computers where compId is id.
     * @param id .
     * @return 1 if deleted or 0.
     * @throws SQLException .
     * @throws DAOException .
     */
    int deleteFromCompanyId(String id) throws SQLException, DAOException;

    /**
     * Build a model from the different data from the ResultSet pass in
     * parameter.
     * @param resultat contains a model
     * @return a model object
     * @throws DAOException error on mapping model
     */
    Computer toModel(ResultSet resultat) throws DAOException;

    /**
     * Build a List of model from the different data from the ResultSet pass
     * in parameter.
     * @param resultat contains all models
     * @return a List of model object
     * @throws DAOException error on mapping model
     */
    List<Computer> toModels(ResultSet resultat) throws DAOException;

    /**
     * Build a modelComputerDTO directly from the db.
     * @param resultat contains a model
     * @return a modelComputerDTO
     * @throws DAOException Error on mapping data
     */
    ComputerDTO toDTO(ResultSet resultat) throws DAOException;

    /**
     * Build a list of modelDTO directly from the db.
     * @param resultat contains all the model from db
     * @return a list of modelDTO
     * @throws DAOException Error on mapping data
     */
    List<ComputerDTO> toDTOs(ResultSet resultat) throws DAOException;
}
