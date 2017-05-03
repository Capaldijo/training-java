package fr.ebiz.computerdatabase.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.ebiz.computerdatabase.services.CompanyService;
import fr.ebiz.computerdatabase.services.ComputerService;
import fr.ebiz.computerdatabase.utils.Utils;
import fr.ebiz.computerdatanase.dtos.CompanyDTO;
import fr.ebiz.computerdatanase.dtos.ComputerDTO;

/**
 * Servlet implementation class EditComputerServlet.
 */
@WebServlet(name = "editComputer", urlPatterns = { "/edit_computer" })
public class EditComputerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String EDIT_VIEW = "/WEB-INF/edit_computer.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        try {
            ComputerDTO computer = ComputerService.getInstance().get(id);
            List<CompanyDTO> companiesDTO = CompanyService.getInstance().getAll();

            request.setAttribute("computer", computer);
            request.setAttribute("companies", companiesDTO);

            this.getServletContext().getRequestDispatcher(EDIT_VIEW).forward(request, response);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter(Utils.PARAM_COMPUTER_ID);
        String name = request.getParameter(Utils.PARAM_COMPUTER_NAME);
        String introduced = request.getParameter(Utils.PARAM_COMPUTER_INTRODUCED);
        String discontinued = request.getParameter(Utils.PARAM_COMPUTER_DISCONTINUED);
        String companyId = request.getParameter(Utils.PARAM_COMPUTER_COMPANYID);

        ComputerDTO computerDTO = new ComputerDTO.Builder(name)
                .id(id)
                .introduced(introduced)
                .discontinued(discontinued)
                .companyId(companyId).build();
        try {
            ComputerService.getInstance().update(computerDTO);

            response.sendRedirect("dashboard");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
