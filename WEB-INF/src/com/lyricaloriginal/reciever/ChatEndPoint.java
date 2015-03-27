package com.lyricaloriginal.reciever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * チャットサーバのエンドポイントです。
 * @author LyricalMaestro
 *
 */
@ServerEndpoint("/loadMessage")
public class ChatEndPoint {

	private static Map<String, List<Session>> sessionGroups = new HashMap<String, List<Session>>();
	private String _roomKey = "Default";

	@OnOpen
	public void onOpen(Session session) {
		String queryString = session.getQueryString();
		Map<String, String> params = getParamFromQuery(queryString);
		if(params.containsKey("room")){
			_roomKey = params.get("room");
		}
		
		synchronized (sessionGroups) {
			List<Session> sessions = sessionGroups.get(_roomKey);
			if(sessions == null){
				sessions = new ArrayList<Session>();
				sessionGroups.put(_roomKey, sessions);
			}
			sessions.add(session);
		}
	}

	@OnMessage
	public void onMessage(String message) throws IOException {
		broadcast(message);
	}

	@OnClose
	public void onClose(Session session) {
		synchronized (sessionGroups) {
			List<Session> sessions = sessionGroups.get(_roomKey);
			sessions.remove(session);
		}
	}
	
	private void broadcast(String message) throws IOException {
		synchronized (sessionGroups) {
			List<Session> sessions = sessionGroups.get(_roomKey);
			for (Session session : sessions) {
				session.getBasicRemote().sendText(message);
			}
		}
	}
	
	private Map<String, String> getParamFromQuery(String queryString){
		Map<String, String> map = new HashMap<String, String>();
		if(isEmpty(queryString)){
			return map;
		}

		String[] keyvalues = queryString.split("&");
		for(String keyvalue : keyvalues){
			String[] kv = keyvalue.split("=");
			map.put(kv[0], kv[1]);
		}
		return map;
	}
	
	private static boolean isEmpty(String str){
		return str == null || str.isEmpty();
	}
}
