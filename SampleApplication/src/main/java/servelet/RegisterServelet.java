package servelet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class RegisterServelet
 */
@WebServlet("/RegisterServelet")
public class RegisterServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServelet() {
        super();
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
		doGet(request, response);
	
	 Connection con = null;
     PreparedStatement ps = null;

     try {
         con = ApplicationDatabase.getConnection();
         String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
         ps = con.prepareStatement(sql);
         ps.setString(1, username);
         ps.setString(2, password);
         int result = ps.executeUpdate();

         if (result > 0) {
             response.sendRedirect("Login.jsp");  
         } else {
             response.sendRedirect("Error.jsp");
         }
     } catch (SQLException e) {
         e.printStackTrace();
         response.sendRedirect("Error.jsp");
     } finally {
    	 ApplicationDatabase .closeConnection(con);
     }
 }
}
