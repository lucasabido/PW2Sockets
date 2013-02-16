package cliente;




// ClienteDeChat.java
import java.io.*;
import java.net.*;
public class ClienteDeChat extends Thread {
// Flag que indica quando se deve terminar a execu��o.
private static boolean done = false;
 public static void main(String args[]) {
  try {
   // Para se conectar a algum servidor, basta se criar um
	// objeto da classe Socket. O primeiro par�metro � o IP ou
	   // o endere�o da m�quina a qual se quer conectar e o
	   // segundo par�metro � a porta da aplica��o. Neste caso,
	   // utiliza-se o IP da m�quina local (127.0.0.1) e a porta
	   // da aplica��o ServidorDeChat. Nada impede a mudan�a
	   // desses valores, tentando estabelecer uma conex�o com
	   // outras portas em outras m�quinas.
	   Socket conexao = new Socket("10.10.0.191", 2222);
	   
	   // uma vez estabelecida a comunica��o, deve-se obter os
	   // objetos que permitem controlar o fluxo de comunica��o
	PrintStream saida = new
	           PrintStream(conexao.getOutputStream());
	// enviar antes de tudo o nome do usu�rio
	BufferedReader teclado =
	      new BufferedReader(new InputStreamReader(System.in));
	System.out.print("Entre com o seu nome: ");
	String meuNome = teclado.readLine();
	   saida.println(meuNome);
	// Uma vez que tudo est� pronto, antes de iniciar o loop 
	   // principal, executar a thread de recep��o de mensagens.
	Thread t = new ClienteDeChat(conexao);
	t.start();
	   // loop principal: obtendo uma linha digitada no teclado e
	   // enviando-a para o servidor.
	   String linha;
	   while (true) {
	    // ler a linha digitada no teclado
	    System.out.print("> ");
	    linha = teclado.readLine();
	// antes de enviar, verifica se a conex�o n�o foi fechada
	    if (done) {break;}
	    // envia para o servidor
	saida.println(linha);
	   }
	  }
	  catch (IOException e) {
	   // Caso ocorra alguma excess�o de E/S, mostre qual foi.
	System.out.println("IOException: " + e);
	  }
	 }
	 // parte que controla a recep��o de mensagens deste cliente
	 private Socket conexao;
	 // construtor que recebe o socket deste cliente
	 public ClienteDeChat(Socket s) {
		  conexao = s;
		 }
		 // execu��o da thread
		 public void run() {
		  try {
		   BufferedReader entrada = new BufferedReader
		     (new InputStreamReader(conexao.getInputStream()));
		   String linha;
		   while (true) {
		    // pega o que o servidor enviou
		    linha = entrada.readLine();
		    // verifica se � uma linha v�lida. Pode ser que a conex�o
		    // foi interrompida. Neste caso, a linha � null. Se isso
		    // ocorrer, termina-se a execu��o saindo com break
		    if (linha == null) {
		     System.out.println("Conex�o encerrada!");
		     break;
		    }
		    // caso a linha n�o seja nula, deve-se imprimi-la
		    System.out.println();
		    System.out.println(linha);
		System.out.print("...> ");
		   }
		  }
		  catch (IOException e) {
		   // caso ocorra alguma exce��o de E/S, mostre qual foi.
		   System.out.println("IOException: " + e);
		  }
		  // sinaliza para o main que a conex�o encerrou.
		  done = true;
		 }
		}