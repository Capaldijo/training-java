package fr.ebiz.computerdatabase.mapper;

import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.model.Company;

import java.util.List;

public interface ICompanyMapper {

    /**
     * Map a model to a DTO.
     * @param model model to map
     * @return a DTO
     */
    CompanyDTO toDTO(Company model);

    /**
     * Map a list of model to list of CompanyDTO.
     * @param list models to map
     * @return a list of CompanyDTOs
     */
    List<CompanyDTO> toDTO(List<Company> list);

}
