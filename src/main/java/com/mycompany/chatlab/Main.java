package com.mycompany.chatlab;



public class Main {

    public static void main(String[] args) throws InterruptedException {

        SerwerFrame serwerframe= new SerwerFrame();
                serwerframe.TextArea.append("Chuju");

        serwerframe.run();
        
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(() -> {
             
                KlientFrame klient = new KlientFrame();
                klient.run();            
            });
          
            Thread.sleep(500);
            thread.start();
        }

    }

}
//