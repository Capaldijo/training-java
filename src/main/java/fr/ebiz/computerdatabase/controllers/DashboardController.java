package fr.ebiz.computerdatabase.controllers;

import java.util.List;
import java.util.Locale;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.interfaces.IComputerService;
import fr.ebiz.computerdatabase.models.LikeBoth;
import fr.ebiz.computerdatabase.models.PaginationFilters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

    private static final String[] HEADER_TABLE_NAME = {null, "c.name", "c.introduced", "c.discontinued", "comp.name"};

    private static final String NAME_COMPUTER = "c.name";
    private static final String NAME_COMPANY = "comp.name";

    private static final String PAGE = "numPage";
    private static final String LINE = "nbLine";
    private static final String SEARCH = "search";
    private static final String ORDER_BY = "orderBy";
    private static final String ASC = "asc";
    private static final String SELECTION = "selection";

    @Autowired
    private IComputerService computerService;

    @RequestMapping(value = {"/", "/dashboard"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String dashboard(ModelMap model,
                            @RequestParam(value = PAGE, defaultValue = "0") String numPage,
                            @RequestParam(value = LINE, defaultValue = "10") String nbLine,
                            @RequestParam(value = SEARCH, defaultValue = "") String search,
                            @RequestParam(value = ORDER_BY, required = false) String orderBy,
                            @RequestParam(value = ASC, required = false) String asc,
                            @RequestParam(value = SELECTION, required = false) String selection) {

        if (selection != null) {
            String[] ids = selection.split(",");
            computerService.delete(ids);
        }
        search = search.trim();

        int count = 0, order = 0;

        try {
            if (asc != null && (!asc.trim().toLowerCase().equals("true") && !asc.trim().toLowerCase().equals("false"))) {
                throw new RuntimeException("Asc not boolean: " + asc);
            }
            if (orderBy != null) {
                order = Integer.parseInt(orderBy);
            }
            PaginationFilters filter = new PaginationFilters.Builder()
                    .search(NAME_COMPUTER, new LikeBoth(search))
                    .search(NAME_COMPANY, new LikeBoth(search))
                    .orderBy(HEADER_TABLE_NAME[order], Boolean.parseBoolean(asc))
                    .build();

            if (search.equals("")) {
                count = computerService.count();
            } else {
                search = search.trim();
                count = computerService.count(search);
            }
            List<ComputerDTO> listComputerDTO = computerService.getByPage(numPage, nbLine, filter);

            model.addAttribute("nbComputer", count);
            model.addAttribute("computers", listComputerDTO);
            model.addAttribute(PAGE, numPage);
            model.addAttribute(SEARCH, search);
            model.addAttribute(LINE, nbLine);
            model.addAttribute(ORDER_BY, orderBy);
            model.addAttribute(ASC, asc);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            return "500";
        }
        return "dashboard";
    }
}
