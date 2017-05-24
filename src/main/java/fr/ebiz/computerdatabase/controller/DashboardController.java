package fr.ebiz.computerdatabase.controller;

import fr.ebiz.computerdatabase.service.IComputerService;
import fr.ebiz.computerdatabase.model.utils.SearchFilter;
import fr.ebiz.computerdatabase.model.utils.PaginationFilters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

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
                            @RequestParam(value = PAGE, defaultValue = "0") int numPage,
                            @RequestParam(value = LINE, defaultValue = "10") int nbLine,
                            @RequestParam(value = SEARCH, defaultValue = "") String search,
                            @RequestParam(value = ORDER_BY, defaultValue = "0") int orderBy,
                            @RequestParam(value = ASC, defaultValue = "false") boolean asc,
                            @RequestParam(value = SELECTION, required = false) String selection) {

        computerService.deleteComputers(selection);

        PaginationFilters filter = new PaginationFilters.Builder()
                .search(NAME_COMPUTER, new SearchFilter(search))
                .search(NAME_COMPANY, new SearchFilter(search))
                .orderBy(HEADER_TABLE_NAME[orderBy], asc)
                .build();

        model.addAttribute("nbComputer", computerService.count(search));
        model.addAttribute("computers", computerService.getByPage(numPage, nbLine, filter));
        model.addAttribute(PAGE, numPage);
        model.addAttribute(SEARCH, search);
        model.addAttribute(LINE, nbLine);
        model.addAttribute(ORDER_BY, orderBy);
        model.addAttribute(ASC, asc);
        return "dashboard";
    }
}
