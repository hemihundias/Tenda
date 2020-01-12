/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    static Empresa empresa = new Empresa();
    static Conector con = new Conector();
    static List<Tienda> tiendas = new ArrayList();
    static List<Cliente> clientes = new ArrayList(); 
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        boolean menu = true;
        Class.forName("org.sqlite.JDBC");
        
        con.connect();
        con.crearBaseDatos();
        con.cargarProvincias();
        
        String nombre,ciudad,id,descripcion,apellidos,mail;
        Double precio;
        int cantidad,i,t;
               
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
            System.out.println("13. Eliminar empleado.");
            System.out.println("14. Añadir un cliente.");
            System.out.println("15. Mostrar clientes.");
            System.out.println("16. Eliminar un cliente.");
            System.out.println("17. Leer titulares El País.");
            System.out.println("0. Salir.\n");
            
            System.out.println("Por favor elixa unha opción:");
            
            String entrada = teclado.nextLine();
            
            boolean eliminado = false;
            switch(entrada){                        
                case "1":
                    System.out.println("Teclee nombre de la nueva tienda:");
                    nombre = teclado.nextLine();
                    
                    System.out.println("Teclee la ciudad de emplazamiento de la tienda:");
                    ciudad = teclado.nextLine();
                    
                    empresa.getTiendas().add(new Tienda(nombre,ciudad));    
                    con.insertarTienda();
                    break;

                case "2":
                    if(empresa.getTiendas().isEmpty()){
                        System.out.println("Non existen tendas para eliminar.\n");
                        break;
                    }
                    System.out.println("Por favor introduza a tenda a eliminar:");
                    nombre = teclado.nextLine();

                    for(i=0;i<empresa.getTiendas().size();i++){
                        if(empresa.getTiendas().get(i).getNome().equalsIgnoreCase(nombre)){
                            empresa.getTiendas().remove(i);                            
                            eliminado = true;    
                            break;
                        }
                    } 
                    if(eliminado){
                        System.out.println("Tenda eliminada correctamente.\n");
                        break;
                    }
                    System.out.println("Non se atopa ningunha tenda para o valor engadido.\n");

                    break;

                case "3":
                    if(empresa.getTiendas().isEmpty()){
                        System.out.println("Non existen tendas as que engadir productos.\n");
                        break;
                    }
                    System.out.println("¿A que tenda quere engadir o producto?");
                    nombre = teclado.nextLine();
                    for(i=0;i<empresa.getTiendas().size();i++){
                        if(empresa.getTiendas().get(i).getNome().equalsIgnoreCase(nombre)){  
                            System.out.println("Engada identificador do novo producto:");
                            id = teclado.nextLine();
                            t =i;
                            if(!empresa.getTiendas().get(t).getProductos().isEmpty()){
                                for(i=0;i<empresa.getTiendas().get(t).getProductos().size();i++){
                                    if(empresa.getTiendas().get(t).getProductos().get(i).getId().equalsIgnoreCase(id)){
                                        System.out.println("Ese identificador xa existe.\n");
                                        eliminado = true;
                                        break;
                                    }
                                }                                   
                            }                                                                                   
                            if(eliminado){
                                break;
                            }
                            System.out.println("Engada descripción do producto:");
                            descripcion = teclado.nextLine();
                            System.out.println("Engada prezo do producto:");
                            precio = teclado.nextDouble();
                            System.out.println("Engada cantidade do producto:");
                            cantidad = teclado.nextInt();
                            empresa.getTiendas().get(i).getProductos().add(new Producto(id,descripcion,precio,cantidad)); 
                            eliminado = true;
                        }
                    }
                    if(eliminado){
                        break;
                    }
                    System.out.println("A tenda non existe.\n");
                    
                    break;

                case "4":
                    if(empresa.getTiendas().isEmpty()){
                        System.out.println("Non existen tendas para a eliminación de productos.\n");
                        break;
                    }
                    System.out.println("Por favor introduza a tenda da que quere eliminar o producto:\n");
                    nombre = teclado.nextLine();
                       
                    for(i=0;i<empresa.getTiendas().size();i++){
                        if(empresa.getTiendas().get(i).getNome().equalsIgnoreCase(nombre) && !empresa.getTiendas().get(i).getProductos().isEmpty()){
                            System.out.println("¿Que producto desexa eliminar? Indique o identificador.\n");
                            id = teclado.nextLine();
                            t = i;
                            for(i = 0;i<empresa.getTiendas().get(t).getProductos().size();i++){
                                if(empresa.getTiendas().get(t).getProductos().get(i).getId().equalsIgnoreCase(id)){
                                    empresa.getTiendas().get(t).getProductos().remove(i);
                                    System.out.println("Producto eliminado correctamente.\n");
                                    eliminado = true;
                                    break;
                                }
                            }                          
                           
                        }
                    } 
                    if(eliminado){
                        break;
                    }
                    System.out.println("Non se atopa o elemento desexado.\n");

                    break;

                case "5":
                    if(empresa.getTiendas().isEmpty()){
                        System.out.println("Non existen tendas ás que engadir empregados.\n");
                        break;
                    }
                    System.out.println("Por favor introduza a tenda na que se incluirá o novo empregado:\n");
                    nombre = teclado.nextLine();
                                                            
                    for(i=0;i<empresa.getTiendas().size();i++){
                        if(empresa.getTiendas().get(i).getNome().equalsIgnoreCase(nombre)){
                            System.out.println("¿Cal é o nome do novo empregado?.\n");
                            nombre = teclado.nextLine();
                            System.out.println("¿Cales son os apelidos do novo empregado?.\n");
                            apellidos = teclado.nextLine();
                            t = i;
                            if(!empresa.getTiendas().get(t).getEmpregados().isEmpty()){
                                for(i = 0;i<empresa.getTiendas().get(t).getEmpregados().size();i++){
                                    if(!empresa.getTiendas().get(t).getEmpregados().get(i).getNome().equalsIgnoreCase(nombre) || !empresa.getTiendas().get(t).getEmpregados().get(i).getApelidos().equalsIgnoreCase(apellidos)){
                                        empresa.getTiendas().get(t).getEmpregados().add(new Empregado(nombre,apellidos));
                                        System.out.println("Empregado engadido correctamente.\n");
                                        eliminado = true;    
                                        break;
                                    }
                                }
                            }else{
                                empresa.getTiendas().get(t).getEmpregados().add(new Empregado(nombre,apellidos));
                                eliminado = true;    
                                break;
                            }
                            
                        }
                    } 
                    if(eliminado){
                        break;
                    }
                    System.out.println("Non foi posible engadir o empregado.\n");

                    break;

                case "6":
                    if(empresa.getTiendas().isEmpty()){
                        System.out.println("Non existen tendas para a eliminación de empregados.\n");
                        break;
                    }
                    System.out.println("Por favor introduza a tenda da que quere dar de baixa o empregado:\n");
                    nombre = teclado.nextLine();
                       
                    for(i=0;i<empresa.getTiendas().size();i++){
                        if(empresa.getTiendas().get(i).getNome().equalsIgnoreCase(nombre) && !empresa.getTiendas().get(i).getEmpregados().isEmpty()){
                            System.out.println("¿Que empregado desexa dar de baixa? Indique o nome:");
                            nombre = teclado.nextLine();
                            System.out.println("Agora os apelidos:");
                            apellidos = teclado.nextLine();
                            t = i;
                            for(i = 0;i<empresa.getTiendas().get(t).getEmpregados().size();i++){
                                if(empresa.getTiendas().get(t).getEmpregados().get(i).getNome().equalsIgnoreCase(nombre) && empresa.getTiendas().get(t).getEmpregados().get(i).getApelidos().equalsIgnoreCase(apellidos)){
                                    empresa.getTiendas().get(t).getEmpregados().remove(i);
                                    System.out.println("Empregado dado de baixa correctamente.\n");
                                    eliminado = true;
                                    break;
                                }
                            }                          
                           
                        }
                    } 
                    if(eliminado){
                        break;
                    }
                    System.out.println("Non foi posible dar de baixa o empregado.\n");

                    break;

                case "7":                  
                    System.out.println("Engada nome do novo cliente:\n");
                    nombre = teclado.nextLine();
                    System.out.println("Engada apelidos do cliente:\n");
                    apellidos = teclado.nextLine();
                    System.out.println("Engada correo electrónico do cliente:\n");
                    mail = teclado.nextLine();
                    for(i = 0;i<empresa.getClientes().size();i++){
                        if(empresa.getClientes().get(i).getMail().equalsIgnoreCase(mail)){
                            System.out.println("O cliente xa existe.");
                            eliminado = true;
                        }
                    }
                    if(eliminado){
                        System.out.println("O cliente xa existe.\n");
                    }else{
                        empresa.getClientes().add(new Cliente(nombre,apellidos,mail));
                                                
                    }

                    break;

                case "8":
                    if(empresa.getClientes().isEmpty()){
                        System.out.println("Non hai clientes para borrar.\n");
                        break;
                    }
                    System.out.println("Por favor introduza mail do cliente a eliminar:\n");
                    mail = teclado.nextLine();

                    for(i = 0;i<empresa.getClientes().size();i++){
                        if(empresa.getClientes().get(i).getMail().equalsIgnoreCase(mail)){
                            empresa.getClientes().remove(i);                                                        
                            System.out.println("Cliente eliminado correctamente.\n");
                            eliminado = true;
                            break;
                        }

                    } 
                    if(eliminado){
                        break;
                    }
                    System.out.println("Non se atopa ningún cliente para o valor engadido.\n");

                    break;

                case "9":
                    
                case "10":
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

                    } catch (SAXException e) {
                        System.out.println("Ocurriu un erro ao ler o arquivo XML");
                    } catch (IOException e) {
                        System.out.println("Ocurriu un erro ao ler o arquivo XML");
                    }
                    System.out.println("\nPrema calquer tecla para saír da sección noticias.");
                    teclado.nextLine();
                    break;
                    
                case "0":
                    return;

                default:
                    System.out.println("O valor introducido debe ser un número comprendido entre o 0 e o 10, ambos inclusive.\n");
                                        
            }   
            
        }    
    }   
        
    public static void escribirJson(){
        //Pasamos a nosa clase a JSON utilizando a libreria GSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(empresa);
                
        //Vamos comezar declarando o ficheiro
        File ficheiro = new File("data.json");

        try {
            //Creamos o fluxo de saida
            FileWriter fluxoDatos = new FileWriter(ficheiro);
            BufferedWriter buferSaida = new BufferedWriter(fluxoDatos);

            buferSaida.write(json);
                        
            buferSaida.close();
        } catch (IOException e) {
            System.err.println(e);    
        }                        
    }
}
