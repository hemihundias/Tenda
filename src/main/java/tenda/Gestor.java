/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenda;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Hemihundias
 */
public class Gestor {
    static Conector con = new Conector();
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */    
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, SAXException {
        boolean menu = true;
        Class.forName("org.sqlite.JDBC");
        
        con.connect();
        con.crearBaseDatos();
        con.cargarProvincias();               
        
        int in,i,ide;
        ResultSet rs = null;
        
        while(menu){      
                                   
            Scanner teclado = new Scanner(System.in);
            
            System.out.println("\n1. Añadir una tienda.");
            System.out.println("2. Mostrar tiendas.");
            System.out.println("3. Eliminar una tienda.");
            System.out.println("4. Añadir un producto.");
            System.out.println("5. Mostrar todos los productos.");
            System.out.println("6. Mostrar productos de una tienda.");
            System.out.println("7. Añadir un producto a una tienda.");
            System.out.println("8. Actualizar stock de producto de una tienda.");
            System.out.println("9. Stock producto de una tienda.");
            System.out.println("10. Eliminar un producto de una tienda.");
            System.out.println("11. Eliminar un producto.");
            System.out.println("12. Añadir empleado a una tienda.");
            System.out.println("13. Registrar horas empleado.");
            System.out.println("14. Eliminar empleado.");
            System.out.println("15. Añadir un cliente.");
            System.out.println("16. Mostrar clientes.");
            System.out.println("17. Eliminar un cliente.");
            System.out.println("18. Leer titulares El País.");
            System.out.println("0. Salir.\n");
            
            System.out.println("Por favor elixa unha opción:");
            
            String entrada = teclado.nextLine();
                        
            switch(entrada){
                
                case "1":
                    con.insertarTienda();
                    System.out.println("Tienda creada....");
                    
                    break;

                case "2":
                    con.mostrarTiendas();
                    
                    break;

                case "3":
                    con.eliminarTienda();
                    System.out.println("Tienda eliminada....");
                    break;

                case "4":
                    con.insertarProducto();
                    System.out.println("Producto creado...");
                    
                    break;

                case "5":
                    con.mostrarProductos();                    

                    break;

                case "6":
                    con.mostrarTiendas();
                    
                    System.out.println("Introduzca el id de la tienda de la que quiere consultar los productos.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    ide = teclado.nextInt();

                    String sql = "SELECT * FROM existencias WHERE id_tienda = '" + ide + "'";
                    try{
                        System.out.println("\nid_tienda\tid_producto\tstock");
                        
                        Statement stm = con.connect.createStatement();              
                        rs = stm.executeQuery(sql);
                        while(rs.next()){
                            System.out.println("\n" + rs.getInt(1) + "\t" +
                                    rs.getInt(2) + "\t" +
                                    rs.getInt(3));
                        }
                    }catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }
                    
                    break;

                case "7":                 
                    con.insertarProductoTienda();                                      
                    
                    System.out.println("Producto añadido...");

                    break;

                case "8":
                                       
                    con.mostrarExistenciasTienda();
                    
                    System.out.println("Introduzca el id del producto del que quiere actualizar stock.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    ide = teclado.nextInt();
                    
                    System.out.println("Introduzca el nuevo stock del producto.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    in = teclado.nextInt();
                    
                    sql = "UPDATE existencias SET stock = '"+ in + "' WHERE id_tienda = '" + ide + "'";
                    try{                     
                        PreparedStatement pst = con.connect.prepareStatement(sql);
                        pst.execute();
                    } catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }                
                    
                    break;

                case "9":
                    
                    con.mostrarTiendas();
                    
                    System.out.println("Introduzca el id de la tienda de la que quiere consultar el stock.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    ide = teclado.nextInt();
                    
                    sql = "SELECT p.nombre,e.stock FROM existencias e INNER JOIN productos p ON p.id = e.id_producto WHERE "+ ide +"=e.id_tienda";
                    
                    try{
                        Statement stm = con.connect.createStatement();              
                        rs = stm.executeQuery(sql);
                        System.out.println("\nproducto\tstock");
                        while(rs.next()){
                            System.out.println("\n" + rs.getString(1) + "\t" +
                                    rs.getInt(2));
                        }
                    }catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }                   
                                       
                    break;
                    
                case "10":
                    
                    con.mostrarTiendas();
                    
                    System.out.println("Introduzca el id de la tienda de la que quiere consultar el stock.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    ide = teclado.nextInt();
                    
                    sql = "SELECT id_tienda,p.nombre,stock FROM existencias e INNER JOIN tiendas t ON "+ ide +" = e.id_tienda INNER JOIN productos p ON p.id = e.id_producto";
                    
                    try{
                        Statement stm = con.connect.createStatement();              
                        rs = stm.executeQuery(sql);
                        System.out.println("\nid\tproducto\tstock");
                        while(rs.next()){
                            System.out.println("\n" + rs.getInt(1) + "\t" +
                                    rs.getString(2) + "\t" +
                                    rs.getInt(3));
                        }
                    }catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }   
                    
                    System.out.println("Introduzca el id del producto que quiere eliminar.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    in = teclado.nextInt();
                    
                    sql = "SELECT stock FROM existencias WHERE id_tienda ='"+ in +"'";
                    
                    try{
                        Statement stm = con.connect.createStatement();              
                        rs = stm.executeQuery(sql);                        
                        while(rs.next()){
                            if(rs.getInt(1) == 0){
                                sql = "DELETE * FROM existencias WHERE id_tienda ="+ in;
                                try{                     
                                    PreparedStatement pst = con.connect.prepareStatement(sql);
                                    pst.execute();
                                } catch (SQLException ex){
                                    System.err.println(ex.getMessage());
                                }                
                            }else{
                                System.out.println("No se puede eliminar ese producto, todavía hay stock.");
                            }
                        }
                    }catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }   
                                                            
                    break;    
                
                case "11":
                    con.mostrarProductos();
                    
                    System.out.println("Introduzca el id del producto que quiere eliminar.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    in = teclado.nextInt();
                    
                    sql = "SELECT sum(e.stock) FROM existencias e WHERE e.id_producto="+ in;
                    
                    try{
                        Statement stm = con.connect.createStatement();              
                        rs = stm.executeQuery(sql);
                        System.out.println("\nid\tproducto\tstock");
                        while(rs.next()){
                            if(rs.getInt(1) == 0){                                
                                sql = "DELETE ON CASCADE p.* FROM productos p WHERE p.id ="+ in;
                                try{                     
                                    PreparedStatement pst = con.connect.prepareStatement(sql);
                                    pst.execute();
                                } catch (SQLException ex){
                                    System.err.println(ex.getMessage());
                                }                
                                }else{
                                    System.out.println("No se puede eliminar ese producto, todavía hay stock.");
                                }                                
                            }
                    }catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }   
                    
                    break;
                
                case "12":
                    con.insertarEmpleado();
                    
                    System.out.println("Empleado añadido...");
                    
                    break;
                    
                case "13":
                    con.mostrarEmpleados();
                    
                    System.out.println("Introduzca el id del empleado al que quiere imputar horas.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    in = teclado.nextInt();     
                                                            
                    System.out.println("Introduzca las horas a imputar.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    i = teclado.nextInt();     
                    
                    con.mostrarTiendas();
                    
                    System.out.println("Introduzca el id de la tienda en la que se han hecho esas horas.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    ide = teclado.nextInt();
                    
                    sql = "INSERT INTO horas (id_tienda, id_empleados, horas) values (?,?,?)";
                    try{                     
                        PreparedStatement st = con.connect.prepareStatement(sql);
                        st.setInt(1, ide);
                        st.setInt(2, in);
                        st.setInt(3, i);
                        st.execute();
                    } catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    } 
                    
                    System.out.println("Horas registradas...");
                    
                    break;    
                
                case "14":
                    con.mostrarEmpleados();
                    
                    System.out.println("Introduzca el id del empleado que quiere eliminar.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    in = teclado.nextInt();                    
                                                  
                    sql = "DELETE FROM empleados WHERE id="+ in;
                    try{                     
                        PreparedStatement pst = con.connect.prepareStatement(sql);
                        pst.execute();
                    } catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }    
                    
                    System.out.println("Empleado eliminado...");
                                        
                    break;
                    
                case "15":
                    con.insertarCliente();
                    
                    System.out.println("Cliente creado...");
                    
                    break;
                    
                case "16":
                    con.mostrarClientes();
                    
                    break;
                
                case "17":
                    con.mostrarClientes();
                    
                    System.out.println("Introduzca el id del cliente que quiere eliminar.");
                    while(!teclado.hasNextInt()){
                        System.out.println("Valor incorrecto, vuelva a intentarlo.");
                    }
                    in = teclado.nextInt();                    
                                                  
                    sql = "DELETE FROM clientes WHERE id ="+ in;
                    try{                     
                        PreparedStatement pst = con.connect.prepareStatement(sql);
                        pst.execute();
                    } catch (SQLException ex){
                        System.err.println(ex.getMessage());
                    }    
                    
                    System.out.println("Cliente eliminado...");
                    
                    break;
                    
                case "18":
                    XMLReader procesadorXML = null;
                    try {

                        //Creamos un parseador de texto e engadimoslle a nosa clase que vai parsear o texto
                        procesadorXML = XMLReaderFactory.createXMLReader();
                        TitularesXML titularesXML = new TitularesXML();
                        procesadorXML.setContentHandler(titularesXML);

                        //Indicamos o texto donde estan gardadas as persoas
                        InputSource arquivo = new InputSource("http://ep00.epimg.net/rss/elpais/portada.xml");
                        procesadorXML.parse(arquivo);

                        //Imprimimos os datos lidos no XML
                        ArrayList<Titular> titulares = titularesXML.getTitulares();
                        for(i=0;i<titulares.size();i++){
                            Titular tituloAux = titulares.get(i);
                            System.out.println("Titular: " + tituloAux.getTitular());
                        }

                    } catch (SAXException | IOException e) {
                        System.out.println("Ocurriu un erro ao ler o arquivo XML");
                    }
                    System.out.println("\nPresione cualquier tecla para salir de la sección noticias.");
                    teclado.nextLine();
                    break;

                    
                case "0":
                    return;

                default:
                    System.out.println("El valor introducido debe ser un número comprendido entre 0 y 10, ambos inclusive.\n");
                                        
            }   
            
        }    
    }  
}
