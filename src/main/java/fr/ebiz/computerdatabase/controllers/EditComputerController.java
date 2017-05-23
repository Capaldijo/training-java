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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditComputerController {

    private static final Logger LOG = LoggerFactory.getLogger(EditComputerController.class);

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

    @RequestMapping(value = {"/edit_computer"}, method = RequestMethod.GET)
    public String editComputer(ModelMap model, @RequestParam("id") String id) {
        try {
            ComputerDTO computer = computerService.get(id);
            List<CompanyDTO> companiesDTO = companyService.getAll();

            model.addAttribute("computer", computer);
            model.addAttribute("companies", companiesDTO);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            return "500";
        }
        return "edit_computer";
    }

    @RequestMapping(value = {"/edit_computer"}, method = RequestMethod.POST)
    public String editComputer(@Validated ComputerDTO computerDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                LOG.info("[CONTROLLER] [EDIT] Computer not valid");
                return "add_computer";
            }
            computerService.update(computerDTO);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            return "500";
        }
        return "redirect:/dashboard";
    }
}
