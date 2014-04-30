package edu.upenn.cis.cis555.youtube;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import rice.environment.Environment;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class PingPong implements Application{

	Node node;
	Endpoint endpoint;
	
	public PingPong(Node node)
	{
		this.node = node;
		//this.callingYtClient = ytClient;
		this.endpoint = node.buildEndpoint(this, "PingPong App");
		this.endpoint.register();
	}

	@Override
	public void deliver(Id id, Message message) {
		OurMessage om = (OurMessage)message;
		if(om.content.equalsIgnoreCase("PONG"))
		{
			System.out.println("Recieved "+om.content+ " Message from "+om.from);
		}
		else
		{
			System.out.println("Recieved "+om.content+ "Messsage to ID: "+ id+" from "+om.from+" returning PONG");
		//}
		if(om.wantResponse)
		{
			//String xmlString = callingYtClient.searchVideos(om.content);
			OurMessage reply = new OurMessage(node.getLocalNodeHandle(),"PONG",node.getId());
			//System.out.println("Sending"++ " to: "+om.from);
			reply.wantResponse = false;
			//this.senderflag = true;
			endpoint.route(null, reply, om.from);
		}
		}
		
	}

	@Override
	public boolean forward(RouteMessage arg0) {
		// TODO Auto-generated method stub
		System.out.println("In Forward...");
		return true;
	}

	@Override
	public void update(NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void sendMessage(Id idToSendTo, String msgToSend)
	{
		//System.out.println(this + " sending to "+idToSendTo);
		OurMessage m = new OurMessage(node.getLocalNodeHandle(),msgToSend,node.getId());
		System.out.println("Sending "+msgToSend+" to "+idToSendTo);
		endpoint.route(idToSendTo, m, null);
	}
	
	
}

