import java.awt.GridLayout;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * Hercinema DNS 
 * 1 UDP Port
 * @author Group17
 */
public class HerDns
{
            public static void main(String args[])
            {
            try
            {
                       
            	JTextArea log;
        		JFrame frame = new JFrame("Hercinema DNS");
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
            	DatagramSocket server=new DatagramSocket(40162);
            
            
              
            	while(true)
                        {
            						byte[] sendbyte=new byte[1024];
                                    byte[] receivebyte=new byte[1024];
                                    DatagramPacket receiver=new DatagramPacket(receivebyte,receivebyte.length);
                                    server.receive(receiver);//UDP connection setup
                                    String str=new String(receiver.getData());
                                    String address=str.trim();
                                
                                    InetAddress clientAddress=receiver.getAddress();
                                    int port=receiver.getPort();
                                    /*
                                     * DNS records Table
                                     */
                                    String name[]={"herCDN.com/F1","www.herCDN.com/F1","herCDN.com/F2","www.herCDN.com/F2","herCDN.com/F3","www.herCDN.com/F3","herCDN.com/F4","www.herCDN.com/F4","herCDN.com/F5","www.herCDN.com/F5"};
                                    String value[]={"www.herCDN.com/F1","192.168.1.104","www.herCDN.com/F2","192.168.1.104","www.herCDN.com/F3","192.168.1.104","www.herCDN.com/F4","192.168.1.104","www.herCDN.com/F5","IPf5"};
                                    String type[]={"CN","A","CN","A","CN","A","CN","A","CN","A"};
                                    log.append("\n"+ "Local dummy DNS sends query to dummy DNS herCDN.com - ACCEPTED"+"\n Source Ip: "+ receiver.getAddress().toString()+ " Destination IP: "+value[1]+"\n Source port: "+receiver.getPort()+" Destination Port: "+"40162");

                           for(int i=0;i<value.length;i++)
                              {
                                          if(address.equals(name[i])) //if localdns asks for herCDN, send the ip for the video
                                       {
                                    
                                    						sendbyte=value[i+1].getBytes(); //ip for www.herCDN.com
                                                            DatagramPacket sender=new DatagramPacket(sendbyte,sendbyte.length,clientAddress,port);
                                                            server.send(sender); //Send to local dns
                                                            server.close();
                                                            break;
                                  
                        }
                               }
                            break;
                        }
  	
            }
            catch(Exception e)
            {
                        System.out.println(e);
            }
            }
}