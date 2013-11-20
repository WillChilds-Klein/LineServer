import java.util.*;
import java.net.*;
import java.io.*;

public class LineClient{
	public static void main(String[] args) throws IOException{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
													  System.in));
		System.out.println("Please enter the IP address or hostname of the"
							+ " desired server.");
		String host = stdin.readLine();
		int port = 10497;

		Socket clientSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

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

		in.close();
		out.close();
		stdin.close();
		clientSocket.close();
	}
}