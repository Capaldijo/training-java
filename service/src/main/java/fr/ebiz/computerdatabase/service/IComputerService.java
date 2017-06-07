package fr.ebiz.computerdatabase.service;

import fr.ebiz.computerdatabase.dao.DAOException;
import fr.ebiz.computerdatabase.model.utils.PaginationFilters;
import fr.ebiz.computerdatabase.dto.ComputerDTO;

import java.util.List;

public interface IComputerService {

    /**
     * Get a Computer or Company by its ID given in parameter.
     * @param id of the Computer objecComputer to get.
     * @return a Computer object.
     */
    ComputerDTO get(String id);

    /**
     * Return all computers stored in DB.
     * @return List of ComputerDTO.
     */
    List<ComputerDTO> getAll();

    /**
     * Return a list of Computer objects.
     * @param numPage geComputer the page the user wants to go on.
     * @param filters given by the user.
     * @param nbLine number of line to print.
     * @return list of computerDTO.
     */
    List<ComputerDTO> getByPage(int numPage, int nbLine, PaginationFilters filters);

    /**
     * Get the number of all the computers stored in the database if
     * research parameter is empty, else return the number of computers
     * corresponding to the research.
     * @param research given by the user.
     * @return the number of computer following the research.
     */
    int count(String research);

    /**
     * Add an Computer object into db.
     * @param entity to add into db.
     * @return an int depending if inserted or not.
     * @throws DAOException .
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
     * @param ids of object to delete
     * @return a inComputer depending if deleted or not.
     */
    int deleteComputers(String ids);

    /**
     * Delete a Computer object.
     * @param id of object to delete
     * @return a inComputer depending if deleted or not.
     */
    int delete(String id);
}
