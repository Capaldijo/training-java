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

	private static ComputerService computerService;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    try {
	        String nbPage = request.getParameter("numPage");
	        String search = request.getParameter("search");
	        
	        computerService = new ComputerService();
            int count = computerService.getNbComputer();
            request.setAttribute("nbComputer", count);
            
            List <ComputerDTO> listComputerDTO = null;
            if (search != null) {
                listComputerDTO = computerService.getResearchByPage("0", search);
                request.setAttribute("computers", listComputerDTO);
            } else {
                if (nbPage != null){
                    listComputerDTO = computerService.getComputersByPage(nbPage);
                } else {
                    listComputerDTO = computerService.getComputersByPage("0");
                }
                request.setAttribute("computers", listComputerDTO);
            }

            this.getServletContext().getRequestDispatcher(Utils.DASHBOARD_VIEW).forward(request, response);
        } catch (ConnectionException | ServiceException | MapperException | DAOException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	    
	}

}
