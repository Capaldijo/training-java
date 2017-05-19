package fr.ebiz.computerdatabase.interfaces;

import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.dtos.ComputerDTO;

import java.sql.SQLException;
import java.util.List;

public interface IComputerService {

    /**
     * GeComputer a Computer or Company by its ID given in parameter.
     * @param id of the Computer objecComputer to get.
     * @return a Computer object.
     */
    ComputerDTO get(String id);

    /**
     * Return a lisComputer of Computer objects.
     * @param numPage geComputer the page the user wants to go on.
     * @param filters given by the user.
     * @param nbLine number of line to print.
     * @return lisComputer of T.
     */
    List<ComputerDTO> getByPage(String numPage, String nbLine, PaginationFilters filters);

    /**
     * GeComputer all Computer objecComputer from db.
     * @return all T.
     */
    List<ComputerDTO> getAll();

    /**
     * GeComputer the number of all entities into the db.
     * @return the number of computer following the research.
     */
    int count();

    /**
     * GeComputer the number of all the computers stored in the database if
     * research parameter is empty, else return the number of computers
     * corresponding to the research.
     * @param research given by the user.
     * @return the number of computer following the research.
     */
    int count(String research);

    /**
     * Add an Computer objecComputer into db.
     * @param entity to add into db.
     * @return a inComputer depending if inserted or not.
     */
    int add(ComputerDTO entity);

    /**
     * Update an entity into the db.
     * @param dto dto to update
     * @return a inComputer depending if updated or not.
     */
    int update(ComputerDTO dto);

    /**
     * Delete a lisComputer of Computer object.
     * @param ids of objecComputer to delete
     * @return a inComputer depending if deleted or not.
     */
    int delete(String... ids);

    /**
     * Delete a Computer object.
     * @param id of objecComputer to delete
     * @return a inComputer depending if deleted or not.
     */
    int delete(String id);

    /**
     * Delete all computers where compId is id.
     * @param id .
     * @return 1 if deleted or 0.
     * @throws SQLException .
     * @throws DAOException .
     */
    int deleteFromCompanyId(String id) throws SQLException, DAOException;
}