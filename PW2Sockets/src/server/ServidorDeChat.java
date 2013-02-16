package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class ServidorDeChat extends Thread {
	 public static void main(String args[]) {
	  // instancia o vetor de clientes conectados
	  clientes = new Vector();
	  try {
	   // criando um socket que fica escutando a porta 2222.
	   ServerSocket s = new ServerSocket(2222);
	   // Loop principal.
	while (true) {
	    // aguarda algum cliente se conectar. A execu��o do
	    // servidor fica bloqueada na chamada do m�todo accept da
	    // classe ServerSocket. Quando algum cliente se conectar
	    // ao servidor, o m�todo desbloqueia e retorna com um
	    // objeto da classe Socket, que � porta da comunica��o.
	    System.out.print("Esperando alguem se conectar...");
	    Socket conexao = s.accept();
	    System.out.println(" Conectou!");
	// cria uma nova thread para tratar essa conex�o
	Thread t = new ServidorDeChat(conexao);
	t.start();
	    // voltando ao loop, esperando mais algu�m se conectar.
	   }
	  }
	  catch (IOException e) {
	   // caso ocorra alguma excess�o de E/S, mostre qual foi.
	   System.out.println("IOException: " + e);
	  }
	 }
	 // Parte que controla as conex�es por meio de  threads.
	 // Note que a instancia��o est� no main.
	private static Vector clientes;
	 // socket deste cliente
	 private Socket conexao;
	// nome deste cliente
	 private String meuNome;
	 // construtor que recebe o socket deste cliente
	public ServidorDeChat(Socket s) {
	  conexao = s;
	 }
	 // execu��o da thread
	 public void run() {
	  try {
	   // objetos que permitem controlar fluxo de comunica��o
	   BufferedReader entrada = new BufferedReader(new
	        InputStreamReader(conexao.getInputStream()));
	   PrintStream saida = new 
	        PrintStream(conexao.getOutputStream());
	   // primeiramente, espera-se pelo nome do cliente
	   meuNome = entrada.readLine();
	   // agora, verifica se string recebida � valida, pois
	   // sem a conex�o foi interrompida, a string � null.
	   // Se isso ocorrer, deve-se terminar a execu��o.
	   if (meuNome == null) {return;}
	   // Uma vez que se tem um cliente conectado e conhecido,
	   // coloca-se fluxo de sa�da para esse cliente no vetor de
	   // clientes conectados.
	   clientes.add(saida);
	   // clientes � objeto compartilhado por v�rias threads!
	   // De acordo com o manual da API, os m�todos s�o
	   // sincronizados. Portanto, n�o h� problemas de acessos
	   // simult�neos.
	  
	   // Loop principal: esperando por alguma string do cliente.
	   // Quando recebe, envia a todos os conectados at� que o
	   // cliente envie linha em branco.
	   // Verificar se linha � null (conex�o interrompida)
	   // Se n�o for nula, pode-se compar�-la com m�todos string
	   String linha = entrada.readLine();
	   while (linha != null && !(linha.trim().equals(""))) {
	    // reenvia a linha para todos os clientes conectados
	    sendToAll(saida, " disse: ", linha);
	 // espera por uma nova linha.
	    linha = entrada.readLine();
	   }
	   // Uma vez que o cliente enviou linha em branco, retira-se
	   // fluxo de sa�da do vetor de clientes e fecha-se conex�o.
	   sendToAll(saida, " saiu ", "do chat!");
	   clientes.remove(saida);
	   conexao.close();
	  }
	  catch (IOException e) {
	   // Caso ocorra alguma excess�o de E/S, mostre qual foi.
	   System.out.println("IOException: " + e);
	  }
	 }
	 // enviar uma mensagem para todos, menos para o pr�prio
	public void sendToAll(PrintStream saida, String acao,
	  String linha) throws IOException {
		 System.out.print(linha);
	  Enumeration e = clientes.elements();
	  while (e.hasMoreElements()) {
	   // obt�m o fluxo de sa�da de um dos clientes
	   PrintStream chat = (PrintStream) e.nextElement();
	// envia para todos, menos para o pr�prio usu�rio
	if (chat != saida) {chat.println(meuNome + acao + linha);}
	  }
	 }
	}
