package fr.ldevapps.mywebstore.ihm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.ldevapps.mywebstore.dao.DAOContext;
import fr.ldevapps.mywebstore.dao.UserDAO;
import fr.ldevapps.mywebstore.model.CatalogBrowser;
import fr.ldevapps.mywebstore.model.User;

/**
 * Servlet implementation class Login
 */
@WebServlet(urlPatterns="/login", loadOnStartup=1)
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = -4319076288258537575L;


	@Override
	public void init() throws ServletException {
		DAOContext.init( this.getServletContext() );
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute( "login", "" );
		request.setAttribute( "password", "" );
		request.setAttribute( "errorMessage", "" );
		request.getRequestDispatcher( "/login.jsp" ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter( "txtLogin" );
		String password = request.getParameter( "txtPassword" );
		
		request.setAttribute( "login", login );
		request.setAttribute( "password", password );
		
		User connectedUser = UserDAO.isValidLogin( login, password );
		if ( connectedUser != null ) {
			
			HttpSession session = request.getSession( true );
			session.setAttribute( "connectedUser", connectedUser ); // Maintient de l'identité sous forme d'objet
			session.setAttribute( "catalogBrowser", new CatalogBrowser() );
			request.getRequestDispatcher( "/viewArticle.jsp" ).forward( request, response );
		
		} else {
		
			request.setAttribute( "errorMessage", "Bad identity" );			
			request.getRequestDispatcher( "/login.jsp" ).forward( request, response );
			
		}
		
	}

}

