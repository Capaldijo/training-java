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
@WebServlet(name = "editComputer", urlPatterns = { "/edit_computer" })
public class EditComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String id = request.getParameter("id");
			
			ComputerDTO computer = ComputerService.getInstance().getComputer(id);
			
			List<CompanyDTO> companiesDTO = CompanyService.getInstance().getCompanies();
			
			request.setAttribute("computer", computer);
			request.setAttribute("companies", companiesDTO);
			
			this.getServletContext().getRequestDispatcher(Utils.EDIT_VIEW).forward(request, response);
		} catch (ConnectionException | DAOException | MapperException | ServiceException e) {
			System.out.println(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String id = request.getParameter(Utils.PARAM_COMPUTER_ID);
	    String name = request.getParameter(Utils.PARAM_COMPUTER_NAME);
        String introduced = request.getParameter(Utils.PARAM_COMPUTER_INTRODUCED);
        String discontinued = request.getParameter(Utils.PARAM_COMPUTER_DISCONTINUED);
        String companyId = request.getParameter(Utils.PARAM_COMPUTER_COMPANYID);
        
        ComputerDTO computerDTO = new ComputerDTO.ComputerDTOBuilder(name)
                                    .id(id)
                                    .introduced(introduced)
                                    .discontinued(discontinued)
                                    .companyId(companyId)
                                    .build();
        try {
            ComputerService.getInstance().updateComputer(computerDTO);
            this.getServletContext().getRequestDispatcher(Utils.DASHBOARD_VIEW).forward(request, response);
        } catch (ServiceException | ConnectionException | DAOException | MapperException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
	}

}
