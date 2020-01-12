/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenda;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class TitularesXML extends DefaultHandler {
    private int nivel = 0;
    //Aqui imos gardar os datos de todalos titulares do XML
    private ArrayList<Titular> titulares;

    //E un atributo auxiliar para ir gardando os datos do titular do XML
    private Titular tituloAux;

    //E un atributo auxiliar po texto que hai entre as etiquetas
    private String cadeaTexto;

    public TitularesXML(){
        super();
    }
        
    /*
    Este metodo executase ao comezar a ler unha etiqueta
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        //Se atopamos a etiqueta channel creamos un novo arrayList
        if(localName.equalsIgnoreCase("channel")){
            this.titulares = new ArrayList();
        }
        //Se atopamos a etiqueta title, creamos o obxecto auxiliar de Titular onde gardaremos todolos datos
        else if(localName.equalsIgnoreCase("item")){
            this.tituloAux = new Titular();
            nivel = 1;
        }
    }

    /*
    Este método executase cando se finaliza de ler unha etiqueta
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //Finalizamos de ler a etiqueta title, polo que podemos gardar o texto que hai entre as etiquetas
        if(localName.equalsIgnoreCase("title")){
            if(nivel == 1){
                this.tituloAux.setTitular(cadeaTexto);
                nivel = 0;
            }                        
        }        
        //Finalizamos de ler a etiqueta item, polo que podemos gardar o obxecto ausiliar de Titular no ArrayList
        else if(localName.equalsIgnoreCase("item")){
            this.titulares.add(this.tituloAux);
        }
    }

    /*
    Este metodo executase cando se lee unha cadea de texto
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //Gardamos todo o texto entre caracteres na cadea de texto auxiliar
        this.cadeaTexto = new String(ch,start,length);
    }

    public ArrayList<Titular> getTitulares() {
        return titulares;
    }
}
