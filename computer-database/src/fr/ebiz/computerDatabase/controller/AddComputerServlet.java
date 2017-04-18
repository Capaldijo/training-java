package fr.ebiz.computerDatabase.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.ebiz.computerDatabase.model.CompanyDTO;
import fr.ebiz.computerDatabase.model.ComputerDTO;
import fr.ebiz.computerDatabase.service.CompanyService;
import fr.ebiz.computerDatabase.service.ComputerService;
import fr.ebiz.computerDatabase.utils.Utils;

/**
 * Servlet implementation class AddComputerServlet
 */
@WebServlet(name = "addComputer", urlPatterns = { "/addComputer" })
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ComputerService computerService;

	private CompanyService companyService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddComputerServlet() {
		super();
		computerService = new ComputerService();
		companyService = new CompanyService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<CompanyDTO> companiesDTO = companyService.getCompanies();
		request.setAttribute("companies", companiesDTO);
		this.getServletContext().getRequestDispatcher(Utils.ADD_VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("computerName");
		String introduced = request.getParameter("computerIntro");
		String discontinued = request.getParameter("computerDiscon");
		String companyId = request.getParameter("companyId");
		
		ComputerDTO computerDTO = new ComputerDTO.ComputerDTOBuilder(name)
									.introduced(introduced)
									.discontinued(discontinued)
									.companyId(companyId)
									.build();
		
		computerService.addComputer(computerDTO);
	}

}
