package cliente;




// ClienteDeChat.java
import java.io.*;
import java.net.*;
public class ClienteDeChat extends Thread {
// Flag que indica quando se deve terminar a execução.
private static boolean done = false;
 public static void main(String args[]) {
  try {
   // Para se conectar a algum servidor, basta se criar um
	// objeto da classe Socket. O primeiro parâmetro é o IP ou
	   // o endereço da máquina a qual se quer conectar e o
	   // segundo parâmetro é a porta da aplicação. Neste caso,
	   // utiliza-se o IP da máquina local (127.0.0.1) e a porta
	   // da aplicação ServidorDeChat. Nada impede a mudança
	   // desses valores, tentando estabelecer uma conexão com
	   // outras portas em outras máquinas.
	   Socket conexao = new Socket("10.10.0.191", 2222);
	   
	   // uma vez estabelecida a comunicação, deve-se obter os
	   // objetos que permitem controlar o fluxo de comunicação
	PrintStream saida = new
	           PrintStream(conexao.getOutputStream());
	// enviar antes de tudo o nome do usuário
	BufferedReader teclado =
	      new BufferedReader(new InputStreamReader(System.in));
	System.out.print("Entre com o seu nome: ");
	String meuNome = teclado.readLine();
	   saida.println(meuNome);
	// Uma vez que tudo está pronto, antes de iniciar o loop 
	   // principal, executar a thread de recepção de mensagens.
	Thread t = new ClienteDeChat(conexao);
	t.start();
	   // loop principal: obtendo uma linha digitada no teclado e
	   // enviando-a para o servidor.
	   String linha;
	   while (true) {
	    // ler a linha digitada no teclado
	    System.out.print("> ");
	    linha = teclado.readLine();
	// antes de enviar, verifica se a conexão não foi fechada
	    if (done) {break;}
	    // envia para o servidor
	saida.println(linha);
	   }
	  }
	  catch (IOException e) {
	   // Caso ocorra alguma excessão de E/S, mostre qual foi.
	System.out.println("IOException: " + e);
	  }
	 }
	 // parte que controla a recepção de mensagens deste cliente
	 private Socket conexao;
	 // construtor que recebe o socket deste cliente
	 public ClienteDeChat(Socket s) {
		  conexao = s;
		 }
		 // execução da thread
		 public void run() {
		  try {
		   BufferedReader entrada = new BufferedReader
		     (new InputStreamReader(conexao.getInputStream()));
		   String linha;
		   while (true) {
		    // pega o que o servidor enviou
		    linha = entrada.readLine();
		    // verifica se é uma linha válida. Pode ser que a conexão
		    // foi interrompida. Neste caso, a linha é null. Se isso
		    // ocorrer, termina-se a execução saindo com break
		    if (linha == null) {
		     System.out.println("Conexão encerrada!");
		     break;
		    }
		    // caso a linha não seja nula, deve-se imprimi-la
		    System.out.println();
		    System.out.println(linha);
		System.out.print("...> ");
		   }
		  }
		  catch (IOException e) {
		   // caso ocorra alguma exceção de E/S, mostre qual foi.
		   System.out.println("IOException: " + e);
		  }
		  // sinaliza para o main que a conexão encerrou.
		  done = true;
		 }
		}