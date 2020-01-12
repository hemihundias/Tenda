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
public class Empregado {
    private final String Nome;
    private final String Apelidos;
    
    public Empregado(String nome, String apelidos) {
        this.Nome = nome;
        this.Apelidos = apelidos;
    }

    public String getNome() {
        return Nome;
    }
    
    public String getApelidos() {
        return Apelidos;
    }
    
}
