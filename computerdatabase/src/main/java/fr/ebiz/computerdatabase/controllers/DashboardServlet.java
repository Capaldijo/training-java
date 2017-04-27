package fr.ebiz.computerdatabase.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.persistence.LikeBoth;
import fr.ebiz.computerdatabase.services.ComputerService;
import fr.ebiz.computerdatabase.utils.Utils;
import fr.ebiz.computerdatanase.dtos.ComputerDTO;

/**
 * Servlet implementation class DashboardServlet.
 */
@WebServlet(name = "dashboard", urlPatterns = { "/dashboard" })
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DASHBOARD_VIEW = "/WEB-INF/dashboard.jsp";

    private static final String NAME_COMPUTER = "c.name";

    private static final String NAME_COMPANY = "comp.name";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String numPage = request.getParameter("numPage");
        String nbLine = request.getParameter("nbLine");
        String search = request.getParameter("search");
        String orderBy = request.getParameter("orderBy");
        String ascOrDesc = request.getParameter("asc");

        if (search == null) {
            search = "";
        }
        int count = 0;

        if (numPage == null) {
            numPage = "0";
        }
        if (nbLine == null) {
            nbLine = String.valueOf(Utils.PAGEABLE_NBLINE);
        }

        PaginationFilters filter = new PaginationFilters.Builder()
                .search(NAME_COMPUTER, new LikeBoth(search))
                .search(NAME_COMPANY, new LikeBoth(search))
                .orderBy(orderBy, Boolean.parseBoolean(ascOrDesc))
                .build();

        try {
            count = ComputerService.getInstance().count(search);
            List<ComputerDTO> listComputerDTO = ComputerService.getInstance().getByPage(numPage, nbLine, filter);
            System.out.println(listComputerDTO);
            request.setAttribute("nbComputer", count);
            request.setAttribute("computers", listComputerDTO);
            request.setAttribute("numPage", numPage);
            request.setAttribute("search", search);
            request.setAttribute("nbLine", nbLine);

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

        ComputerService.getInstance().delete(ids);

        request.setAttribute("numPage", numPage);
        request.setAttribute("search", search);
        request.setAttribute("nbLine", nbLine);

        doGet(request, response);
    }
}
