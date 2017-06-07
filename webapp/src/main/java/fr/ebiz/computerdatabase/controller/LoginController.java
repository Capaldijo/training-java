package fr.ebiz.computerdatabase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage(ModelMap modelMap, @RequestParam(value = "error", required = false) boolean error) {
        modelMap.addAttribute("error", error);
        return "login";
    }
}
