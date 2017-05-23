package fr.ebiz.computerdatabase.controllers;

import java.util.List;
import fr.ebiz.computerdatabase.dtos.CompanyDTO;
import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.interfaces.ICompanyService;
import fr.ebiz.computerdatabase.interfaces.IComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AddComputerController {

    private static final Logger LOG = LoggerFactory.getLogger(AddComputerController.class);

    @Autowired
    private IComputerService computerService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    @Qualifier("computerValidator")
    private Validator validator;

    /**
     * Set custom validator to binder.
     * @param binder .
     */
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

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
    public String addComputer(@Validated ComputerDTO computerDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                LOG.info("[CONTROLLER] [ADD] Computer not valid");
                return "add_computer";
            }
            computerService.add(computerDTO);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            return "500";
        }
        return "redirect:/add_computer";
    }
}
