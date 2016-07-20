package com.orangefood.xml;

import java.util.Properties;

public interface Handler
{
	public void processingInstruction(String pi, Properties attributes);
	public void open(String tag, Properties attributes);
	public void close(String tag);
	public void text(String text);
	public void comment(String comment);
}
