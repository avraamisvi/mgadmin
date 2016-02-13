package br.org.isvi.mgadmin;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LocalServer  extends WebSocketServer {
//	Manager manager;
	Gson gson;
	WebSocket conn;
	
	public LocalServer(InetSocketAddress address) throws UnknownHostException {
		super(address);
		
		System.out.println("Starting server on: " + address.getHostName() + " " + address.getPort());
		
		gson = new GsonBuilder().create();
//		this.manager = new Manager(this);
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		this.conn = conn;
		conn.send("OK");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println("close");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// TODO Auto-generated method stub
		
		JsonObject msg = gson.fromJson(message, JsonObject.class);
//		manager.parseMessage(msg);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		// TODO Auto-generated method stub
		System.out.println("error");
		ex.printStackTrace();
	}
	
	public void send(JsonElement obj) {
		String ret = gson.toJson(obj);
		this.conn.send(ret);
	}
}
