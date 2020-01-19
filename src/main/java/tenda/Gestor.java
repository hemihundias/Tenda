/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenda;

import java.io.IOException;
import java.util.Scanner;

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
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        boolean menu = true;
        Class.forName("org.sqlite.JDBC");
        Scanner teclado = new Scanner(System.in);           
        
        con.connect();
        con.crearBaseDatos();
        con.cargarProvincias();              
                                
        while(menu){ 
            
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
                                        
                    break;

                case "2":
                    con.mostrarTiendas();
                    
                    break;

                case "3":
                    con.eliminarTienda();
                    
                    break;

                case "4":
                    con.insertarProducto();
                                        
                    break;

                case "5":
                    con.mostrarProductos();                    

                    break;

                case "6":
                    con.mostrarTiendas();
                    con.consultarProductosTienda();
                    
                    break;

                case "7":                 
                    con.insertarProductoTienda();                                      
                                        
                    break;

                case "8":                                       
                    con.mostrarExistenciasTienda();
                    con.actualizarStock();                          
                    
                    break;

                case "9":
                    con.mostrarTiendas();
                    con.consultarStock();                      
                                       
                    break;
                    
                case "10":                    
                    con.mostrarTiendas();
                    con.eliminarProductoTienda();                    
                                                            
                    break;    
                
                case "11":
                    con.mostrarProductos();
                    con.eliminarProducto();
                    
                    break;
                
                case "12":
                    con.insertarEmpleado();
                                                            
                    break;
                    
                case "13":
                    con.registrarHoras();
                                                            
                    break;    
                
                case "14":
                    con.mostrarEmpleados();
                    con.eliminarEmpleado();                    
                                                                                
                    break;
                    
                case "15":
                    con.insertarCliente();
                                        
                    break;
                    
                case "16":
                    con.mostrarClientes();
                    
                    break;
                
                case "17":
                    con.mostrarClientes();                    
                    con.eliminarCliente();
                                                            
                    break;
                    
                case "18":
                    con.leerTitulares();
                    
                    break;
                    
                case "0":
                    con.close();
                    
                    return;

                default:
                    System.out.println("El valor introducido debe ser un número comprendido entre 0 y 10, ambos inclusive.\n");
                                        
            }   
            
        }    
    }  
}
