package fr.ebiz.computerdatabase.controller;

import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping(value = "/api/companies")
public class CompanyRestController {

    @Autowired
    private ICompanyService companyService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> listAllCompanies() {
        List<CompanyDTO> companyDTOS = companyService.getAll();
        if (companyDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(companyDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable("id") String id) {
        CompanyDTO companyDTO;
        try {
            companyDTO = companyService.get(id);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(companyDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") String id) {
        int delete = companyService.delete(id);
        if (delete > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @RequestMapping(value = "/pages/{page}/lines/{line}", method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> listComputersByPage(@PathVariable("page") int page,
                                                                 @PathVariable("line") int line) {
        int numPage = page * line;
        List<CompanyDTO> companyDTOS = companyService.getByPage(numPage, line);
        if (companyDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(companyDTOS, HttpStatus.OK);
    }
}
