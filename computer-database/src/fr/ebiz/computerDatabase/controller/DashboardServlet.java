package fr.ebiz.computerDatabase.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.ebiz.computerDatabase.exceptions.ConnectionException;
import fr.ebiz.computerDatabase.exceptions.DAOException;
import fr.ebiz.computerDatabase.exceptions.MapperException;
import fr.ebiz.computerDatabase.exceptions.ServiceException;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.service.ComputerService;
import fr.ebiz.computerDatabase.utils.Utils;

/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet(name = "dashboard", urlPatterns = { "/dashboard" })
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String numPage = request.getParameter("numPage");
            String search = request.getParameter("search");
            String nbLine = request.getParameter("nbLine");

            int count = 0;

            int numP = 0, nbL = Utils.PAGEABLE_NBLINE;

            if (numPage != null)
                numP = Integer.parseInt(numPage);
            
            if (nbLine != null)
                nbL = Integer.parseInt(nbLine);
            
            if (search == null)
                search = "";
                
            count = ComputerService.getInstance().getNbComputer(search);
            List<ComputerDTO> listComputerDTO = ComputerService.getInstance().getComputersByPage(numP, search, nbL);
            
            request.setAttribute("nbComputer", count);
            request.setAttribute("computers", listComputerDTO);
            request.setAttribute("numPage", numP);
            request.setAttribute("search", search);
            request.setAttribute("nbLine", nbL);

            this.getServletContext().getRequestDispatcher(Utils.DASHBOARD_VIEW).forward(request, response);
        } catch (ConnectionException | ServiceException | MapperException | DAOException | NumberFormatException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
