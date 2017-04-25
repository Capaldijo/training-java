package fr.ebiz.computerdatabase.mapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.model.Company;
import fr.ebiz.computerdatabase.model.CompanyDTO;

public class CompanyMapper {

    final Logger logger = LoggerFactory.getLogger(CompanyMapper.class);

    /**
     * Constructor CompanyMapper.
     */
    public CompanyMapper() {
    }

    /**
     * From a list of Companies, maps it into a list of CompanyDTOs.
     * @param listCompany list of companies to map
     * @return a list of CompanyDTO
     */
    public List<CompanyDTO> toDTO(List<Company> listCompany) {
        List<CompanyDTO> listDTO = new ArrayList<>();

        for (Company company : listCompany) {
            String id = String.valueOf(company.getId());
            String name = company.getName();
            listDTO.add(new CompanyDTO(id, name));
        }

        return listDTO;
    }

    /**
     * From a company, maps it into a CompanyDTO.
     * @param company to map into companyDTO
     * @return a CompanyDTO
     */
    public CompanyDTO toDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        String id = String.valueOf(company.getId());
        String name = company.getName();

        companyDTO.setId(id);
        companyDTO.setName(name);

        return companyDTO;
    }
}
