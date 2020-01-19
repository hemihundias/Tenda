/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenda;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Hemihundias
 */
public class Conector {
    private int in,ide,i,id_provincia;
    private String sql,sql1,sql2,sql3,sql4,sql5,sql6,nombre,apellidos, email,descripcion,provincia,ciudad;
    private ResultSet rs;
    private Double precio;
    private boolean x = true;
    private File datos;
    private Properties propiedadesConex;
    private Statement stm;
    private PreparedStatement pst;
    private Connection connect;
    
    Scanner teclado = new Scanner(System.in);
    String url = "tenda.db";
    
    //Método para la creación de la conexión a la base de datos,de no existir esta, la crea.
    public void connect(){
        try {
            propiedadesConex=new Properties();
            propiedadesConex.setProperty("foreign_keys", "true");      
            connect = DriverManager.getConnection("jdbc:SQLite:"+url,propiedadesConex);
            if (connect!=null) {
                System.out.println("Conectado");
            }
        }catch (SQLException ex) {
            System.err.println("No se ha podido conectar a la base de datos\n"+ex.getMessage());
        }
    }
    
    //Método para el cierre de la conexión a la bd
    public void close(){
            try {
                connect.close();
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    //Método por el cual creamos, de no existir, la estructura de nuestra base de datos
    public void crearBaseDatos(){               
        sql = "CREATE TABLE IF NOT EXISTS provincias (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY,\n"
                + "    nombre VARCHAR NOT NULL\n"                
                + ");";
        
        sql1 = "CREATE TABLE IF NOT EXISTS tiendas (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre VARCHAR NOT NULL,\n"
                + "    id_provincia VARCHAR NOT NULL,\n"
                + "    ciudad VARCHAR NOT NULL,\n"
                + "    foreign key(id_provincia) references provincias(id)\n"
                + ");";
                        
        sql2 = "CREATE TABLE IF NOT EXISTS productos (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre VARCHAR NOT NULL,\n"
                + "    descripcion VARCHAR NOT NULL,\n"
                + "    precio REAL NOT NULL\n"
                + ");";
        
        sql3 = "CREATE TABLE IF NOT EXISTS existencias (\n"
                + "    id_tienda INTEGER NOT NULL,\n"
                + "    id_producto INTEGER NOT NULL,\n"
                + "    stock INTEGER NULL,\n"
                + "    PRIMARY KEY(id_tienda, id_producto),\n"
                + "    foreign key(id_tienda) references tiendas(id),\n"
                + "    foreign key(id_producto) references productos(id)\n"
                + ");";
        
        sql4 = "CREATE TABLE IF NOT EXISTS empleados (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre VARCHAR NOT NULL,\n"
                + "    apellidos VARCHAR NOT NULL\n"
                + ");";
        
        sql5 = "CREATE TABLE IF NOT EXISTS horas (\n"
                + "    id_tienda INTEGER NOT NULL,\n"
                + "    id_empleados INTEGER NOT NULL,\n"
                + "    horas INTEGER NULL,\n"
                + "    PRIMARY KEY(id_tienda, id_empleados),\n"
                + "    foreign key(id_tienda) references tiendas(id),\n"
                + "    foreign key(id_empleados) references empleados(id)\n"
                + ");";
        
        sql6 = "CREATE TABLE IF NOT EXISTS clientes (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre VARCHAR NOT NULL,\n"
                + "    apellidos VARCHAR NOT NULL,\n"
                + "    email VARCHAR NOT NULL\n"
                + ");";
        
        try {
            Statement stmt = connect.createStatement();
            stmt.execute(sql);
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5);
            stmt.execute(sql6);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //Mediante este método sacamos la información de las provincias contenida en el json, la pasamos a la clase Provincias, y posteriormente a la bd mediante el método insertarProvincias
    public void cargarProvincias() {
        datos = new File("provincias.json");
        if(datos.exists()){
            try{     
                FileReader fluxoDatos = new FileReader(datos);                
                BufferedReader buferEntrada = new BufferedReader(fluxoDatos);
                
                StringBuilder jsonBuilder = new StringBuilder();
                String linea;
                    
                while ((linea=buferEntrada.readLine()) != null) {
                    jsonBuilder.append(linea).append("\n");                    
                }
                String json = jsonBuilder.toString();
                
                Gson gson = new Gson(); 
 
                Provincias[] provincias = gson.fromJson(json, Provincias[].class);  
                
                for(Provincias prov:provincias){                  
                    insertarProvincias(prov.id,prov.nombre);
                }
                
            }catch (JsonSyntaxException | IOException ex){
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("Imposible actualizar datos de las provincias.");
        }         
    }     
    
    //Método mediante el que insertaremos los datos de las provincias en la bd
    public void insertarProvincias(int id, String nombre){
        sql = "INSERT OR IGNORE INTO provincias (id, nombre) values (?,?)";
        
        try {
            pst = connect.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, nombre);
            pst.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    //Método para la creación de una nueva Tienda en la bd
    public void insertarTienda(){                
        System.out.println("Teclee nombre de la nueva tienda:");
        nombre = teclado.next();
                    
        System.out.println("Teclee la provincia de emplazamiento de la tienda:");                    
        while(x){
            provincia = teclado.next();
            sql = "SELECT * FROM provincias WHERE nombre='"+ provincia +"'";
            
            try {
                stm = connect.createStatement();
                rs = stm.executeQuery(sql);
                rs.next();
                if(provincia.equalsIgnoreCase(rs.getString(2))){
                    x= false;
                    id_provincia = rs.getInt(1);
                }else{
                    System.out.println("Valor incorrecto. Teclee la provincia de emplazamiento de la tienda:");
                }  
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }                        
        }
                    
        System.out.println("Teclee la ciudad de emplazamiento de la tienda:");
        ciudad = teclado.next();   
        
        sql = "INSERT INTO tiendas (nombre, id_provincia, ciudad) values (?,?,?)";
        try{                     
            pst = connect.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setInt(2, id_provincia);
            pst.setString(3, ciudad);  
            pst.execute();
            System.out.println("Tienda creada...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                
    }
    
    //Método que nos devuelve todas las tiendas dadas de alta en la bd
    public void mostrarTiendas(){
        sql = "SELECT t.id,t.nombre,p.nombre,t.ciudad FROM tiendas t INNER JOIN provincias p ON t.id_provincia = p.id";
        rs = null;
        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            System.out.printf("%-20s%-20s%-20s%-20s\n","id","Nombre","Provincia","Ciudad");
            while(rs.next()){
                System.out.printf("%-20s%-20s%-20s%-20s\n",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }        
    }
    
    //Método que nos permite eliminar una tienda
    public void eliminarTienda(){
        mostrarTiendas();
        System.out.println("Teclee el número id de la tienda que quiere eliminar.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();
        
        sql = "DELETE FROM tiendas WHERE id = '" + ide + "'";
        try{
            pst = connect.prepareStatement(sql);
            pst.execute();
            System.out.println("Tienda eliminada...");
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    //Método para dar de alta un nuevo producto
    public void insertarProducto(){                        
        System.out.println("Teclee nombre del nuevo producto:");
        nombre = teclado.nextLine();
        
        System.out.println("Teclee una descripción del producto:");
        descripcion = teclado.nextLine();
                            
        System.out.println("Teclee el precio del producto:");
        while(!teclado.hasNextDouble()){
            System.out.println("Valor incorrecto, intentelo de nuevo.");
        }
        precio = teclado.nextDouble();
        
        sql = "INSERT INTO productos (nombre, descripcion, precio) values (?,?,?)";
        try{                     
            pst = connect.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, descripcion);
            pst.setDouble(3, precio);  
            pst.execute();
            System.out.println("Producto creado...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                
        teclado.nextLine();
    }
    
    //Método que nos devuelve todos los productos dados de alta
    public void mostrarProductos(){
        sql = "SELECT * FROM productos";
        rs = null;
        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            System.out.printf("%-20s%-20s%-20s%-20s\n","id","Nombre","Descripción","Precio");
            while(rs.next()){
                System.out.printf("%-20s%-20s%-20s%-20s\n",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }        
    }
    
    //Método para dar de alta un producto en una tienda
    public void insertarProductoTienda(){             
        mostrarTiendas();
        System.out.println("Introduzca el número id de la tienda a la que se añadira el producto.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();
                    
        mostrarProductos();
        System.out.println("Introduzca el número id del producto que quiere añadir a la tienda.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        in = teclado.nextInt();
        
        System.out.println("Introduzca el stock del producto.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        i = teclado.nextInt();
        
        sql = "INSERT INTO existencias (id_tienda, id_producto, stock) values (?,?,?)";
        try{                     
            pst = connect.prepareStatement(sql);
            pst.setInt(1, ide);
            pst.setInt(2, in);
            pst.setInt(3, i);  
            pst.execute();
            
            System.out.println("Producto añadido...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                
    }
    
    //Método que nos devuelve la cantidad de cada producto que posee una tienda en concreto
    public void mostrarExistenciasTienda(){
        sql = "SELECT id_tienda,t.nombre,p.nombre,stock FROM existencias e INNER JOIN tiendas t ON t.id = e.id_tienda INNER JOIN productos p ON p.id = e.id_producto";
        rs = null;
        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            System.out.printf("%-20s%-20s%-20s%-20s\n","id","Tiendas","Producto","Stock");
            while(rs.next()){
                System.out.printf("%-20s%-20s%-20s%-20s\n",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }        
    }
    
    //Método que nos permite dar de alta un nuevo empleado
    public void insertarEmpleado(){                                
        System.out.println("Teclee nombre del nuevo empleado:");
        nombre = teclado.nextLine();
        
        System.out.println("Teclee apellidos:");
        apellidos = teclado.nextLine();                           
                
        sql = "INSERT INTO empleados (nombre, apellidos) values (?,?)";
        try{                     
            pst = connect.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, apellidos);
            pst.execute();
            System.out.println("Empleado dado de alta...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }    
        teclado.nextLine();
    }
    
    //Método que nos devuelve todos los empleados dados de alta
    public void mostrarEmpleados(){
        sql = "SELECT * FROM empleados";
        rs = null;
        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            System.out.printf("%-20s%-20s%-20s\n","id","Nombre","Apellidos");
            while(rs.next()){
                System.out.printf("%-20s%-20s%-20s\n",rs.getInt(1),rs.getString(2),rs.getString(3));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    //Método ppara dar de alta un nuevo cliente
    public void insertarCliente(){                                
        System.out.println("Teclee nombre del nuevo cliente:");
        nombre = teclado.nextLine();
        
        System.out.println("Teclee apellidos:");
        apellidos = teclado.nextLine();    
        
        System.out.println("Teclee mail:");
        email = teclado.nextLine(); 
                
        sql = "INSERT INTO clientes (nombre, apellidos, email) values (?,?,?)";
        try{                     
            pst = connect.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, apellidos);
            pst.setString(3, email);
            pst.execute();
            System.out.println("Cliente creado...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }         
    }
    
    //Método para mostrar los clientes dados de alta
    public void mostrarClientes(){
        sql = "SELECT * FROM clientes";
        rs = null;
        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            System.out.printf("%-20s%-20s%-20s%-20s\n","id","Nombre","Apellidos","Mail");
            while(rs.next()){
                System.out.printf("%-20s%-20s%-20s%-20s\n",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));                
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
            
    //Método para la eliminación de un cliente
    public void eliminarCliente(){
        System.out.println("Introduzca el id del cliente que quiere eliminar.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        in = teclado.nextInt();                    

        sql = "DELETE FROM clientes WHERE id ="+ in;
        try{                     
            pst = connect.prepareStatement(sql);
            pst.execute();
            System.out.println("Cliente eliminado...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }    
    }
    
    //Método que saca por pantalla los titulares de "El País"
    public void leerTitulares(){
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
        teclado.next();
    }
    
    //Método que nos permite eliminar un empleado
    public void eliminarEmpleado(){
        System.out.println("Introduzca el id del empleado que quiere eliminar.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        in = teclado.nextInt();                    

        sql = "DELETE FROM empleados WHERE id="+ in;
        try{                     
            pst = connect.prepareStatement(sql);
            pst.execute();
            System.out.println("Empleado eliminado...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }    
    }
    
    //Método para registrar las horas trabajadas por cada empleado, y en que tienda.
    public void registrarHoras(){
        mostrarEmpleados();
                    
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

        mostrarTiendas();

        System.out.println("Introduzca el id de la tienda en la que se han hecho esas horas.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();

        sql = "INSERT INTO horas (id_tienda, id_empleados, horas) values (?,?,?)";
        try{                     
            pst = connect.prepareStatement(sql);
            pst.setInt(1, ide);
            pst.setInt(2, in);
            pst.setInt(3, i);
            pst.execute();
            System.out.println("Horas registradas...");
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        } 
    }
    
    //Método para la eliminación de un producto, siempre y cuando su stock sea 0
    public void eliminarProducto(){
        System.out.println("Introduzca el id del producto que quiere eliminar.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        in = teclado.nextInt();

        sql = "SELECT sum(e.stock) FROM existencias e WHERE e.id_producto="+ in;

        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            while(rs.next()){
                if(rs.getInt(1) == 0){                                
                    sql = "DELETE FROM productos WHERE id ="+ in;
                    try{                     
                        pst = connect.prepareStatement(sql);
                        pst.execute();
                        System.out.println("Producto eliminado...");
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
    }
    
    //Método que nos permite eliminar un producto de una tienda en concreto, siempre que su stock sea 0
    public void eliminarProductoTienda(){
        System.out.println("Introduzca el id de la tienda de la que quiere consultar el stock.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();

        sql = "SELECT e.id_tienda,p.nombre,e.stock FROM existencias e INNER JOIN productos p ON p.id = e.id_producto WHERE e.id_tienda="+ide;

        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            System.out.printf("%-20s%-20s%-20s\n","id","Producto","Stock");  
            while(rs.next()){
                System.out.printf("%-20s%-20s%-20s\n",rs.getInt(1),rs.getString(2),rs.getInt(3));                  
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
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);                        
            while(rs.next()){
                if(rs.getInt(1) == 0){
                    sql = "DELETE FROM existencias WHERE id_tienda ="+ in;
                    try{                     
                        pst = connect.prepareStatement(sql);
                        pst.execute();
                        System.out.println("Producto eliminado de la tienda...");
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
    }
    
    //Método para consultar el stock de una tienda en concreto
    public void consultarStock(){
        System.out.println("Introduzca el id de la tienda de la que quiere consultar el stock.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();

        sql = "SELECT p.nombre,e.stock FROM existencias e INNER JOIN productos p ON p.id = e.id_producto WHERE "+ ide +"=e.id_tienda";

        try{
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            System.out.printf("%-20s%-20s\n","Producto","Stock");
            while(rs.next()){
                System.out.printf("%-20s%-20s\n",rs.getString(1),rs.getInt(2));                
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                 
    }
    
    //Método que nos permite moidificar el stock de un producto en una tienda
    public void actualizarStock(){
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
            pst = connect.prepareStatement(sql);
            pst.execute();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }          
    }
    
    //Método que nos permite ver que productos hay en una tienda
    public void consultarProductosTienda(){
        System.out.println("Introduzca el id de la tienda de la que quiere consultar los productos.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();

        sql = "SELECT * FROM existencias WHERE id_tienda = '" + ide + "'";
        try{
            System.out.printf("%-20s%-20s%-20s\n","id_tienda","id_producto","Stock\n");    
            stm = connect.createStatement();              
            rs = stm.executeQuery(sql);
            while(rs.next()){
                System.out.printf("%-20s%-20s%-20s\n",rs.getInt(1),rs.getInt(2),rs.getInt(3) + "\n");                
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
        
}
