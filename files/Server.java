

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread {

	private static boolean isChamadaAberta = false;
	private static String turma;
	private static  Map<String, String> chamadasAbertas;
	private static ArrayList<String> alunosPresentes;

	private Socket server = null;
	private DataInputStream input;

	public Server(Socket socket) throws IOException {
		this.server = socket;
		input = new DataInputStream(socket.getInputStream());
	}
	
	public void run() {
			
		try {
			
			DataInputStream input = new DataInputStream(server.getInputStream());
			DataOutputStream output = new DataOutputStream(server.getOutputStream());

			String cliente = input.readUTF();

			if ("Professor".equalsIgnoreCase(cliente)) {
				System.out.println(Util.data() + " >>> O Cliente " + cliente + " foi conectado ao servidor. \n");
				processaClienteProfessor(server, input, output);
			}

			else if ("Aluno".equalsIgnoreCase(cliente)) {
				System.out.println(Util.data() + " >>> O Cliente " + cliente + " foi conectado ao servidor. \n");
				processaClienteAluno(server, input, output);
			}

		} catch (IOException e) {
			e.getMessage();
		}		

}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		System.out.println(" ### O servidor foi iniciado na porta: 4433 Data: " + Util.data() + " ### \n");

		System.out.println("<----- Os clientes que se comunicarao a esse servidor podem ser iniciados ----> \n");
		
		alunosPresentes = new ArrayList<>();
		chamadasAbertas = new HashMap<>();
		
		while(true) {
			
			try {
				
				ServerSocket serverSocket = new ServerSocket(4433);
				serverSocket.setSoTimeout(1000000);
				Socket socket = serverSocket.accept();
				Server server = new Server(socket);
				server.start();
				
				Thread.sleep(2000);
				serverSocket.close();
							
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private  void processaClienteAluno(Socket server, DataInputStream input, DataOutputStream output) {
		
		try {
			// 1) Matricula aluno
			String matricula = input.readUTF();
			// 2) Numero Turma
			String numTurma = input.readUTF();

			if (!chamadasAbertas.containsKey(numTurma)) {
				
				System.out.println(Util.data() + " Nao ha chamada aberta para a turma informada. ");

				output.writeUTF(Util.data() + " Nao ha chamada aberta para a turma informada. ");
				return;
			}
			
			if(alunosPresentes.contains(matricula)) {

				System.out.println(Util.data() + " Matricula ja registrada.");

				output.writeUTF(Util.data() + " Matricula ja registrada.");
				return;
			}
				
			
			String concat = chamadasAbertas.get(numTurma);
			concat +=  matricula +", ";
			
			chamadasAbertas.replace(numTurma, concat);
			
			System.out.println(Util.data() + " Presenca foi registrada com sucesso! ");

			output.writeUTF("1");
			
			alunosPresentes.add(matricula);
			
		} catch (IOException e) {
			e.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}

	}

	private  void processaClienteProfessor(Socket server, DataInputStream input, DataOutputStream output)
			throws IOException {
		
		String turma = new String();
		
		// 1) Numero da Turma
		turma = input.readUTF();

		if (turma != null && !turma.equals("")) {
			
			chamadasAbertas.put(turma, "");
			
			System.out.println(Util.data() + " >>> A chamada da turma " + turma + " foi aberta pelo professor. \n");
			
			if(!chamadasAbertas.containsKey(turma)) {
				chamadasAbertas.put(turma, "1");
			}

			// 2) chamada finalizada pelo professor;
			String finalizou = input.readUTF();
			
		
			String stringMatriculas = chamadasAbertas.get(turma);
			String str = "";
			
			if(stringMatriculas.length() > 3)
				 str = stringMatriculas.substring(0, stringMatriculas.length() - 2);
			
			//3) Envia lista da matricula dos alunos que responderam a chamada
			output.writeUTF(str);

			if (finalizou.equals("0")) {
				chamadasAbertas.remove(turma);
				System.out.println(Util.data() + " >>> A chamada da turma " + turma + " foi encerrado. \n");
			}
			
		} else {

			System.out.println(Util.data() + " >>> Numero da turma inválida.");
		}
	

	}

}
