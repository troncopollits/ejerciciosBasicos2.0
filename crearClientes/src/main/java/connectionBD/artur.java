/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectionBD;

import static connectionBD.ConnectionHikari.oConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
    private ArrayList crearRegistro() {

        String[] arrayNombres = {"Arturo", "Pepa", "Amparo", "Azael", "Carlos", "Silvia", "María", "Rubén"};
        String[] arrayApe1 = {"Perez", "Coll", "Pastor", "Barber", "Sendra", "Monllor", "Garcia", "Lamont"};
        String[] arrayApe2 = {"Bisquert", "Coll", "Morato", "Fuster", "Vicens", "Monllor", "Garcia", "Sospedra"};
        String[] login = {"pez", "leon", "tigre", "perro", "gato", "zebra", "gallo", "raton"};
        String[] pass = {"123"};

        ArrayList<String> listaCliente = new ArrayList<>();

        int valorNombre = (int) Math.floor(Math.random() * 7 + 1);
        int valorApe1 = (int) Math.floor(Math.random() * 7 + 1);
        int valorApe2 = (int) Math.floor(Math.random() * 7 + 1);
        int valorLogin = (int) Math.floor(Math.random() * 7 + 1);
        int valorPass = 0;

        listaCliente.add(arrayNombres[valorNombre]);
        listaCliente.add(arrayApe1[valorApe1]);
        listaCliente.add(arrayApe2[valorApe2]);
        listaCliente.add(login[valorLogin]);
        listaCliente.add(pass[valorPass]);

        return listaCliente;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        //Recibir la request y guardar las variables
        int registros = Integer.parseInt(request.getParameter("registros"));
        //conectarse
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            String strJson = "{\"status\":500,\"msg\":\"jdbc driver not found\"}";
        }

        try (PrintWriter out = response.getWriter()) {

            Connection oConnection = ConnectionHikari.newConnection();

            if (oConnection != null) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet artur</title>");
                out.println("</head>");
                out.println("<body>");

                try {
                    while (registros > 0) {

                        ArrayList<String> lista = new ArrayList<>();
                        PreparedStatement oPreparedStatement = null;
                        ResultSet oResultSet = null;
                        //Llamar al metodo
                        lista = crearRegistro();

                        //Llenar
                        String nombre = lista.get(0);
                        String apellido1 = lista.get(1);
                        String apellido2 = lista.get(2);
                        String login = lista.get(3);
                        String pass = lista.get(4);

                        //PreparedStatement
                        try {
                            String query = "INSERT INTO cliente (nombre, apellido1, apellido2, login, pass) VALUES (?,?,?,?,?)";

                            oPreparedStatement = oConnection.prepareStatement(query);
                            oPreparedStatement.setString(1, nombre);
                            oPreparedStatement.setString(2, apellido1);
                            oPreparedStatement.setString(3, apellido2);
                            oPreparedStatement.setString(4, login);
                            oPreparedStatement.setString(5, pass);
                            oPreparedStatement.executeUpdate();

                            oResultSet = oPreparedStatement.getGeneratedKeys();

                            while (oResultSet.next()) {
                                out.println("<h3>Registro completado del nuevo cliente con id==> " + oResultSet.getInt(1) + " </h3>");
   //                             out.println("<div>Login: " + oResultSet.getString("login") + "</div>");
//                            out.println("<div>Nombre: " + oResultSet.getString("nombre") + "</div>");
//                            out.println("<div>Apellido 1: " + oResultSet.getString("apellido1") + "</div>");
//                            out.println("<div>Apellido 2: " + oResultSet.getString("apellido2") + "</div>");
                                out.println("<div>================================================================</div>");
                            }

                            //Descontamos uno cada vez hasta llegar a 0
                            registros -= 1;

                        } finally {
                            if (oPreparedStatement != null) {
                                oPreparedStatement.close();
                            }
                            if (oResultSet != null) {
                                oResultSet.close();
                            }
                        }

                    }
                } catch (Exception e) {
                    out.println("<h1>Error de " + e.getMessage() + "</h1>");
                }

                //Registros a 0
                if (registros == 0) {
                    out.println("<h1>TODOS LOS REGISTROS INTRODUCIDOS CORRECTAMENTE.</h1>");
                }
                //Cerramos estructura html
                out.println("</body>");
                out.println("</html>");
            } else {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet artur</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Connexion ==> NO CONECTADO</h1>");
                out.println("</body>");
                out.println("</html>");
            }

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
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
