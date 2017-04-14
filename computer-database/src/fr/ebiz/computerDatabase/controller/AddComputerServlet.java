package fr.ebiz.computerDatabase.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.ebiz.computerDatabase.service.ComputerService;

/**
 * Servlet implementation class AddComputerServlet
 */
@WebServlet(name = "addComputer", urlPatterns = { "/addComputer" })
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String ADD_VIEW = "/WEB-INF/addComputer.jsp";
	
	private ComputerService computerService = new ComputerService();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComputerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * instantiate companyService 
		 * get list of companies as DTO
		 */
		
		this.getServletContext().getRequestDispatcher(ADD_VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * instantiate computerService
		 * launch the insert
		 */
	}

}
