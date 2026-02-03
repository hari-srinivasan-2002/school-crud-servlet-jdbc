import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CRUD")
public class CRUD extends HttpServlet {

    
    public static Connection getConnection() throws Exception {
        
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Return the connection
        return DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:xe",  
            "system",                              
            "system"                                
        );
    }
protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html;charset=UTF-8");

    try (PrintWriter out = response.getWriter();
         Connection con = getConnection()) {   // Connection auto-closed

        String action = request.getParameter("action");

        switch (action) {

            case "add": {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String location = request.getParameter("location");
                String state = request.getParameter("state");

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO school (id, name, location, state) VALUES (?,?,?,?)"
                );
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, location);
                ps.setString(4, state);

                ps.executeUpdate();
                out.println("School Added Successfully");
                break;
            }

            case "update": {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String location = request.getParameter("location");
                String state = request.getParameter("state");

                PreparedStatement ps = con.prepareStatement(
                    "UPDATE school SET name=?, location=?, state=? WHERE id=?"
                );
                ps.setString(1, name);
                ps.setString(2, location);
                ps.setString(3, state);
                ps.setInt(4, id);

                ps.executeUpdate();
                out.println("School Updated Successfully");
                break;
            }

            case "delete": {
                int id = Integer.parseInt(request.getParameter("id"));

                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM school WHERE id=?"
                );
                ps.setInt(1, id);
                ps.executeUpdate();

                out.println("School Deleted Successfully");
                break;
            }

            case "view": {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM school");

                out.println("<h2>School List</h2>");
                out.println("<table border='1'>");
                out.println("<tr><th>ID</th><th>Name</th><th>Location</th><th>State</th></tr>");

                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("id") + "</td>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("location") + "</td>");
                    out.println("<td>" + rs.getString("state") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                break;
            }

            default:
                out.println("Invalid Action");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
