package com.orangefood.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Parser
{
	private Handler _handler;
	
	// Combine to a single buffer
	private StringBuffer _sbTag=new StringBuffer();
	private StringBuffer _sbAttribute=new StringBuffer();
	private StringBuffer _sbText=new StringBuffer();
	
	private String _strTag;
	private String _strAttrName=null;
	private String _strAttrValue=null;

	private Properties _propAttributes = new Properties();
	private StringBuffer _sbComment=new StringBuffer();
	
	private int _nParseMode=TEXT;
	public static final int TAG=1;
	public static final int ATTRIBUTE=2;
	public static final int COMMENT=3;
	public static final int TEXT=4;
	public static final int CDATA=5;
	
	private int _nTagType=OPEN;
	public static final int OPEN=1;
	public static final int CLOSE=2;
	public static final int EMPTY=3;
	public static final int PROCESSING_INSTRUCTION=4;
	
	
	public void parse(InputStream is, Handler handler)
		throws IOException
	{
		_handler=handler;
		int n=-1;
		while( (n=is.read())!=-1)
		{
			switch(_nParseMode)
			{
				case TAG:
					_handleTag(n);
					break;
				case ATTRIBUTE:
					_handleAttribute(n);
					break;
				case COMMENT:
					_handleComment(n);
					break;
				case TEXT:
					_handleText(n);
					break;
				case CDATA:
					_handleCDATA(n);
					break;
			}
		}
	}
	
	/**
	 * <tag attr="value">
	 * <tag attr="value"/>
	 * </tag>
	 * @param n
	 */
	private void _handleTag(int n)
	{
		switch(n)
		{
			case '>':
				_strTag=_sbTag.toString().trim();
				switch(_nTagType)
				{
					case CLOSE:
						_handler.close(_strTag);
						break;
					case OPEN:
						_handler.open(_strTag,_propAttributes);
						_propAttributes.clear();
						break;
					case EMPTY:
						_handler.open(_strTag,_propAttributes);
						_propAttributes.clear();
						_handler.close(_strTag);
						break;
					case PROCESSING_INSTRUCTION:
						_handler.processingInstruction(_strTag,_propAttributes);
						_propAttributes.clear();
						break;
				}
				_sbTag.setLength(0);
				_nParseMode=TEXT;
				_nTagType=OPEN;
				break;
			case '/':
				if( _sbTag.length()==0) // if the entity starts with a / then it's a close
				{
					_nTagType=CLOSE;
				}
				else
				{
					_nTagType=EMPTY;
				}
				break;
			case '?':
				if( _sbTag.length()==0) // if the entity starts with a ? then it's a processing instruction
				{
					_nTagType=PROCESSING_INSTRUCTION;
				}
				break;
			default:
				// If the tag starts with a comment indicator
				String strTag=_sbTag.toString();
				if ("!--".equals(strTag) )
				{
					_sbTag.setLength(0);
					_nParseMode=COMMENT;
					_handleComment(n);
				}
				else if( "![CDATA[".equals(strTag))
				{
					_sbTag.setLength(0);
					_nParseMode=CDATA;
					_handleCDATA(n);
				}
				// If there is a tag, the current character is not whitespace
				// and the previous character was whitespace, then we have an 
				// attribute
				else if( _sbTag.length()>0 && 
			             (!Character.isWhitespace((char)n) && Character.isWhitespace(_sbTag.charAt(_sbTag.length()-1)))
				  )
				{
					_nParseMode=ATTRIBUTE;
					_handleAttribute(n);						
				}
				else
				{	
					_sbTag.append((char)n);
				}
		}
	}
	
	/**
	 * name="value"
	 * @param n
	 */
	private void _handleAttribute(int n)
	{
		switch(n)
		{
			case '=':
				_strAttrName=_sbAttribute.toString();
				_sbAttribute.setLength(0);
				break;
			case '"':
				if(_sbAttribute.length()>0)
				{
					_propAttributes.setProperty(_strAttrName,_sbAttribute.toString());
					_sbAttribute.setLength(0);
					_nParseMode=TAG;
				}
				break;
			default:
				_sbAttribute.append((char)n);
		}
	}
	
	private void _handleComment(int n)
	{
		switch(n)
		{
			case '>':
				int len = _sbComment.length();
				if( _sbComment.charAt(len-1)=='-' &&
					_sbComment.charAt(len-2)=='-' )
				{
					_sbComment.deleteCharAt(len-1);
					_sbComment.deleteCharAt(len-2);
					_handler.comment(_sbComment.toString());
					_sbComment.setLength(0);
					_nParseMode=TEXT;
				}
				else
				{
					_sbComment.append('>');
				}
				break;
			default:
				_sbComment.append((char)n);
		}
	}
	
	private void _handleText(int n)
	{
		switch(n)
		{
			case '<':
				_handler.text(_sbText.toString());
				_sbText.setLength(0);
				_nParseMode=TAG;
				break;
			default:
				_sbText.append((char)n);
		}
		
	}
	
	private void _handleCDATA(int n)
	{
		switch(n)
		{
			case '>':
				int len = _sbComment.length();
				if( _sbText.charAt(len-1)==']' &&
					_sbText.charAt(len-2)==']' )
				{
					_sbText.deleteCharAt(len-1);
					_sbText.deleteCharAt(len-2);
					_handler.text(_sbText.toString());
					_sbText.setLength(0);
					_nParseMode=TEXT;
				}
				else
				{
					_sbText.append('>');
				}
				break;
			default:
				_sbText.append((char)n);
		}
	
	}
}
