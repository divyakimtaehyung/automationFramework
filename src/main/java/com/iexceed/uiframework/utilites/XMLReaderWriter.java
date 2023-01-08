package com.iexceed.uiframework.utilites;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.xpath.XPath;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class XMLReaderWriter {
    private static Document doc;
    private static XPath xPath;
    private static NodeList nodes;
    private static Transformer xformer;


    public static void updateElementText(String givenXpath, String filePath, int itemNo, String modifiedContent) {
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    new InputSource(filePath));
            xPath = XPathFactory.newInstance().newXPath();
            nodes = (NodeList) xPath.evaluate(givenXpath, doc,
                    XPathConstants.NODESET);
            nodes.item(itemNo).setTextContent(modifiedContent);
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
