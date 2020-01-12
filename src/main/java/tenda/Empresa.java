/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenda;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hemihundias
 */
public class Empresa {
    private List<Tienda> tendas;
    private List<Cliente> clientes;
    

    public Empresa() {
        this.tendas = new ArrayList();
        this.clientes = new ArrayList();
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Tienda> getTiendas() {
        return tendas;
    }
    
}
