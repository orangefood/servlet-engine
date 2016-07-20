<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:preserve-space elements="*"/>
  <xsl:param name="servlet"/>
  <xsl:param name="servletmapping"/>
  <xsl:template match="/ | @* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="comment()">
  	<xsl:choose>
		<xsl:when test='.="Servlet tags go here"'>
  			<xsl:copy-of select="document($servlet)" />
  			<xsl:comment>Servlet tags go here</xsl:comment>
		</xsl:when>
		<xsl:when test='.="Servlet mapping tags go here"'>
  			<xsl:copy-of select="document($servletmapping)" />
  			<xsl:comment>Servlet mapping tags go here</xsl:comment>
		</xsl:when>
    	<xsl:otherwise>
	    	<xsl:copy/>
	    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>

 

