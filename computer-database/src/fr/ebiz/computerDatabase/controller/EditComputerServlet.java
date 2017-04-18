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
import fr.ebiz.computerDatabase.model.CompanyDTO;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.service.CompanyService;
import fr.ebiz.computerDatabase.service.ComputerService;
import fr.ebiz.computerDatabase.utils.Utils;

/**
 * Servlet implementation class EditComputerServlet
 */
@WebServlet(name = "editComputer", urlPatterns = { "/editComputer" })
public class EditComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private CompanyService companyService;
	
	private ComputerService computerService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputerServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String id = request.getParameter("id");
			
			computerService = new ComputerService();
			ComputerDTO computer = computerService.getComputer(id);
			System.out.println(computer);
			
			companyService = new CompanyService();
			List<CompanyDTO> companiesDTO = companyService.getCompanies();
			
			request.setAttribute("computerId", id);
			request.setAttribute("companies", companiesDTO);
			
			this.getServletContext().getRequestDispatcher(Utils.ADD_VIEW).forward(request, response);
		} catch (ConnectionException | DAOException | MapperException | ServiceException e) {
			System.out.println(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
