package fr.ebiz.computerdatabase.mappers;

import java.util.ArrayList;
import java.util.List;

import fr.ebiz.computerdatabase.interfaces.ICompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.models.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper implements ICompanyMapper {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyMapper.class);

    /**
     * Constructor CompanyMapper.
     */
    public CompanyMapper() {
    }

    @Override
    public List<CompanyDTO> toDTO(List<Company> listCompany) {
        List<CompanyDTO> listDTO = new ArrayList<>();

        for (Company company : listCompany) {
            String id = String.valueOf(company.getId());
            String name = company.getName();
            listDTO.add(new CompanyDTO(id, name));
        }

        return listDTO;
    }

    @Override
    public CompanyDTO toDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        String id = String.valueOf(company.getId());
        String name = company.getName();

        companyDTO.setId(id);
        companyDTO.setName(name);

        return companyDTO;
    }
}
