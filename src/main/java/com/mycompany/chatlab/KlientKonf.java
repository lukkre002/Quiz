/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatlab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Olek
 */
public class KlientKonf {
    
     public KlientKonf() {
        konfigurujKomunikacje();       
    }
    
    BufferedReader czytelnik;
    PrintWriter pisarz;
    Socket gniazdo;

    private void konfigurujKomunikacje() {
        try {
            gniazdo = new Socket("127.0.0.1", 5000);
            InputStreamReader czytelnikStrm = new InputStreamReader(gniazdo.getInputStream());
            czytelnik = new BufferedReader(czytelnikStrm);
            pisarz = new PrintWriter(gniazdo.getOutputStream(), true);
            System.out.println("obsluga sieci przygotowana");
            //pisarz.println("czesc-CHATLAB");
            //pisarz.flush();
        } catch (IOException ex) {
            System.out.println("wlacz serwer");
        }

    } 
    
    public PrintWriter dajpisarza()
    {
        return pisarz;
    }
    
    public BufferedReader dajczytelnika()
    {
        return czytelnik;
    }
    

    
}
