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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EditComputerController {

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
        model.addAttribute("computer", computerService.get(id));
        model.addAttribute("companies", companyService.getAll());
        return "edit_computer";
    }

    @RequestMapping(value = {"/edit_computer"}, method = RequestMethod.POST)
    public String editComputer(@ModelAttribute("computer") @Validated ComputerDTO computerDTO,
                               BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companies", companyService.getAll());
            return "edit_computer";
        }
        computerService.update(computerDTO);
        return "redirect:/dashboard";
    }
}
