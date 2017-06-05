package com.mycompany.chatlab;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public final class SerwerKonf extends Thread {

    BlockingQueue<String> kolejka = new ArrayBlockingQueue<>(1);
    LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
    OdczytZPliku odczyt = new OdczytZPliku();

    ArrayList<String> pytania;
    ArrayList<String> odpowiedzi;
    int iloscpytan;

    ArrayList strumienieWyjsciowe;
    String imie;
    String odpowiedz;
 

    @Override
    public void run() {
        System.out.println("serwer uruchomiony");
        OdczytZHM();
        doRoboty();
        
        SerwerFrame fs = new SerwerFrame();
        fs.run();
        fs.TextArea.append("dsdsdsdsdsd");
    }

    private void OdczytZHM() {

//        Random random=new Random();        
//        int liczba= random.nextInt(3-1)+1;
        hm = odczyt.Odczyt();
        pytania = new ArrayList<String>(hm.keySet());
        odpowiedzi = new ArrayList<String>(hm.values());
        iloscpytan = pytania.size();

//        System.out.println(pytania);
//        System.out.println(iloscpytan);        
//        System.out.println("---------------------------------------------------------------------" + pytania.get(1));
        Producent producent = new Producent(kolejka);
        Konsument konsument = new Konsument(kolejka);

        new Thread(producent).start();
        new Thread(konsument).start();
    }

    public void doRoboty() {
        strumienieWyjsciowe = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(5000);
            while (true) {
                Socket gniazdoKlienta = serverSock.accept();
                PrintWriter pisarz = new PrintWriter(gniazdoKlienta.getOutputStream());
                strumienieWyjsciowe.add(pisarz);
                Thread t = new Thread(new ObslugaKlientow(gniazdoKlienta));
                t.start();
                System.out.println("mamy polaczenie");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Błąd serwera", "Błąd", 0);
        }

    }

//    public void rozeslijDoWszystkich(String message) {
//        Iterator it = strumienieWyjsciowe.iterator();
//        while (it.hasNext()) {
//            try {
//                PrintWriter pisarz = (PrintWriter) it.next();
//                pisarz.println(message);
//                pisarz.flush();
//            } catch (Exception ex) {
//            }
//        }
//    }
    public class Konsument implements Runnable {

        private BlockingQueue<String> kolejka;
        private String pytanie;

        Konsument(BlockingQueue<String> kolejka) {
            this.kolejka = kolejka;
        }

        @Override
        public void run() {
            try {
                pytanie = kolejka.take();
                System.out.println("---------------- Konsument odebrał: " + pytanie);
                Thread.sleep(1500);

                while (true) {
                    String odpprawidlowa=hm.get(pytanie);
                    System.out.println("============================================================"+odpprawidlowa);
                    System.out.println("---------------------------------------------------"+odpowiedz);
                    if (odpprawidlowa.equals(odpowiedz)) {
                        pytanie = kolejka.take();
                        System.out.println("---------------- Konsument odebrał: " + pytanie);
                        
                    }
                    Thread.sleep(1500);

                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SerwerKonf.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

//        {
//
//            try {
//                while (!(pytanie = kolejka.take()).equals("koniec")) {                 
//                    System.out.println("---------------- Konsument odebrał: " + pytanie);
//                    Thread.sleep(1500);
//                }
//                
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SerwerKonf.class.getName()).log(Level.SEVERE, null, ex);
//            }
//   
//        }
    }

    public class Producent implements Runnable {

        private BlockingQueue<String> kolejka;

        public Producent(BlockingQueue<String> kolejka) {
            this.kolejka = kolejka;
        }

        @Override
        public void run() {

            for (int i = 0; i <= iloscpytan - 1; i++) {
                try {

                    Thread.sleep(100);
                    kolejka.put(pytania.get(i));
                    System.out.println("+++++++++++++++++++++++++Producent wrzucił: " + i + "   " + pytania.get(i));
                } catch (InterruptedException ex) {
                    Logger.getLogger(SerwerKonf.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                kolejka.put("koniec");
            } catch (InterruptedException ex) {
                Logger.getLogger(SerwerKonf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//     public void rozeslijDoWszystkich(String message) {
//        Iterator it = strumienieWyjsciowe.iterator();
//        while (it.hasNext()) {
//            try {
//                PrintWriter pisarz = (PrintWriter) it.next();
//                pisarz.println(message);
//                pisarz.flush();
//            } catch (Exception ex) {
//            }
//        }
//    }

    public class ObslugaKlientow implements Runnable {

        BufferedReader czytelnik;
        Socket gniazdo;

        public ObslugaKlientow(Socket clientSocket) {
            try {
                gniazdo = clientSocket;
                InputStreamReader isReader = new InputStreamReader(gniazdo.getInputStream());
                czytelnik = new BufferedReader(isReader);
            } catch (Exception ex) {
            }
        }

        @Override
        public void run() {
            String wiadomosc;
            try {
                while ((wiadomosc = czytelnik.readLine()) != null) {
                    System.out.println("Odczytano: " + wiadomosc);

                    StringTokenizer token = new StringTokenizer(wiadomosc, ";");

                    imie = token.nextToken();
                    odpowiedz = token.nextToken();

                    //rozeslijDoWszystkich(wiadomosc);
                    System.out.println("imie: " + imie + " odp:" + odpowiedz);
                }
            } catch (Exception ex) {
            }
        }
    }
}
//-------------------------------------------------------------------------------------------------------------
/*

    ArrayList strumienieWyjsciowe;
    BufferedReader czytelnik;
    Socket gniazdo;

    public SerwerKonf(Socket clientSocket) {
        try {
            gniazdo = clientSocket;
            InputStreamReader isReader = new InputStreamReader(gniazdo.getInputStream());
            czytelnik = new BufferedReader(isReader);
        } catch (Exception ex) {
        }
    }

 
    public void uruchom() {
        String wiadomosc;
        try {
            while ((wiadomosc = czytelnik.readLine()) != null) {
                System.out.println("Odczytano: " + wiadomosc);
                //rozeslijDoWszystkich(wiadomosc);
            }
        } catch (Exception ex) {
        }
    }


public void doRoboty() {
        strumienieWyjsciowe = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(5000);
            while (true) {
                Socket gniazdoKlienta = serverSock.accept();
                PrintWriter pisarz = new PrintWriter(gniazdoKlienta.getOutputStream());
                strumienieWyjsciowe.add(pisarz);
                Thread t = new Thread((Runnable) new SerwerKonf(gniazdoKlienta));   //???????????????
                t.start();
                System.out.println("mamy polaczenie");
            }
        } catch (Exception ex) {
        }
        
        
    } 

    public void rozeslijDoWszystkich(String message) {
        Iterator it = strumienieWyjsciowe.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter pisarz = (PrintWriter) it.next();
                pisarz.println(message);
                pisarz.flush();
            } catch (Exception ex) {
            }
        } 
    } 
}
 */
