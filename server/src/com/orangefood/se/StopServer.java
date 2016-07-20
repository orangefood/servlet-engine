package com.orangefood.se;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class StopServer
{
	public static void main(String[] args)
		throws Exception
	{
		int nPort = Integer.parseInt(args[1]);
		Socket sock = new Socket(InetAddress.getLocalHost(),nPort);
		OutputStream os = sock.getOutputStream();
		os.write(args[0].getBytes());
		sock.close();
	}
}
