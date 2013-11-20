import java.util.*;
import java.net.*;
import java.io.*;

public class LineServerProtocol{
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
