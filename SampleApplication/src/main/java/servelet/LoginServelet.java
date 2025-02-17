package servelet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class LoginServelet
 */
@WebServlet("/LoginServelet")
public class LoginServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LoginServelet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	String username=request.getParameter("username");
	String password=request.getParameter("password");
	 Connection con = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     try {
         con = ApplicationDatabase.getConnection();
         String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
         ps = con.prepareStatement(sql);
         ps.setString(1, username);
         ps.setString(2, password);
         rs = ps.executeQuery();

         if (rs.next()) {
             HttpSession session = request.getSession();
             session.setAttribute("username", username);
             response.sendRedirect("Welcome.jsp");  
         } else {
             response.sendRedirect("Error.jsp");  
         }
     } catch (SQLException e) {
         e.printStackTrace();
         response.sendRedirect("Error.jsp");
     } finally {
    	 ApplicationDatabase.closeConnection(con);
     }
 }
}
