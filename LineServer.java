import java.util.*;
import java.net.*;
import java.io.*;

public class LineServer{
	public static void main(String[] args) throws IOException{
		if(args.length != 1){
			System.err.println("Please specify ONE file to serve.");
			System.exit(-1);
		}

		ServerSocket serverSocket = null;
		int port = 10497;

		try{
			serverSocket = new ServerSocket(port);
		}
		catch(IOException e){
			System.err.println("Could not listen on port " + port);
			System.exit(-1);
		}

		Socket clientSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try{
			clientSocket = serverSocket.accept();
		}
		catch(IOException e){
			System.err.println("accept() failed: port " + port);
			System.exit(-1);
		}

		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(
									clientSocket.getInputStream()));

		String inputLine = null;
		LineServerProtocol lsp = new LineServerProtocol();

		String fileName = args[0];
		lsp.preProcessFile(fileName);

		while(true){
			try{
				inputLine = in.readLine();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			if(inputLine.equals("QUIT")){
				in.close();
				out.close();
				clientSocket.close();
				try{
					clientSocket = serverSocket.accept();
				}
				catch(IOException e){
					System.err.println("accept() failed: port " + port);
					System.exit(-1);
				}
				clientSocket.getOutputStream().flush();
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(
									clientSocket.getInputStream()));
				continue;
			}
			if(inputLine.equals("SHUTDOWN"))
				break;
			out.println(lsp.processInput(inputLine));
		}

		in.close();
		out.close();
		clientSocket.close();
		serverSocket.close();
	}
}