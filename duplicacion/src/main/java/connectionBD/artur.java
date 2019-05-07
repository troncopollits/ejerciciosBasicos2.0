/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectionBD;

import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author artur
 */
public class artur extends HttpServlet {

    private HikariDataSource oConnectionPool;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //Metodo para duplicar una fila de la bbdd
    public int duplicate(int id, Connection oConnection) throws Exception {
        String strSQL = "INSERT INTO cliente (nombre,apellido1,apellido2) SELECT nombre,apellido1,apellido2 FROM cliente WHERE id=" + id;
        ResultSet oResultSet = null;
        PreparedStatement oPreparedStatement = null;

        //Clave que nos devuelve el id generado nuevo
        int claveGenerada = 0;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oPreparedStatement.executeUpdate();
            oResultSet = oPreparedStatement.getGeneratedKeys();
            if (oResultSet.next()) {
                claveGenerada = oResultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new Exception("Error en metodo duplicate", e);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return claveGenerada;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        //recibir la petición y analizar la petición
        String id = request.getParameter("id");
        if (id != null) {
            int nuevoID = Integer.parseInt(id);

            String strJson = "";

            //conectarse
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                strJson = "{\"status\":500,\"msg\":\"jdbc driver not found\"}";
            }
            //servir la petición. Incluye acceso a BD
            try (PrintWriter out = response.getWriter()) {

                Connection oConnection = ConnectionHikari.newConnection();

                if (oConnection != null) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet artur</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h2>id=" + nuevoID + "</h2>");

                    int resultadoDuplicacion = this.duplicate(nuevoID, oConnection);
                    out.println("<div>resultado de la dupli: " + resultadoDuplicacion + "</div>");

                    try {
                        String strSQL = "SELECT * FROM cliente WHERE id= ? ";
                        PreparedStatement oPreparedStatement;
                        oPreparedStatement = oConnection.prepareStatement(strSQL);
                        //oPreparedStatement.setString(1, tabla);
                        oPreparedStatement.setInt(1, resultadoDuplicacion);
                        ResultSet oResultSet = oPreparedStatement.executeQuery();
                        while (oResultSet.next()) {
                            out.println("<h2>FILA DUPLICADA:</h2>");
                            out.println("<div>Nombre: " + oResultSet.getString("nombre") + "</div>");
                            out.println("<div>Apellido 1: " + oResultSet.getString("apellido1") + "</div>");
                            out.println("<div>Apellido 2: " + oResultSet.getString("apellido2") + "</div>");
                        }
                    } catch (Exception e) {
                        out.println("<h1>Error de " + e.getMessage() + "</h1>");
                    }

                    /* TODO output your page here. You may use following sample code. */
                    //out.println("<h1>Servlet artur con tabla: " + tabla + " </h1>");
                    out.println("<h1>Servlet artur con el registro de id=" + nuevoID + " duplicado a " + resultadoDuplicacion + " </h1>");

                    out.println("</body>");
                    out.println("</html>");
                } else {
                    /* TODO output your page here. You may use following sample code. */
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet artur</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Servlet artur con" + nuevoID + " </h1>");
                    out.println("<h1>Connexion ==> no conectado</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }

            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                oConnectionPool.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(artur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(artur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(artur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
