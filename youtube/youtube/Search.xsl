<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/documentCollection">
<html>
  <body>
   <xsl:for-each select="document">
   <h3>Title</h3>
          <p><a href ="{link}" ><xsl:value-of select="title"/></a></p>
      <h3>Link</h3>
          <p><xsl:value-of select="link"/></p>
	  <h3>Tf-Idf Value</h3>
          <p><xsl:value-of select="tfIdf"/></p>
	            
     <hr/>
   </xsl:for-each>
     
</body>
</html>
</xsl:template>
</xsl:stylesheet>