package simori.Modes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import simori.ModeController;
import simori.Modes.ChangerMode.Changer;
import simori.Modes.ChangerMode.Setting;

/**
 * Class to act as the master in the master slave mode.
 * It contains functions to search through an ip range
 * and set up a socket to transfer the model over.
 * @version 1.4.0
 * @author Adam
 * @author Matt
 */
public class NetworkMaster implements Runnable {
	
	private int port;
	private String ip;
	private ModeController controller;
	private NetworkSlave slave;
	private final static String os = System.getProperty("os.name").toLowerCase();
	private boolean running;
	
	/**
	 * Constructor for the Network Master Class.
	 * @author Adam
	 * @param port   The port to search on.
	 * @param model  The model to export.
	 * @throws IOException 
	 */
	public NetworkMaster(int port, ModeController controller, NetworkSlave slave)
			throws IOException {
		this.port = port;
		this.controller = controller;
		this.slave = slave;
	}
	
	/**
	 * Find slave generates the ip ranges to search in for another simori on.
	 * @author adam
	 */
	public void findSlave(){
		if(!running){
			running = true;
			/* generate y range (xxx.xxx.xxx.yyy) */
			String cloesestRangeIP =  ip.substring(0, ip.indexOf('.',
					ip.indexOf('.',ip.indexOf('.')+1)+1) + 1);
			/* generate y range (xxx.xxx.yyy.xxx) */
			String thisRangeIP = cloesestRangeIP.substring
					(0, cloesestRangeIP.indexOf('.',cloesestRangeIP.indexOf('.')+1) + 1);
			/* First check the same end ip range */
			boolean found = closestRangeIP(cloesestRangeIP);
			if(!found){
				iterateOverIPRange(thisRangeIP);
			}
			slave.switchOn();
		}
		
	}
	
	
	/**
	 * Method to check ips in the last section of the address.
	 * @author Adam
	 * @param ip  The ip range to itterate over
	 * @return  boolean, true if an exception ip was found
	 */
	private boolean closestRangeIP(String ip){
		for(int i = 1; i < 256; i++){
			if(running){
		        try {
		        	System.out.println(ip + i);
		        	/* If it's not my ip */
		        	checkSocket(ip + i);
		        	return true;
		        } catch (IOException	 e){
		        	
		        }
			} else {
				break;
			}
	    }
		return false;
	}
	
	
	/**
	 * Method to attempt a socket connection on a given ip.
	 * @author Adam
	 * @author Matt
	 * @param ip  The IP to attempt a connection with.
	 * @throws IOException
	 */
	private void checkSocket(String ip) throws IOException{
		Socket socket = new Socket();
		
		/* attempt socket connection with 100ms timeout */
        socket.connect(new InetSocketAddress(ip, port), 200);
       
        OutputStream out = (OutputStream) socket.getOutputStream();
        
        ObjectOutputStream serializer = new ObjectOutputStream(out);
       
        /* Serialize and write the model to the output stream */
        serializer.writeObject(controller.getModel());
        serializer.close();
        out.close();
        socket.close();
	}
	
	/**
	 * Method to return the systems local IP address.
	 * @author Adam
	 * @return  A string containing the current systems local IP. 
	 * @throws UnknownHostException 
	 * @throws IOException 
	 */
	private String getIP() throws UnknownHostException, IOException {
		Socket s;
		String betterIP;
		try {
			/* Check if local address is on the 192.168.0 range */
			s = new Socket();
			s.connect(new InetSocketAddress("192.168.0.1", 80), 500);
			betterIP = s.getLocalAddress().getHostAddress();
			s.close();
		} catch (IOException e) {
			/* Else try trace route for ip */
			if((betterIP = routeIP()).equals("0.0.0.0")){
				/* Else set address to unreliable local address */
				betterIP  = Inet4Address.getLocalHost().getHostAddress();
			}
		}
		
		return betterIP;
	}
	
	/**
	 * Method to exec traceroute and pass what it returns
	 * to the ipv4 function.
	 * @author Adam
	 * @return The IP address of the users router.
	 * @throws IOException
	 */
	private String routeIP() throws IOException {
		
		/* choose command based on windows or unix */
		String trace = (os.contains("win") ? "tracert" : "traceroute");
		/* execute command */
		
		ProcessBuilder pb = new ProcessBuilder(trace, "www.google.com");
		Process p = pb.start();
		
        BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String content = output.readLine();
        String line;
        
        while((line = output.readLine()) != null){
        	content += line; //add each line content
        	
        }
        
        
        String gateway= ipv4(content);
        return gateway;
    }
	
	/**
	 * Method to find the 2nd ip address in a string.
	 * @author Adam.
	 * @param search  The string to search for an ip in.
	 * @return An ipv4 address.
	 */
	private static String ipv4(String search){
		/* Regex to match an IP Address */
		String IPADDRESS_PATTERN =  
				"(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
				"{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		
		/* Check input string against regex */
		Matcher matcher = pattern.matcher(search);
		if (matcher.find()) { //If one match
			if (matcher.find()) { //If two matches
				return matcher.group(); //return the 2nd IP.
			} 
		}
		return "0.0.0.0";
	}
	
	/**
	 * Method to itterate over an ip address.Z
	 * @author Adam
	 * @param ip  The first 2 sections of an ip to loop through.
	 */
	private void iterateOverIPRange(String ip) {
		for(int j = 0; j < 256; j++){
			if(running){
				/* itterate through the end section. */
				closestRangeIP(ip + j + '.'); 
			} else {
				break;
			}
			
		} 
	}
	
	/**
	 * @author Adam
	 */
	public void stopRunning(){
		running = false;
	}

	@Override
	public void run() {
		try {
			slave.switchOff();
			this.ip = getIP();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		findSlave();
	}
	
	/**
	 * This implementation of the Changer interface allows the simori
	 * to probe on a given port to find other Simori-ons over a network.
	 * The first to respond receives the masters configuration and the master
	 * continues to performance mode.
	 * 
	 * @author Adam
	 * @version 1.1.0
	 * @see Changer.getText(), Changer.doThingTo(), Changer.getCurrentSetting()
	 * @return Changer
	 */
	protected static Changer masterSlave(final ModeController controller){
		return new Changer(){

			@Override
			public String getText(Setting s) {
				return "Searching...";
			}

			@Override
			public boolean doThingTo(ModeController controller) {
				return true;
			}

			@Override
			public Setting getCurrentSetting() {
				controller.startNetworkMaster();
				return null;
			}
		};
	}
}