package fr.ebiz.computerdatabase.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.ebiz.computerdatabase.model.ComputerDTO;
import fr.ebiz.computerdatabase.service.ComputerService;
import fr.ebiz.computerdatabase.utils.Utils;

/**
 * Servlet implementation class DashboardServlet.
 */
@WebServlet(name = "dashboard", urlPatterns = { "/dashboard" })
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DASHBOARD_VIEW = "/WEB-INF/dashboard.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String numPage = request.getParameter("numPage");
        String search = request.getParameter("search");
        String nbLine = request.getParameter("nbLine");

        int count = 0;

        try {
            count = ComputerService.getInstance().getNbComputer(search);
            List<ComputerDTO> listComputerDTO = ComputerService.getInstance().getComputersByPage(numPage, search, nbLine);

            if (numPage == null) {
                numPage = "0";
            }
            if (search == null) {
                search = "";
            }
            if (nbLine == null) {
                nbLine = String.valueOf(Utils.PAGEABLE_NBLINE);
            }
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
        String search = request.getParameter("search");
        String nbLine = request.getParameter("nbLine");

        String selection = request.getParameter("selection");
        String[] ids = selection.split(",");

        //this.getServletContext().getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);
    }
}
