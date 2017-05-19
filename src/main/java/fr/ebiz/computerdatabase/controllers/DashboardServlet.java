package fr.ebiz.computerdatabase.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.interfaces.IComputerService;
import fr.ebiz.computerdatabase.models.LikeBoth;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet implementation class DashboardServlet.
 */
@WebServlet(name = "dashboard", urlPatterns = { "/dashboard" })
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DASHBOARD_VIEW = "/WEB-INF/dashboard.jsp";

    private static final String NAME_COMPUTER = "c.name";

    private static final String NAME_COMPANY = "comp.name";

    @Autowired
    private IComputerService computerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Allow the context of tomcat to load the context of Spring
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String numPage = request.getParameter("numPage");
        String nbLine = request.getParameter("nbLine");
        String search = request.getParameter("search");
        String orderBy = request.getParameter("orderBy");
        String asc = request.getParameter("asc");

        if (search == null) {
            search = "";
        } else {
            search = search.trim();
        }
        int count = 0, order = 0;

        if (numPage == null) {
            numPage = "0";
        }
        if (nbLine == null) {
            nbLine = String.valueOf(Utils.PAGEABLE_NBLINE);
        }

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
                    .orderBy(Utils.HEADERTABLE_NAME[order], Boolean.parseBoolean(asc))
                    .build();

            if (search == null) {
                count = computerService.count();
            } else {
                search = search.trim();
                count = computerService.count(search);
            }
            List<ComputerDTO> listComputerDTO = computerService.getByPage(numPage, nbLine, filter);

            request.setAttribute("nbComputer", count);
            request.setAttribute("computers", listComputerDTO);
            request.setAttribute("numPage", numPage);
            request.setAttribute("search", search);
            request.setAttribute("nbLine", nbLine);
            request.setAttribute("orderBy", orderBy);
            request.setAttribute("asc", asc);

            this.getServletContext().getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String numPage = request.getParameter("numPage");
        String search = request.getParameter("search");
        String nbLine = request.getParameter("nbLine");

        String selection = request.getParameter("selection");
        String[] ids = selection.split(",");

        computerService.delete(ids);

        request.setAttribute("numPage", numPage);
        request.setAttribute("search", search);
        request.setAttribute("nbLine", nbLine);

        doGet(request, response);
    }
}
