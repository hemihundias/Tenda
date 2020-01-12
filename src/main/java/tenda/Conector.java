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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hemihundias
 */
public class Conector {
    String url = "C:\\sqlite\\tenda.db";
    Connection connect;
    
    private List<Tienda> tienda;
    
    public void connect(){
        try {
            connect = DriverManager.getConnection("jdbc:SQLite:"+url);
            if (connect!=null) {
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
                + "    id INTEGER NOT NULL,\n"
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
                + "    id_tienda VARCHAR NOT NULL,\n"
                + "    id_producto VARCHAR NOT NULL,\n"
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
                + "    id_tienda VARCHAR NOT NULL,\n"
                + "    id_empleados VARCHAR NOT NULL,\n"
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
        String sql = "INSERT INTO provincias (id, nombre) values (?,?)";
        
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
        String sql = "INSERT INTO tiendas (nombre, id_provincia, ciudad) values (?,?)";
        String sql1 = "SELECT id FROM PROFF WHERE nombre=";
        try{
            
            for(int i=0; i<tienda.size();i++){
                Statement stmt = connect.createStatement();
                ResultSet rs =  stmt.executeQuery(sql1+tienda.get(i).getCidade());
                PreparedStatement st = connect.prepareStatement(sql);
                st.setString(1, tienda.get(i).getNome());
                st.setString(2, rs.toString());
                st.setString(3, tienda.get(i).getCidade());
            }
                
        } catch (SQLException ex){
            System.err.println(ex.getMessage());
        }
                
    }
        
}
