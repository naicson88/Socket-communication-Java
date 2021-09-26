
import java.net.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientProfessor {

   public static void main(String [] args) throws IOException {
    
	   Socket socket = new Socket("127.0.0.1", 4433);
	   Scanner sc = new Scanner(System.in);
		
		try {

			DataInputStream input = new DataInputStream(socket.getInputStream());		
	        DataOutputStream output = new DataOutputStream(socket.getOutputStream()); 
	        //Cliente que chamou o Servidor
	        output.writeUTF("Professor");
	        
	        System.out.println(" Olá, professor. Inicie a chamada digitando o numero da turma: ");
	        String numeroTurma = sc.next();
	        //1) Numero da Turma
	        output.writeUTF(numeroTurma);
	        
	        System.out.println(Util.data() + " >>> A chamada da turma " + numeroTurma + " foi iniciada... \n\n");
	        
	        System.out.println(" Para interromper o chamado, digite a tecla 0 (zero) \n\n ");
	        String finalizou = sc.next();
	        
	        while(!"0".equals(finalizou)) {
	        	System.out.println(" Para interromper o chamado, digite a tecla 0 (zero) \n\n");
	        	finalizou = sc.next();
	        }
	        
	        //2) chamada foi finalizada;
	        output.writeUTF(finalizou);
	        
	        System.out.println(Util.data() + " ##### Chamada encerrada, a aplicacao sera finalizada  ######");
     
		}catch(Exception e) {
			e.getMessage();
		} finally {
			socket.close();
			sc.close();
		}
   }
}