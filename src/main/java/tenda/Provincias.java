/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tenda;

/**
 *
 * @author Hemihundias
 */
public class Provincias {
    String nombre;
    int id;

    public Provincias() {
        this.id = 0;
        this.nombre = "";
    }

    public Provincias(String nome, int id) {
        this.nombre = nome;
        this.id = id;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nome) {
        this.nombre = nome;
    }
    
    
}