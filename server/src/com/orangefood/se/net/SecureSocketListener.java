package com.orangefood.se.net;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import java.security.KeyStore;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * This class listens for connections on the specified port and when
 * a connection is recieved the socket is passed on the the SocketHandler
 * for processing.
 */
public class SecureSocketListener extends  TCPSocketListener
{
	private String _strKeyStore;
	private String _strKeyStorePasswd;
	
	public void init(Properties prop)
	{
		_strKeyStore=prop.getProperty("keystore");
		_strKeyStorePasswd=prop.getProperty("keystore-password");
		super.init(prop);
  	}
  
  protected void $createServerSocket()
  {
	try
	{
	  	//  First, get the default KeyManagerFactory.
	    String alg=KeyManagerFactory.getDefaultAlgorithm();
	    KeyManagerFactory kmFact=KeyManagerFactory.getInstance(alg);
	      
	    // Next, set up the KeyStore to use. We need to load the file into
	    // a KeyStore instance.
	    FileInputStream fis=new FileInputStream(_strKeyStore);
	    KeyStore ks=KeyStore.getInstance("jks");
	    ks.load(fis, _strKeyStorePasswd.toCharArray());
	    fis.close();

	    // Now we initialize the TrustManagerFactory with this KeyStore
	    kmFact.init(ks, _strKeyStorePasswd.toCharArray());

	    // And now get the suitable key managers
	    KeyManager[] kms=kmFact.getKeyManagers();

	    // Now construct a SSLContext using these KeyManagers. We
	    // specify a null TrustManager and SecureRandom, indicating that the
	    // defaults should be used.
	    SSLContext context=SSLContext.getInstance("SSL");
	    context.init(kms, null, null);

	    // Finally, we get a SocketFactory, and pass it to SimpleSSLClient.
	    SSLServerSocketFactory ssf=context.getServerSocketFactory();

		$srv = ssf.createServerSocket(getPort(), BACK_LOG, getBindAddress());

		// Set the listen timeout
		$srv.setSoTimeout( getListenTimeOut() );
		$setListeningPort($srv.getLocalPort());
	}
	catch ( Exception e )
	{
	  setRun(false);
	  e.printStackTrace();
	}  	
  }
}
