/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectionBD;

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

        //recoger parametros de la request
        String nombre = request.getParameter("nombre");
        int contador = 0;//Indica el número de registros borrados

        String strJson = "";

        //conectarse
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            strJson = "{\"status\":500,\"msg\":\"jdbc driver not found\"}";
        }
        try (PrintWriter out = response.getWriter()) {

            //Creamos conexion
            Connection oConnection = ConnectionHikari.newConnection();

            //Comprobación de si la conexión es null o no
            if (oConnection != null) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet artur</title>");
                out.println("</head>");
                out.println("<body>");

                PreparedStatement oPreparedStatement = null;

                //Sentencia sql y consulta a la base de datos
                try {
                    String query = "DELETE FROM cliente WHERE nombre = ?";

                    oPreparedStatement = oConnection.prepareStatement(query);
                    oPreparedStatement.setString(1, nombre);
                    contador = oPreparedStatement.executeUpdate();

                    if (contador != 0) {
                        out.println("<h1>Se han eliminado los registros.</h1>");
                    } else if (contador == 0) {
                        out.println("<h1>Nombre introducido incorrecto.</h1>");
                    }

                } catch (SQLException e) {
                    out.print("<h1>Error SQL==> " + e.getMessage() + " </h1>");
                } finally {
                    if (oPreparedStatement != null) {
                        oPreparedStatement.close();
                    }
                }
                out.println("</body>");
                out.println("</html>");
            } else {
                
                out.println("</body>");
                out.println("</html>");
            }
        } catch (Exception e) {

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
