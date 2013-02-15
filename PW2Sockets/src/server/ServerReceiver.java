package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerReceiver extends Thread {  
    
    private Socket conexao;  
      
    public ServerReceiver(Socket conexao ){  
        this.conexao = conexao;  
    }  
      
    public static void main(String a[]) {  
        try {  
              
            // criando um socket que fica escutando a porta 2222.  
            ServerSocket s = new ServerSocket(2222);  
            while (true) {  
                  
                System.out.print("Esperando alguem se conectar...");  
                Socket conexao = s.accept();  
                System.out.println(" Conectou!");  
                  
                // cria uma nova thread para tratar essa conexão  
                Thread t = new ServerReceiver(conexao);  
                t.start();  
                // voltando ao loop, esperando mais alguém se conectar.  
            }  
              
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
          
    }  
      
    public void run(){  
          
        ServerSocket serskt=null;  
        PrintWriter writeskt=null;  
          
        int rep;  
        try   {  
            FileOutputStream fout = new FileOutputStream("C:\\okscm\\destino_remoto\\asin.txt");  
             
            try {  
                System.out.println("Listining...");  
                writeskt=new PrintWriter(conexao.getOutputStream(),true);  
                BufferedReader readskt = new BufferedReader(new InputStreamReader(conexao.getInputStream()));  
                System.out.println("File Recieving...");  
                try   {  
                      
                    while(((rep = Integer.parseInt(readskt.readLine())) != -1)) { // a Execução para aqui depois da leitura do ultimo byte  
                        System.out.print(rep+" - ");  
                        fout.write(rep);  
                        System.out.println(" >>1<< ");  
                        writeskt.println(rep);  
                        System.out.println(" >>2<< "); //break  ;  
                    }  
                    System.out.println("Depois>>>");  
                    fout.close();  
                    //      fout.write(readskt.read());  
                } catch(IOException e){}  
                writeskt.close();  
                readskt.close();  
            }   catch(Exception e) {   System.out.println(e); e.printStackTrace();        }  
        }catch(FileNotFoundException e){}  
          
    }
}

