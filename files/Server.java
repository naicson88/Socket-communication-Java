
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {
	
	private static boolean isChamadaAberta = false;
	private static String numeroTurma;
	private static ArrayList<String> alunosPresentes;
	
	public static void main(String[] args) throws IOException {	
		
		ServerSocket serverSocket = new ServerSocket(4433);
		Scanner sc = new Scanner(System.in);
				
		try {

			serverSocket.setSoTimeout(1000000);
			
			Thread t = new Server();
			t.start();
			
			while(true) {
				
			System.out.println(" ### O servidor foi iniciado na porta: " + serverSocket.getLocalPort() + " Data: " + Util.data() + " ### \n");						
			System.out.println("<----- Os clientes que se comunicarao a esse servidor podem ser iniciados ----> \n");	
	
			Socket server = serverSocket.accept();
			
			DataInputStream input = new DataInputStream(server.getInputStream());
		    DataOutputStream output = new DataOutputStream(server.getOutputStream()); 
			
			String cliente = input.readUTF();
			
			System.out.println(Util.data() + " >>> O Cliente " + cliente + " foi conectado ao servidor. \n");
			
			if("Professor".equalsIgnoreCase(cliente))
				processaClienteProfessor(server, input, output);
			
			else if ("Aluno".equalsIgnoreCase(cliente))
				processaClienteAluno(server, input, output);			
			}
			
		} catch(IOException e) {
			e.getMessage();
		} finally {
			serverSocket.close();
		}
		
	}
	
	private static void processaClienteAluno(Socket server, DataInputStream input, DataOutputStream output) {
		try {
			//1) Matricula aluno
			String matricula = input.readUTF();
			//2) Numero Turma
			String numTurma = input.readUTF();
			
			if(isChamadaAberta == true && numTurma.equals(numeroTurma)) {
				
				alunosPresentes.add(matricula);
				
				System.out.println(Util.data() + " Presenca foi registrada com sucesso! ");
				
				output.writeUTF(Util.data() + " Sua presenca foi registrada com sucesso. ");
				
			} else {
				
				System.out.println(Util.data() + " Nao ha chamada aberta para a turma informada. ");
				
				output.writeUTF(Util.data() + " Nao ha chamada aberta para a turma informada. ");
				
			}
			
		} catch(IOException e) {
			e.getMessage();
		} catch(Exception e) {
			e.getMessage();
		}
		
		
	}

	private static void processaClienteProfessor(Socket server, DataInputStream input, DataOutputStream output ) throws IOException {
		  //1) Numero da Turma
		 numeroTurma = input.readUTF();
		 
		if(numeroTurma != null && !numeroTurma.equals(""))
			isChamadaAberta = true;		 
		System.out.println(Util.data() + " >>> A chamada da turma " + numeroTurma + " foi aberta pelo professor. \n");
		
		//2) chamada finalizada pelo professor;
		String finalizou = input.readUTF();
		
		if(finalizou.equals("0")) {
			isChamadaAberta = false;
			System.out.println(Util.data() + " >>> A chamada da turma " + numeroTurma + " foi encerrado. \n");
		}
			
	}
	
}
