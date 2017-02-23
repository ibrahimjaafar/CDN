

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * HisCinemaServer
 * 1 TCP port
 * @author Group17
 *
 */


public class HisCinemaServer   {
	public static final int PORT = 40163;

	  JTextArea log;
	  ServerSocket serverSock;
public HisCinemaServer(int port){

	JFrame frame = new JFrame("Hiscinema Server");
	log = new JTextArea("");
	log.setEditable(false);
	JPanel panel = new JPanel();
	JScrollPane jsp = new JScrollPane(log);
	panel.setLayout(new GridLayout());
	panel.add(jsp);
	
	frame.add(panel);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(400,900);
	frame.setVisible(true);
	try {
		serverSock = new ServerSocket(port);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	new Thread(new server()).start();
}
class server implements Runnable {
	
	public void run() {
		
      
 
		while (true) {	
			try {
			        Socket sock = serverSock.accept(); //Accept client request for file url
				    InputStream sis = sock.getInputStream();
		            BufferedReader br = new BufferedReader(new InputStreamReader(sis));
		            String request = br.readLine(); // GET request received (index.html)
		            String fileNumber = br.readLine(); //Second part of GET request (file number)
		            int c = 0;
		            String[] splitReq = request.split(" ");
		            String path = splitReq[1];
		            int fileNum = Integer.parseInt(fileNumber);
		            DataOutputStream out =
		        			new DataOutputStream(sock.getOutputStream());
		            File file = new File(path);
		            if (!file.exists()) { //Check if index.html exists
		            	 log.append("\n ERROR 404 FILE NOT FOUND");
		            	 }
		            FileReader fr = new FileReader(file);
		            BufferedReader bfr = new BufferedReader(fr);
		            String line;
		            while ((line = bfr.readLine()) != null) {//Read file to output, received in inputstream by Client
		              c++;
		            	if (fileNum == c)
		            	out.writeBytes(line);
		                
		            }
		            log.append("\n"+ "Client contacts dummy www.hiscinema.com"+ sock.getInetAddress()+ " Destination IP: "+serverSock.getInetAddress()+"\n Source port: "+sock.getPort()+" Destination Port: "+"40163");

		            bfr.close();//Close sockets
		            br.close();
		            out.close();
			} catch (IOException e) {
				log.append("\n" + "Failed to accept client request");
			}
		}
	}
}

    public static void main(String args[] )  throws IOException {
    	HisCinemaServer servr = new HisCinemaServer(PORT);	
    }

	

}