<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:preserve-space elements="*"/>
  <xsl:param name="handler"/>
  <xsl:param name="listener"/>
  <xsl:template match="/ | @* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="comment()">
  	<xsl:choose>
		<xsl:when test='.="handler tags go here"'>
  			<xsl:copy-of select="document($handler)" />
  			<xsl:comment>handler tags go here</xsl:comment>
		</xsl:when>
		<xsl:when test='.="listener tags go here"'>
  			<xsl:copy-of select="document($listener)" />
  			<xsl:comment>listener tags go here</xsl:comment>
		</xsl:when>
    	<xsl:otherwise>
	    	<xsl:copy/>
	    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>

 

