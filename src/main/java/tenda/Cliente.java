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
public class Cliente {
    private final String Nome;
    private final String Apelidos;
    private String mail;
    
    public Cliente(String nome,String apelidos,String mail) {
        this.Nome = nome;
        this.Apelidos = apelidos;
        this.mail = mail;
    }

    public String getNome() {
        return Nome;
    }
    
    public String getApelidos() {
        return Apelidos;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
}
