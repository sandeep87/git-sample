package com.paradigmcreatives.ccl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class ServerData {

	public String data(String url) {
		
		HttpConnection connection = null;
		InputStream inputstream = null;
		String str = null;
		
		try
		{
		connection = (HttpConnection) Connector.open(url);
		
		connection.setRequestMethod(HttpConnection.GET);
		//connection.setRequestProperty("Content-Type","//text plain");
		//connection.setRequestProperty("Connection", "close");
		// HTTP Response
		System.out.println("Status Line Code: " + connection.getResponseCode());
		System.out.println("Status Line Message: " + connection.getResponseMessage());
		if (connection.getResponseCode() == HttpConnection.HTTP_OK)
		{
		//System.out.println(connection.getHeaderField(0)+ " " + connection.getHeaderFieldKey(0));
		//System.out.println("Header Field Date: " + connection.getHeaderField("date"));
			
		inputstream = connection.openInputStream();
		int length = (int) connection.getLength();
		System.out.println("Length"+length);
		if (length != -1)
		{
		byte incomingData[] = new byte[length];
		inputstream.read(incomingData);
		str = new String(incomingData);
		System.out.println(str +"/n");
		}
		else
		{
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = inputstream.read()) != -1)
		{
		bytestream.write(ch);
		}
		str = new String(bytestream.toByteArray());
		System.out.println("Data"+"/n"+str);
		bytestream.close();
		}	
		}
		return str;
		}
		catch(IOException error)
		{
		System.out.println("Caught IOException: " + error.toString());
		}
		
		finally
		{
		if (inputstream!= null)
		{
		try
		{
		inputstream.close();
		}
		catch( Exception error)
		{
		/*log error*/
		}
		}
		if (connection != null)
		{
		try
		{
		connection.close();
		}
		catch( Exception error)
		{
		/*log error*/
		}
		}
		}
		return null;
			
	}
}
