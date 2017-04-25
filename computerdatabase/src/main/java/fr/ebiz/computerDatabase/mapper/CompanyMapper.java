package fr.ebiz.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.model.Company;
import fr.ebiz.computerDatabase.model.CompanyDTO;
import fr.ebiz.computerDatabase.utils.Utils;

public class CompanyMapper {

    final Logger logger = LoggerFactory.getLogger(CompanyMapper.class);

    /**
     * Constructor CompanyMapper.
     */
    public CompanyMapper() {
    }

    /**
     * Get a company from the database and build it as an instance of Company
     * class return a company object.
     * @param resultat contains a company from db.
     * @return a company.
     * @throws MapperException Error on mapping data from db.
     */
    public Company fromDBToCompany(ResultSet resultat) throws MapperException {
        Long id = 0L;
        String name = null;
        try {
            id = resultat.getLong(Utils.COLUMN_ID);
            name = resultat.getString(Utils.COLUMN_NAME);
        } catch (SQLException e) {
            throw new MapperException("[FDBTCOMP] Error on accessing data.");
        }

        return new Company(id, name);
    }

    /**
     * Get a list company from the database and build it as an instance of Company
     * class return a company object list.
     * @param resultat contains all companies from db.
     * @return a list of Companies.
     * @throws MapperException Error on mapping data from db.
     */
    public List<Company> fromDBToCompanies(ResultSet resultat) throws MapperException {
        List<Company> list = new ArrayList<>();

        try {
            while (resultat.next()) {
                list.add(fromDBToCompany(resultat));
            }
        } catch (SQLException sqle) {
            logger.error("Error on reading data from db.");
        }

        return list;
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
