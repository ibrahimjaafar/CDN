import java.awt.GridLayout;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Local DNS 
 * 2 UDP ports 
 * @author Group 17
 *
 */
public class Local {


	public static void main(String args[])
    {
		final int UDP_PORT1 = 40160;
		final int UDP_PORT2 = 40164;
    try
    {
    	JTextArea log;
		JFrame frame = new JFrame("Local DNS");
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
    	
    	DatagramSocket server=new DatagramSocket(UDP_PORT1);
    	DatagramSocket server2=new DatagramSocket(UDP_PORT2);
    	while(true)
                {
                            byte[] sendbyte=new byte[1024];
                            byte[] receivebyte=new byte[1024];
                            DatagramPacket receiver=new DatagramPacket(receivebyte,receivebyte.length);//Receive request from Client
                            server.receive(receiver);
                            String str=new String(receiver.getData()); 
                            String response=str.trim();
                           
                            InetAddress clientAddress=receiver.getAddress();
                            int port=receiver.getPort();
                            /*
                             * DNS records Table
                             */
                            String name[]={"herCDN.com",	"NSherCDN.com",	"hisCinema.com",	"NShisCinema.com"};
                            String value[]={"NSherCDN.com", "127.0.0.1",		"NShisCinema.com",	"192.168.1.117"};
                            String type[]={"NS",			"A",			"NS",				"A"};
                            log.append("\n" + "Client selects the content URL and contacts local dummy DNS - ACCEPTED \n Source Ip: "+clientAddress.toString()+" Destination IP: "+"Local ip"+" \n Source port: "+receiver.getPort()+"Destination Port: "+UDP_PORT1+"\n");
                            InetAddress hisaddress = InetAddress.getByName(value[3]);
                            InetAddress heraddress = InetAddress.getByName(value[1]);
                            //Ask hisdns for hercdn url 
                            sendbyte=response.getBytes();
                            DatagramPacket sender=new DatagramPacket(sendbyte,sendbyte.length,hisaddress,40161);
                            server2.send(sender);
                            sendbyte=new byte[1024];
                            receivebyte=new byte[1024];
                            receiver=new DatagramPacket(receivebyte,receivebyte.length);
                            server2.receive(receiver);
                            str=new String(receiver.getData());
                            response=str.trim();
                            System.out.println(response);
                            log.append("\n" + "DNS server for hiscinea.com sends reply - ACCEPTED\n Source Ip: "+value[3]+" Destination IP: "+"local ip"+" \n Source port: "+receiver.getPort()+"Destination Port: "+UDP_PORT2+"\n");
                          	  
                            //Ask herdns for the ip address of hercdn.com
                        	sendbyte=response.getBytes();
                            sender=new DatagramPacket(sendbyte,sendbyte.length,heraddress,40162);
                            server2.send(sender);
                            sendbyte=new byte[1024];
                            receivebyte=new byte[1024];
                            receiver=new DatagramPacket(receivebyte,receivebyte.length);
                            server2.receive(receiver);
                            str=new String(receiver.getData());
                            response=str.trim();
                          
                            log.append("\n" + "Dummy DNS for herCDN.com replies to local dummy DNS - ACCEPTED\n Source Ip: "+value[3]+" Destination IP: "+value[1]+" \n Source port: "+receiver.getPort()+"Destination Port: "+UDP_PORT2);   
                            //Send the ip address of hercdn.com to the client     
                            sendbyte=response.getBytes();
                            sender=new DatagramPacket(sendbyte,sendbyte.length,clientAddress,port);
                            server.send(sender);
                            break;
                             
                
               }
    	server.close();//Close sockets
    	server2.close();
    }
    catch(Exception e)
    {
                System.out.println(e);
    }
    }

	
}