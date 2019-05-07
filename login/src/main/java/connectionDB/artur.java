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

    static boolean check = false;

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

        //Recoger parametros
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        String strJson;

        //Driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            strJson = "{\"status\":500,\"msg\":\"jdbc driver not found\"}";
        }
        //Comprobamos valores
        if (login != null && pass != null) {
            try (PrintWriter out = response.getWriter()) {
                //Connection
                Connection oConnection = ConnectionHikari.newConnection();
                //Valor connection
                if (oConnection != null) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Login</title>");
                    out.println("</head>");
                    out.println("<body>");
                    try {
                        PreparedStatement oPreparedStatement;
                        ResultSet oResultSet;
                        String query = "SELECT * FROM cliente WHERE login = ? AND pass = ?";
                        oPreparedStatement = oConnection.prepareStatement(query);
                        oPreparedStatement.setString(1, login);
                        oPreparedStatement.setString(2, pass);
                        oResultSet = oPreparedStatement.executeQuery();

                        while (oResultSet.next()) {
                            check = true;
                        }
                    } catch (SQLException e) {
                        out.println("<h1>Error en " + e.getMessage() + "</h1>");
                    }

                    if (check = true) {
                        int aleatorio = (int) Math.floor(Math.random() * 100 + 1);
                        out.println("<h1>Te has logueado correctamente, puedes ver el número secreto==> " + aleatorio + " .</h1>");
                    } else {
                        out.println("<h1>Datos incorrectos, no puedes loguearte.</h1>");
                    }

                    out.println("</body>");
                    out.println("</html>");
                } else {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Login</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Conexión con valor nulo. No conectado.</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }

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
