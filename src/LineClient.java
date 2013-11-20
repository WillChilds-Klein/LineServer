import java.util.*;
import java.net.*;
import java.io.*;

public class LineClient{

	static int port;
	String host;
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;
	BufferedReader stdin;

	public LineClient(){
		port = 10497;
		host = null;
		clientSocket = null;
		out = null;
		in = null;
		stdin = new BufferedReader(new InputStreamReader(System.in));
	}

	public void init() throws IOException{
		System.out.println("Please enter the IP address or hostname of the"
					+ " desired server.");
		String host = stdin.readLine();

		try{
			clientSocket = new Socket(host, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
										clientSocket.getInputStream()));
		}
		catch(UnknownHostException e){
			System.err.println("Unknown host: " + host);
			System.exit(1);
		}
		catch(IOException e){
			System.err.println("Unable to obtain I/O for connection" + 
								"with " + host);
			System.exit(1);
		}
		System.out.println("Success! Connection established.");
		return;
	}

	public void process() throws IOException{
		String userInput = null;
		String temp = null;

		while((userInput = stdin.readLine()) != null){
			out.println(userInput);
			if(userInput.equals("SHUTDOWN") || userInput.equals("QUIT"))
				break;
			temp = in.readLine();
			if(!temp.equals("ERR"))
				System.out.println("OK");
			System.out.println(temp);
		}
		return;
	}

	public void shutdown() throws IOException{
		in.close();
		out.close();
		stdin.close();
		clientSocket.close();
	}

	public static void main(String[] args) throws IOException{
		LineClient lc = new LineClient();
		
		lc.init();
		lc.process();
		lc.shutdown();
	}
}