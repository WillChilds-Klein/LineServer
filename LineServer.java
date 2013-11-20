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

	public String processInput(String str){
		if(str.length() < 4 || !str.substring(0,4).equals("GET ")){
			return "ERR";
		}

		int lineNumber = Integer.parseInt(str.substring(4,str.length()));
		if(lineNumber < 0)
			return "ERR";

		FileReader freader = null;
		try{
			freader = new FileReader("lines/" + lineNumber + ".tmp"); 
		}
		catch(IOException e){
			return "ERR";
		}
		BufferedReader br = new BufferedReader(freader);

		String ret = null;
		try{
			ret = br.readLine();
		}
		catch(IOException e){
			return "ERR";
		}
		return ret;
	}

	public void preProcessFile(String fileName) throws IOException{
		FileReader freader = null;
		try{
			freader = new FileReader(fileName);
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		BufferedReader br = new BufferedReader(freader);

		String currLine = null;
		FileWriter fwriter = null;
		int lineNumber = 1;

		File tempDir = new File("lines/");
		boolean b = tempDir.mkdir();
		if(!b){
			System.err.println("error making lines/ dir");
			System.exit(-1);
		}

		while((currLine = br.readLine()) != null){
			try{
				fwriter = new FileWriter("lines/" + lineNumber + ".tmp");
				fwriter.write(currLine + "\n");
				fwriter.close();
				lineNumber++;
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}