package com.orangefood.se.util;


public class Base64
{
	public static final byte[] BASE64_ENCODE = new byte[] {
		(byte)'A',(byte)'B',(byte)'C',(byte)'D',(byte)'E',(byte)'F',(byte)'G',(byte)'H',
		(byte)'I',(byte)'J',(byte)'K',(byte)'L',(byte)'M',(byte)'N',(byte)'O',(byte)'P',
		(byte)'Q',(byte)'R',(byte)'S',(byte)'T',(byte)'U',(byte)'V',(byte)'W',(byte)'X',
		(byte)'Y',(byte)'Z',(byte)'a',(byte)'b',(byte)'c',(byte)'d',(byte)'e',(byte)'f',
		(byte)'g',(byte)'h',(byte)'i',(byte)'j',(byte)'k',(byte)'l',(byte)'m',(byte)'n',
		(byte)'o',(byte)'p',(byte)'q',(byte)'r',(byte)'s',(byte)'t',(byte)'u',(byte)'v',
		(byte)'w',(byte)'x',(byte)'y',(byte)'z',(byte)'0',(byte)'1',(byte)'2',(byte)'3',
		(byte)'4',(byte)'5',(byte)'6',(byte)'7',(byte)'8',(byte)'9',(byte)'+',(byte)'/'
		};
    
  public static final byte[] BASE64_DECODE = new byte[128];
  
  static
  {
    for (int n = 'A'; n <= 'Z'; n++)
    {
      BASE64_DECODE[n] = (byte)(n - 'A');
    }
  
    for (int n = 'a'; n <= 'z'; n++)
    {
      BASE64_DECODE[n] = (byte)(n - 'a' + 26);
    }
  
    for (int n = '0'; n <= '9'; n++)
    {
      BASE64_DECODE[n] = (byte)(n - '0' + 52);
    }
  
    BASE64_DECODE['+'] = 62;
    BASE64_DECODE['/'] = 63;
  }
  
  public static byte[] encode(byte[] abyData)
  {
    if(abyData==null)
    {
      return new byte[0];
    }
    
    // Calculate the remaining bytes
    int nRemainder = abyData.length % 3;
    int nLength = abyData.length - nRemainder;
    byte[] abyEncoded = new byte[(abyData.length/3)*4 + (nRemainder==0 ? 0 : 4 )];
    int n1,n2,n3;
    
    for( int nSource=0, nDest=0; nSource<nLength; nSource+=3, nDest+=4 )
    {
      n1 = abyData[nSource] & 0xff;
      n2 = abyData[nSource+1] & 0xff;
      n3 = abyData[nSource+2] & 0xff;
      
      abyEncoded[nDest]   = BASE64_ENCODE[( n1>>>2)             & 0x3f];
      abyEncoded[nDest+1] = BASE64_ENCODE[((n1 <<4) | (n2>>>4)) & 0x3f];  
      abyEncoded[nDest+2] = BASE64_ENCODE[((n2 <<2) | (n3>>>6)) & 0x3f];
      abyEncoded[nDest+3] = BASE64_ENCODE[  n3                  & 0x3f];
    }
    
    
    // Handle the remainder bytes
    switch( nRemainder )
    {
      case 1:
        n1 = abyData[abyData.length-1];
        
        abyEncoded[abyEncoded.length-4]=BASE64_ENCODE[(n1>>>2)&0x3f];
        abyEncoded[abyEncoded.length-3]=BASE64_ENCODE[(n1 <<4)&0x3f];
        abyEncoded[abyEncoded.length-2]=(byte)'=';
        abyEncoded[abyEncoded.length-1]=(byte)'=';
        break;
      case 2:
        n1 = abyData[abyData.length-2]&0xff;
        n2 = abyData[abyData.length-1]&0xff;
        
        abyEncoded[abyEncoded.length-4]=BASE64_ENCODE[( n1>>>2) & 0x3f];
        abyEncoded[abyEncoded.length-3]=BASE64_ENCODE[((n1 <<4) | (n2>>>4)) & 0x3f];
        abyEncoded[abyEncoded.length-2]=BASE64_ENCODE[( n2 <<2) & 0x3f];
        abyEncoded[abyEncoded.length-1]=(byte)'=';
        break;
    }
    return abyEncoded;
  }
  
  public static byte[] decode(byte[] abyData)
  {
    byte b1,b2,b3,b4;
    
    // Create the byte array to hold the decoded bytes
    byte[] abyDecoded;
    
    if( abyData==null || abyData.length==0)
    {
      return new byte[0];
    }
    
    if (abyData[abyData.length - 2] == '=')
    {
      abyDecoded = new byte[(((abyData.length / 4) - 1) * 3) + 1];
    }
    else if (abyData[abyData.length - 1] == '=')
    {
      abyDecoded = new byte[(((abyData.length / 4) - 1) * 3) + 2];
    }
    else
    {
      abyDecoded = new byte[((abyData.length / 4) * 3)];
    }    
    
    for (int nData = 0, nEncoded = 0; nData < abyData.length - 4; nData += 4, nEncoded += 3)
    {
      b1 = BASE64_DECODE[abyData[nData]];
      b2 = BASE64_DECODE[abyData[nData + 1]];
      b3 = BASE64_DECODE[abyData[nData + 2]];
      b4 = BASE64_DECODE[abyData[nData + 3]];

      abyDecoded[nEncoded]     = (byte)((b1 << 2) | (b2 >> 4));
      abyDecoded[nEncoded + 1] = (byte)((b2 << 4) | (b3 >> 2));
      abyDecoded[nEncoded + 2] = (byte)((b3 << 6) | b4);
    }

    if (abyData[abyData.length - 2] == '=')
    {
      b1 = BASE64_DECODE[abyData[abyData.length - 4]];
      b2 = BASE64_DECODE[abyData[abyData.length - 3]];

      abyDecoded[abyDecoded.length - 1] = (byte)((b1 << 2) | (b2 >> 4));
    }
    else if (abyData[abyData.length - 1] == '=')
    {
      b1 = BASE64_DECODE[abyData[abyData.length - 4]];
      b2 = BASE64_DECODE[abyData[abyData.length - 3]];
      b3 = BASE64_DECODE[abyData[abyData.length - 2]];

      abyDecoded[abyDecoded.length - 2] = (byte)((b1 << 2) | (b2 >> 4));
      abyDecoded[abyDecoded.length - 1] = (byte)((b2 << 4) | (b3 >> 2));
    }
    else
    {
      b1 = BASE64_DECODE[abyData[abyData.length - 4]];
      b2 = BASE64_DECODE[abyData[abyData.length - 3]];
      b3 = BASE64_DECODE[abyData[abyData.length - 2]];
      b4 = BASE64_DECODE[abyData[abyData.length - 1]];

      abyDecoded[abyDecoded.length - 3] = (byte)((b1 << 2) | (b2 >> 4));
      abyDecoded[abyDecoded.length - 2] = (byte)((b2 << 4) | (b3 >> 2));
      abyDecoded[abyDecoded.length - 1] = (byte)((b3 << 6) | b4);
    }
    return abyDecoded;  
  }
}

