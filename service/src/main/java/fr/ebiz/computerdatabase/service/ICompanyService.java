package fr.ebiz.computerdatabase.service;

import java.util.List;

import fr.ebiz.computerdatabase.dto.CompanyDTO;

public interface ICompanyService {

    /**
     * Get a Computer or Company by its ID given in parameter.
     * @param id of the T object to get.
     * @return a T object.
     */
    CompanyDTO get(String id);

    /**
     * Return a list of CompanyDTO objects.
     * @param numPage get the page the user wants to go on.
     * @param nbLine number of line to print.
     * @return list of CompanyDTO.
     */
    List<CompanyDTO> getByPage(int numPage, int nbLine);

    /**
     * Get all CompanyDTO object from db.
     * @return all CompanyDTO.
     */
    List<CompanyDTO> getAll();

    /**
     * Delete a CompanyDTO object.
     * @param id of object to delete
     * @return a int depending if deleted or not.
     */
    int delete(String id);
}
