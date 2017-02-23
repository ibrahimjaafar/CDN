import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * The application Client
 * 2 TCP port
 * 1 UDP port
 * 
 * @author Group 17
 *
 */
public class ApplicationClient {
private static JTextArea log;
public static String hiscinemaURL; 
final static int Hiscinema_PORT = 40163;
final static String Hiscinema_Addr="192.168.1.117";
private static String hercdnIP = "";
private final static int hercdnPort = 40167;
private final static String movieFile = "C:\\Users\\ibrahim\\Desktop\\movie113.txt";

	public static void main(String argv[]) throws Exception
	{
		final int CLIENT_PORT = 40169;
	    InetAddress addr=InetAddress.getByName("192.168.1.117");
		
		JFrame frame = new JFrame("Client");
		log = new JTextArea("Close this window to close the server.");
		log.setEditable(false);
		JPanel panel = new JPanel();
		JScrollPane jsp = new JScrollPane(log);
		panel.setLayout(new GridLayout());
		panel.add(jsp);
		
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,900);
		frame.setVisible(true);
	webserver();
    log.append("\n" +"www.hiscinema.com returns index file\n Source Ip: "+Hiscinema_Addr+" Destination IP: "+addr+" \n Source port: "+ Hiscinema_PORT+" Destination Port: "+CLIENT_PORT);
    try 
    {
                DatagramSocket client=new DatagramSocket(CLIENT_PORT);//Contact local dns
            

                byte[] sendbyte=new byte[1024];
                byte[] receivebyte=new byte[1024];
                BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
            
                String video_url=hiscinemaURL;//Set the url to the url received from hiscinema web server
                sendbyte=video_url.getBytes();
                DatagramPacket sender=new DatagramPacket(sendbyte,sendbyte.length,addr,40160); 
                client.send(sender);
                DatagramPacket receiver=new DatagramPacket(receivebyte,receivebyte.length);
                client.receive(receiver); //Send request and receive response (resolved url) from local dns
                String herIP=new String(receiver.getData());
                log.append("\n" + herIP.trim()+ "Local dummy DNS replies to the client with resolved IP address of content server\n Source Ip: "+ receiver.getAddress()+ " Destination IP: "+addr +"\n Source port: "+receiver.getPort()+" Destination Port: "+CLIENT_PORT);
                hercdnIP = herIP.trim(); //Set the hercdn IP that was resolved in local dns
                client.close();
    }
    catch(Exception e)
    {
                System.out.println(e);
    }
    
    herfile();
    log.append("\n" +"Content server replies with the content. Whole file should be downloaded before playing it"+"\n Source Ip: "+hercdnIP+" Destination IP: "+addr+" \n Source port: "+ hercdnPort+" Destination Port: "+CLIENT_PORT);

	} 
	/**
	 * Returns the URL for the file from the web server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void webserver() throws UnknownHostException, IOException{ //his cinema web server method
		String getRequest;
		Socket clientSocket = new Socket(Hiscinema_Addr, Hiscinema_PORT); 

		getRequest = "GET C:/Users/Ibrahim/workspace/CDN/src/index.txt HTTP/1.1\r\n2\r\n";//Get request which asks for index.html and file number
		DataOutputStream outToServer =
				new DataOutputStream(clientSocket.getOutputStream());

		
		outToServer.writeBytes(getRequest + '\n');//Send the request to Hiscinema web server
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		hiscinemaURL = inFromServer.readLine();// Read the returned url message from the web server

	//System.out.println(hiscinemaURL);
		clientSocket.close();//Close the sockets
		outToServer.close();
		inFromServer.close();
	}
	/**
	 * Returns the file from the web server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void herfile() throws UnknownHostException, IOException{//HerCDN web server method
			byte[] b = new byte[1];
	        int bytesRead;
	        String fn = hiscinemaURL.substring(20,22);
	        try {
	       ///
	        	Socket     cs = new Socket( hercdnIP , hercdnPort );
	        	DataOutputStream outToServer =
	    				new DataOutputStream(cs.getOutputStream());
	        	
	    		
	    		outToServer.writeBytes(fn+"");//Send the request to Hiscinema web server
	    	cs.close();
	    	outToServer.close();
	    		Socket    clientSocket= new Socket( hercdnIP , hercdnPort );
	    		InputStream      is = clientSocket.getInputStream();//Accept the socket request
	        	
	        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            FileOutputStream fos = null;
	            BufferedOutputStream bos = null;
	          
	                fos = new FileOutputStream(movieFile);//Create the file that will be read into
	                bos = new BufferedOutputStream(fos);
	                bytesRead = is.read(b, 0, b.length);//Read from the input stream

	                do {
	                        baos.write(b);
	                        bytesRead = is.read(b);
	                } while (bytesRead != -1);

	                bos.write(baos.toByteArray());
	                bos.flush();
	                bos.close();
	                clientSocket.close();
	              
	          	  is.close();//Close the sockets
	          	  baos.close();
	            } catch (IOException ex) {
	            	 log.append("\n "+ex);
	            
	        }
	
	}
} 

