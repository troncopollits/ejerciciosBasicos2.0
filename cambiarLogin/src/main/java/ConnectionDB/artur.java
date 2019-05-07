/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionDB;

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
    //Comprobar la validez del registro buscado
    private boolean comprobarRegistro(Connection oConnection, String nombre, String apellido) {
        boolean comprobado = false;

        try {
            String query = "SELECT * FROM cliente WHERE nombre = ? AND apellido1 = ?";
            PreparedStatement oPreparedStatement;
            ResultSet oResulSet;

            oPreparedStatement = oConnection.prepareStatement(query);
            oPreparedStatement.setString(1, nombre);
            oPreparedStatement.setString(2, apellido);
            oResulSet = oPreparedStatement.executeQuery();

            if (oResulSet.next()) {
                comprobado = true;
            } else {
                comprobado = false;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return comprobado;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        //Recoger valores de la request
        String login = request.getParameter("login");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        boolean comprobado;

        //Driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            String strJson = "{\"status\":500,\"msg\":\"jdbc driver not found\"}";
        }

        try (PrintWriter out = response.getWriter()) {

            //New Connection 
            Connection oConnection = ConnectionHikari.newConnection();

            if (oConnection != null && nombre != null & apellido != null) {
                //Llamada al metodo
                comprobado = comprobarRegistro(oConnection, nombre, apellido);
                if (comprobado) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet artur</title>");
                    out.println("</head>");
                    out.println("<body>");

                    try {
                        String query = "UPDATE cliente SET login=? WHERE nombre = ? AND apellido1 = ?";
                        PreparedStatement oPreparedStatement;
                        ResultSet oResultSet;
                        int contador = 0;
                        oPreparedStatement = oConnection.prepareStatement(query);
                        oPreparedStatement.setString(1, login);
                        oPreparedStatement.setString(2, nombre);
                        oPreparedStatement.setString(3, apellido);

                        contador = oPreparedStatement.executeUpdate();
                        if (contador != 0) {
                            out.println("<h1>Registro actualizado correctamente.</h1>");
                        }
                    } catch (SQLException e) {
                        out.println("<h1>Error en " + e.getMessage() + "</h1>");
                    }

                    out.println("</body>");
                    out.println("</html>");
                } else {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet artur</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Registro no encontrado.</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }

            } else {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet artur</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Connection erronea.</h1>");
                out.println("</body>");
                out.println("</html>");
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
