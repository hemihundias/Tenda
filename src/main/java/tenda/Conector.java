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
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tenda.Gestor.con;

/**
 *
 * @author Hemihundias
 */
public class Conector {
    Scanner teclado = new Scanner(System.in);
    String url = "C:\\sqlite\\tenda.db";
    Connection connect;
        
    public void connect(){
        try {
            connect = DriverManager.getConnection("jdbc:SQLite:"+url);
            if (connect!=null) {
                //PRAGMA foreign_keys = ON;
                System.out.println("Conectado");
            }
        }catch (SQLException ex) {
            System.err.println("No se ha podido conectar a la base de datos\n"+ex.getMessage());
        }
    }
    public void close(){
            try {
                connect.close();
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void crearBaseDatos(){               
        String sql = "CREATE TABLE IF NOT EXISTS provincias (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY,\n"
                + "    nombre VARCHAR NOT NULL\n"                
                + ");";
        
        String sql1 = "CREATE TABLE IF NOT EXISTS tiendas (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre VARCHAR NOT NULL,\n"
                + "    id_provincia VARCHAR NOT NULL,\n"
                + "    ciudad VARCHAR NOT NULL,\n"
                + "    foreign key(id_provincia) references provincias(id)\n"
                + ");";
                        
        String sql2 = "CREATE TABLE IF NOT EXISTS productos (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre VARCHAR NOT NULL,\n"
                + "    descripcion VARCHAR NOT NULL,\n"
                + "    precio REAL NOT NULL\n"
                + ");";
        
        String sql3 = "CREATE TABLE IF NOT EXISTS existencias (\n"
                + "    id_tienda INTEGER NOT NULL,\n"
                + "    id_producto INTEGER NOT NULL,\n"
                + "    stock INTEGER NULL,\n"
                + "    PRIMARY KEY(id_tienda, id_producto),\n"
                + "    foreign key(id_tienda) references tiendas(id),\n"
                + "    foreign key(id_producto) references productos(id)\n"
                + ");";
        
        String sql4 = "CREATE TABLE IF NOT EXISTS empleados (\n"
                + "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre VARCHAR NOT NULL,\n"
                + "    apellidos VARCHAR NOT NULL\n"
                + ");";
        
        String sql5 = "CREATE TABLE IF NOT EXISTS horas (\n"
                + "    id_tienda INTEGER NOT NULL,\n"
                + "    id_empleados INTEGER NOT NULL,\n"
                + "    horas INTEGER NULL,\n"
                + "    PRIMARY KEY(id_tienda, id_empleados),\n"
                + "    foreign key(id_tienda) references tiendas(id),\n"
                + "    foreign key(id_empleados) references empleados(id)\n"
                + ");";
        
        String sql6 = "CREATE TABLE IF NOT EXISTS clientes (\n"
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
    
    public void cargarProvincias() {
        File datos = new File("provincias.json");
        if(datos.exists()){
            try{     
                FileReader fluxoDatos;
                fluxoDatos = new FileReader(datos);
                
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
    
    public void insertarProvincias(int id, String nombre){
        String sql = "INSERT OR IGNORE INTO provincias (id, nombre) values (?,?)";
        
        try {
            PreparedStatement st = connect.prepareStatement(sql);
            st.setInt(1, id);
            st.setString(2, nombre);
            st.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
    
    public void insertarTienda(){
        boolean x = true;
        int id_provincia = 0;
        String nombre,provincia,ciudad;
        System.out.println("Teclee nombre de la nueva tienda:");
        nombre = teclado.next();
                    
        System.out.println("Teclee la provincia de emplazamiento de la tienda:");                    
        while(x){
            provincia = teclado.next();
            String sql = "SELECT * FROM provincias WHERE nombre='"+ provincia +"'";
            
            try {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
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
        
        String sql = "INSERT INTO tiendas (nombre, id_provincia, ciudad) values (?,?,?)";
        try{                     
            PreparedStatement st = connect.prepareStatement(sql);
            st.setString(1, nombre);
            st.setInt(2, id_provincia);
            st.setString(3, ciudad);  
            st.execute();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                
    }
    
    public void mostrarTiendas(){
        String sql = "SELECT t.id,t.nombre,p.nombre,t.ciudad FROM tiendas t INNER JOIN provincias p ON t.id_provincia = p.id";
        ResultSet rs = null;
        try{
            Statement st = connect.createStatement();              
            rs = st.executeQuery(sql);
            System.out.println("\nid\tnombre\tprovincia\tciudad");
            while(rs.next()){
                System.out.println("\n" + rs.getInt(1) + "\t" +
                        rs.getString(2) + "\t" +
                        rs.getString(3) + "\t" +
                        rs.getString(4));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }        
    }
    
    public void eliminarTienda(){
        mostrarTiendas();
        System.out.println("Teclee el número id de la tienda que quiere eliminar.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        int ide = teclado.nextInt();
        
        String sql = "DELETE FROM tiendas WHERE id = '" + ide + "'";
        try{
            PreparedStatement stm = connect.prepareStatement(sql);
            stm.execute();
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    public void insertarProducto(){
        String nombre,descripcion;
        Double precio;
                
        System.out.println("Teclee nombre del nuevo producto:");
        nombre = teclado.nextLine();
        
        System.out.println("Teclee una descripción del producto:");
        descripcion = teclado.nextLine();
                            
        System.out.println("Teclee el precio del producto:");
        while(!teclado.hasNextDouble()){
            System.out.println("Valor incorrecto, intentelo de nuevo.");
        }
        precio = teclado.nextDouble();
        
        String sql = "INSERT INTO productos (nombre, descripcion, precio) values (?,?,?)";
        try{                     
            PreparedStatement st = connect.prepareStatement(sql);
            st.setString(1, nombre);
            st.setString(2, descripcion);
            st.setDouble(3, precio);  
            st.execute();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                
        teclado.nextLine();
    }
    
    public void mostrarProductos(){
        String sql = "SELECT * FROM productos";
        ResultSet rs = null;
        try{
            Statement st = connect.createStatement();              
            rs = st.executeQuery(sql);
            System.out.println("\nid\tnombre\tdescripcion\tprecio");
            while(rs.next()){
                System.out.println("\n" + rs.getInt(1) + "\t" +
                        rs.getString(2) + "\t" +
                        rs.getString(3) + "\t" +
                        rs.getDouble(4));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }        
    }
    
    public void insertarProductoTienda(){
        int ide,in,ine;
                
        con.mostrarTiendas();
        System.out.println("Introduzca el número id de la tienda a la que se añadira el producto.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();
                    
        con.mostrarProductos();
        System.out.println("Introduzca el número id del producto que quiere añadir a la tienda.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        in = teclado.nextInt();
        
        System.out.println("Introduzca el stock del producto.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ine = teclado.nextInt();
        
        String sql = "INSERT INTO existencias (id_tienda, id_producto, stock) values (?,?,?)";
        try{                     
            PreparedStatement st = connect.prepareStatement(sql);
            st.setInt(1, ide);
            st.setInt(2, in);
            st.setInt(3, ine);  
            st.execute();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                
    }
    
    public void mostrarExistenciasTienda(){
        String sql = "SELECT id_tienda,t.nombre,p.nombre,stock FROM existencias e INNER JOIN tiendas t ON t.id = e.id_tienda INNER JOIN productos p ON p.id = e.id_producto";
        ResultSet rs = null;
        try{
            Statement st = connect.createStatement();              
            rs = st.executeQuery(sql);
            System.out.println("\nid\ttienda\tproducto\tstock");
            while(rs.next()){
                System.out.println("\n" + rs.getString(1) + "\t" +
                        rs.getString(2) + "\t" +
                        rs.getString(3) + "\t" +
                        rs.getInt(4));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }        
    }
    
    public void insertarEmpleado(){
        String nombre,apellidos;
                        
        System.out.println("Teclee nombre del nuevo empleado:");
        nombre = teclado.nextLine();
        
        System.out.println("Teclee apellidos:");
        apellidos = teclado.nextLine();                           
                
        String sql = "INSERT INTO empleados (nombre, apellidos) values (?,?)";
        try{                     
            PreparedStatement st = connect.prepareStatement(sql);
            st.setString(1, nombre);
            st.setString(2, apellidos);
            st.execute();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }    
        teclado.nextLine();
    }
    
    public void mostrarEmpleados(){
        String sql = "SELECT * FROM empleados";
        ResultSet rs = null;
        try{
            Statement st = connect.createStatement();              
            rs = st.executeQuery(sql);
            System.out.println("\nid\tnombre\tapellidos");
            while(rs.next()){
                System.out.println("\n" + rs.getInt(1) + "\t" +
                        rs.getString(2) + "\t" +
                        rs.getString(3));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    public void insertarCliente(){
        String nombre,apellidos, email;
                        
        System.out.println("Teclee nombre del nuevo cliente:");
        nombre = teclado.nextLine();
        
        System.out.println("Teclee apellidos:");
        apellidos = teclado.nextLine();    
        
        System.out.println("Teclee mail:");
        email = teclado.nextLine(); 
                
        String sql = "INSERT INTO clientes (nombre, apellidos, email) values (?,?,?)";
        try{                     
            PreparedStatement st = connect.prepareStatement(sql);
            st.setString(1, nombre);
            st.setString(2, apellidos);
            st.setString(3, email);
            st.execute();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        } 
        
    }
    
    public void mostrarClientes(){
        String sql = "SELECT * FROM clientes";
        ResultSet rs = null;
        try{
            Statement st = connect.createStatement();              
            rs = st.executeQuery(sql);
            System.out.println("\nid\tnombre\tapellidos\tmail");
            while(rs.next()){
                System.out.println("\n" + rs.getInt(1) + "\t" +
                        rs.getString(2) + "\t" +
                        rs.getString(3) + "\t" +
                        rs.getString(4));
            }
        }catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    public void registrarHoras(){
        int ide,in,ine;
                
        con.mostrarEmpleados();
                    
        System.out.println("Introduzca el id del empleado al que se le van a registrar las horas.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        in = teclado.nextInt();

        con.mostrarTiendas();

        System.out.println("Introduzca el id de la tienda a la que se van a computar las horas.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ide = teclado.nextInt();

        System.out.println("Introduzca el número de horas a registrar.");
        while(!teclado.hasNextInt()){
            System.out.println("Valor incorrecto, vuelva a intentarlo.");
        }
        ine = teclado.nextInt();
        
        String sql = "INSERT INTO horas (id_tienda, id_empleado, horas) values (?,?,?)";
        try{                     
            PreparedStatement st = connect.prepareStatement(sql);
            st.setInt(1, in);
            st.setInt(2, ide);
            st.setInt(3, ine);  
            st.execute();
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }                
    }
        
}
