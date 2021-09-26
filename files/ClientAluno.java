

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientAluno {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		 Socket socket = new Socket("127.0.0.1", 4433);
		  Scanner sc = new Scanner(System.in);
		  
			try {

				DataInputStream input = new DataInputStream(socket.getInputStream());		
		        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		        
		        //Cliente que chamou o Servidor
		        output.writeUTF("Aluno");
		        
		        System.out.println(" Prezado aluno, informe sua matricula: ");
		        String matricula = sc.next();
		        System.out.println(" Agora informe o numero da sua turma: ");
		        String numTurma = sc.next();
		        
		        //1) Matricula Aluno
		        output.writeUTF(matricula);
		        //2) Numero turma
		        output.writeUTF(numTurma);
		        
		        //3)Verifica se a presença foi registrada
		        String codResposta =  input.readUTF();
		        
		        if("1".equals(codResposta))
		        	System.out.println(Util.data() + "Turma: " + numTurma + " - Sua presenca foi registrada com sucesso! Turma: " + numTurma);
		        else
		        	System.out.println(Util.data() + " Nao ha chamada aberta para esta turma!");
		        
	     
			}catch(Exception e) {
				e.getMessage();
			} finally {
				socket.close();
				sc.close();
			}

	}

}
