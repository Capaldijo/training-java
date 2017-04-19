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
 * Servlet implementation class AddComputerServlet
 */
@WebServlet(name = "addComputer", urlPatterns = { "/add_computer" })
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ComputerService computerService;

	private CompanyService companyService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddComputerServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			companyService = new CompanyService();
			List<CompanyDTO> companiesDTO = companyService.getCompanies();
			request.setAttribute("companies", companiesDTO);
			this.getServletContext().getRequestDispatcher(Utils.ADD_VIEW).forward(request, response);
		} catch (ConnectionException | DAOException | MapperException e) {
			System.out.println(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter(Utils.PARAM_COMPUTER_NAME);
		String introduced = request.getParameter(Utils.PARAM_COMPUTER_INTRODUCED);
		String discontinued = request.getParameter(Utils.PARAM_COMPUTER_DISCONTINUED);
		String companyId = request.getParameter(Utils.PARAM_COMPUTER_COMPANYID);
		
		ComputerDTO computerDTO = new ComputerDTO.ComputerDTOBuilder(name)
									.introduced(introduced)
									.discontinued(discontinued)
									.companyId(companyId)
									.build();
		try {
			computerService = new ComputerService();
			computerService.addComputer(computerDTO);
		} catch (ServiceException | ConnectionException | DAOException | MapperException e) {
			System.out.println(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
