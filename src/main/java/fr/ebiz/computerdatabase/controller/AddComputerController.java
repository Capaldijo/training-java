package fr.ebiz.computerdatabase.controller;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.service.ICompanyService;
import fr.ebiz.computerdatabase.service.IComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AddComputerController {

    @Autowired
    private IComputerService computerService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    @Qualifier("computerValidator")
    private Validator validator;

    /**
     * Set custom validator to binder.
     * @param binder WebDataBinder.
     */
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    /**
     * Method called for the springForm 'cause of commandName param
     * in add_computer.jsp.
     * @return an empty ComputerDTO.
     */
    @ModelAttribute("computer")
    public ComputerDTO createComputerDTOModel() {
        return new ComputerDTO();
    }

    @RequestMapping(value = {"/add_computer"}, method = RequestMethod.GET)
    public String addComputer(ModelMap model) {
        model.addAttribute("companies", companyService.getAll());
        return "add_computer";
    }

    @RequestMapping(value = {"/add_computer"}, method = RequestMethod.POST)
    public String addComputer(@ModelAttribute("computer") @Validated ComputerDTO computerDTO,
                              BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.getAll());
            return "add_computer";
        }
        computerService.add(computerDTO);
        return "redirect:/add_computer";
    }
}
