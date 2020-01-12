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
public class Producto {
    private final String Id;
    private final String Descripcion;
    private double prezo;
    private int cantidade;
            
    public Producto(String id,String descripcion,double prezo,int cantidade) {
        this.Id = id;
        this.Descripcion = descripcion;
        this.prezo = prezo;
        this.cantidade = cantidade;
    }

    public String getId() {
        return Id;
    }   

    public String getDescripcion() {
        return Descripcion;
    }
    
    public double getPrezo() {
        return prezo;
    }

    public int getCantidade() {
        return cantidade;
    }

    
    
}
