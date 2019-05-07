/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectionDB;

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

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        //Recoger los parametros de la ruta
        String registro = request.getParameter("registro");
        String strJson = "";
        //conectarse
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            strJson = "{\"status\":500,\"msg\":\"jdbc driver not found\"}";
        }

        try (PrintWriter out = response.getWriter()) {

            //Creamos la conexión
            Connection oConnection = ConnectionHikari.newConnection();

            //Comprobamos si la conexión está vacía o no
            if (oConnection != null) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet artur</title>");
                out.println("</head>");
                out.println("<body>");

                ResultSet oResultSet = null;
                PreparedStatement oPreparedStatement = null;
                switch (registro) {
                    case "par":
                        try {
                            String query = "SELECT * FROM cliente WHERE mod(id,2) = 0";
                            oPreparedStatement = oConnection.prepareStatement(query);
                            oResultSet = oPreparedStatement.executeQuery();
                            while (oResultSet.next()) {
                                out.println("<h1>------------------FILA SELECCIONADA-------------------------------</h1>");
                                out.println("<h1>Id==> " + oResultSet.getString("id") + "</h1>");
                                out.println("<div>Nombre: " + oResultSet.getString("nombre") + "</div>");
                                out.println("<div>Apellido 1: " + oResultSet.getString("apellido1") + "</div>");
                                out.println("<div>Apellido 2: " + oResultSet.getString("apellido2") + "</div>");
                            }
                        } catch (SQLException e) {
                            out.println("<h1>Error SQL==> " + e.getMessage() + "</h1>");
                        } finally {
                            if (oResultSet != null) {
                                oResultSet.close();
                            }
                            if (oPreparedStatement != null) {
                                oPreparedStatement.close();
                            }
                        }
                        break;
                    case "impar":
                        try {
                            String query = "SELECT * FROM cliente WHERE mod(id,2) = 1";
                            oPreparedStatement = oConnection.prepareStatement(query);
                            oResultSet = oPreparedStatement.executeQuery();
                            while (oResultSet.next()) {
                                out.println("<h1>------------------FILA SELECCIONADA-------------------------------</h1>");
                                out.println("<h1>Id==> " + oResultSet.getString("id") + "</h1>");
                                out.println("<div>Nombre: " + oResultSet.getString("nombre") + "</div>");
                                out.println("<div>Apellido 1: " + oResultSet.getString("apellido1") + "</div>");
                                out.println("<div>Apellido 2: " + oResultSet.getString("apellido2") + "</div>");
                            }
                        } catch (SQLException e) {
                            out.println("<h1>Error SQL==> " + e.getMessage() + "</h1>");
                        } finally {
                            if (oResultSet != null) {
                                oResultSet.close();
                            }
                            if (oPreparedStatement != null) {
                                oPreparedStatement.close();
                            }
                        }
                        break;
                }
            } else {
                out.println("<h1>Conexión vacía</h1>");
            }
            out.println("</body>");
            out.println("</html>");

            //Cerramos conexion
            oConnection.close();
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
