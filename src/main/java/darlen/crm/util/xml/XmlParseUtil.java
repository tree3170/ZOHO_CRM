/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   DomParse.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util.xml;

import darlen.crm.util.CommonUtils;
import darlen.crm.util.Constants;
import darlen.crm.util.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * darlen.crm.util.xml
 * Description：ZOHO_CRM  这个类用于解析所有的result的xml，并得到field和value的mapping关系
 * Created on  2016/08/24 21：38
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：38   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class XmlParseUtil {
    public static Logger logger = Logger.getLogger(XmlParseUtil.class);
    private static final String leads_filePath = "sampledata/records/getRecords_Leads.xml";
    private static final String so_filePath = "sampledata/records/getRecords_SO.xml";

    public static void main(String[] args) throws Exception {
        XmlParseUtil domParse = new XmlParseUtil();
        //for leads
        /*Document document = domParse.getDocument(CommonUtils.getFileNamePath("",leads_filePath));
        domParse.parseDocument(document, Constants.ELEMENT_LEADS_KEY);*/

        //for SO
        Document document = domParse.getDocument(CommonUtils.getFileNamePath("",so_filePath));
        domParse.parseDocument(document, Constants.ELEMENT_SALESORDER_KEY);
       // domParse.createXML();
    }

    /**
     * first read the file as a document
     * @param filePath
     * @return
     */
    public Document getDocument(String filePath) throws Exception{
        logger.debug("#[DomParse], start getDocument... ");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try{
            //DOM parse instance
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            //parse an XML file into a DOM tree
            document =  builder.parse(filePath);//this.getClass().getResourceAsStream(filePath));
        }  catch (Exception e) {
            logger.error("getDocument occurs error",e);
            throw e;
        }
        logger.debug("#[DomParse], end getDocument... ");
        return  document;
    }

    /**
     * 由W3C Document 转成DOM4J的document然后再解析Element
     * @param document
     * @param moduleKeys 指定module 的key值，以便解析时过滤
     */
    public void parseDocument(Document document,String moduleKeys){
        logger.debug("#[DomParse], start parseDocument... ");
        //转化w3cDocument to DOM4j Document
        DOMReader reader = new DOMReader();
        org.dom4j.Document dom4jDoc = reader.read(document);
        org.dom4j.Element root = dom4jDoc.getRootElement();
        System.out.println("Root Element Name:"+root.getName());
        List childList = root.elements();
        System.out.println("Size:"+childList.size());
        traverElement(root,1,moduleKeys);
        logger.debug("#[DomParse], end parseDocument... ");
    }

    /**
     * 遍历Element，并取出相应的Element的Name,attribute和value
     * @param root
     * @param i
     */
    private void traverElement(org.dom4j.Element root,int i, String moduleKeys){
//        logger.debug("#[DomParse], start traverElement... ");
        for(Iterator<org.dom4j.Element> iter = root.elementIterator();iter.hasNext();){

            org.dom4j.Element element = iter.next();
            String elementName = element.getName();
            String attrVal = element.attributeValue("val");
            String text = StringUtils.nullToString(element.getText()).trim();

            //如果name是既不是result也不是module key， 继续
            if(!Constants.ELEMENT_RESULT_KEY.equals(elementName) && !moduleKeys.equals(elementName)){
                if(Constants.ELEMENT_ROW_KEY.equals(elementName)){
                    int childSize = element.elements().size();
                    i=1;
                    attrVal = element.attributeValue("no");
                    logger.debug("$$$$第: "+attrVal+"条数据; 元素名字:::"+elementName+"; 属性 'no'的值:::"+attrVal+"; 有"+childSize+"个字段有值");
                }else{
                    //排除attribute value is product detail 和 element是product的数据
                    if(!Constants.ELEMENT_ATTR_VAL_VALUE.equalsIgnoreCase(attrVal) && !Constants.ELEMENT_PRODUCT_KEY.equalsIgnoreCase(elementName)){
                        logger.debug("#第: "+i+"个字段; 元素名字:::"+elementName+"; 属性 'val'的值:::"+attrVal+"; 文本内容:::"+text);
                        ++i;
                    }
                }
            }
            traverElement(element,i,moduleKeys);

        }
//        logger.debug("#[DomParse], end traverElement... ");
    }

    /**
     * W3C Document解析Element
     * @param document
     */
    public void parseDocument2(Document document){
        //get root element
        Element rootElement = document.getDocumentElement();

        //traverse child elements
        NodeList nodes = rootElement.getChildNodes();
        for (int i=0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                //process child element
            }
        }

        NodeList nodeList = rootElement.getElementsByTagName("book");
        if(nodeList != null)
        {
            for (int i = 0 ; i < nodeList.getLength(); i++)
            {
                Element element = (Element)nodeList.item(i);
                String id = element.getAttribute("id");
                System.out.println(id);
            }
        }
    }

    /**
     * 创建XML
     * @throws IOException
     */
    public void createXML() throws IOException {
        // 创建文档并设置文档的根元素节点 ：第一种方式
        // Document document = DocumentHelper.createDocument();
        //
        // Element root = DocumentHelper.createElement("student");
        //
        // document.setRootElement(root);

        // 创建文档并设置文档的根元素节点 ：第二种方式
        org.dom4j.Element root = DocumentHelper.createElement("student");
        org.dom4j.Document document = DocumentHelper.createDocument(root);

        root.addAttribute("name", "zhangsan");

        org.dom4j.Element helloElement = root.addElement("hello");
        org.dom4j.Element worldElement = root.addElement("world");

        helloElement.setText("hello");
        worldElement.setText("world");

        helloElement.addAttribute("age", "20");

        XMLWriter xmlWriter = new XMLWriter();
        xmlWriter.write(document);

        OutputFormat format = new OutputFormat("    ", true);

        XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream("student2.xml"), format);
        xmlWriter2.write(document);

        xmlWriter2.close();
    }
}
