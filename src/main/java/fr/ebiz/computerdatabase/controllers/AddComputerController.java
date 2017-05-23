package fr.ebiz.computerdatabase.controllers;

import java.util.List;
import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.interfaces.ICompanyService;
import fr.ebiz.computerdatabase.interfaces.IComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AddComputerController {

    private static final Logger LOG = LoggerFactory.getLogger(AddComputerController.class);

    @Autowired
    private IComputerService computerService;

    @Autowired
    private ICompanyService companyService;

    @RequestMapping(value = {"/add_computer"}, method = RequestMethod.GET)
    public String addComputer(ModelMap model) {
        try {
            List<CompanyDTO> companiesDTO = companyService.getAll();
            model.addAttribute("companies", companiesDTO);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            return "500";
        }
        return "add_computer";
    }

    @RequestMapping(value = {"/add_computer"}, method = RequestMethod.POST)
    public String addComputer(ComputerDTO computerDTO) {
        try {
            computerService.add(computerDTO);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            return "500";
        }
        return "add_computer";
    }
}
