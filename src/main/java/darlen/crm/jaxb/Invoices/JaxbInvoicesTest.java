package darlen.crm.jaxb.Invoices;


import darlen.crm.jaxb.common.FL;
import darlen.crm.jaxb.common.ProdDetails;
import darlen.crm.jaxb.common.ProdRow;
import darlen.crm.jaxb.common.Product;
import darlen.crm.util.CommonUtils;
import darlen.crm.util.JaxbUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class JaxbInvoicesTest {
    private static JaxbInvoicesTest test;
    @BeforeClass
    public static void  getInstance(){
        test = new JaxbInvoicesTest();
    }

	@Test
	public void showMarshaller() {
        Response response = new Response();
        Result result = new Result();
        Invoices invoices = new Invoices();
        List<ProdRow> rows = new ArrayList<ProdRow>();
        ProdRow row = new ProdRow();
        row.setNo(1);
        List<FL> fls = new ArrayList<FL>();
        FL fl = new FL();
        fl.setFieldName("Invoices ID");
        fl.setFieldValue("123123");
        fls.add(fl);

        ProdDetails pds = new ProdDetails();
        Product product = new Product();
        product.setNo("1");
        FL fl2 = new FL();
        fl2.setFieldName("Product Name");
        fl2.setFieldValue("<![CDATA[ 服务器 ]]>");
        FL fl3 = new FL();
        fl3.setFieldName("Product Id");
        fl3.setFieldValue("85333000000089011");
        List<FL> pdsFls = new ArrayList<FL>();
        pdsFls.add(fl2);
        pdsFls.add(fl3);
        product.setFls(pdsFls);
        pds.setVal("Product Details");
        List<Product> products = new ArrayList<Product>();
        products.add(product);
        pds.setProducts(products);
        row.setFls(fls);
        row.setPds(pds);
        rows.add(row);
        invoices.setRows(rows);
        result.setInvoices(invoices);
        response.setResult(result);
        String str = JaxbUtil.convertToXml(response);
        System.out.println("转换pds为FL前：Object to XML"+str);
        //转化pds为FL
        String s1 = str.replace("pds", "FL");
        System.out.println("转换pds为FL后：Object to XML"+s1);
//
//        //由 Product Details之前的FL转换为pds:如何找到前后匹配的FL
//        String prex = s1.replace("<FL val=\"Product Details\">","<pds val=\"Product Details\">");
//        System.out.println("prex::"+prex );
//        String suffix = prex.substring(0,prex.lastIndexOf("</FL>")) +"</pds>"+prex.substring(prex.lastIndexOf("</FL>")+5);
//        System.out.println("suffix::"+suffix );
        System.out.println("convertFLToPdsXmlTag:"+test.convertFLToPdsXmlTag(s1));
        System.out.println("XML to Object::: "+JaxbUtil.converyToJavaBean(str,Response.class));
	}

    //TODO：太繁琐，以后改成正则表达式
    /**
     * 替换关于product detail的标签FL 为pds
     * 1. 先替换<FL val="Product Details"> 为 <pds val="Product Details">, 接下来替换最后一个</product>的第一个</FL>为</pds>
     * 2. 拿出最后一个</product>前面的字符串为prefProduct（包含</product>） 和  </product>之后的字符串suffixProduct（不包含</product>）
     * 3. 取出suffixProduct字符串的第一个为</FL>之后的字符串(目的是为了替换</FL> 为</pds>)
     * 4. 组装finalStr=prefProduct+"</pds>" + suffixProduct
     * @param str
     * @return
     */
    private String convertFLToPdsXmlTag(String str){
        //1.由 Product Details之前的FL转换为pds:如何找到前后匹配的FL
        String prexPds = str.replace("<FL val=\"Product Details\">","<pds val=\"Product Details\">");
        System.out.println("prex::"+prexPds );
//        String suffix = prexPds.substring(0,prexPds.lastIndexOf("</FL>")) +"</pds>"+prexPds.substring(prexPds.lastIndexOf("</FL>")+5);
        //2. 获取最后一个</product>前面的字符串
        String prefProduct = prexPds.substring(0,prexPds.lastIndexOf("</product>")+10);
        String suffixProduct= prexPds.substring(prexPds.lastIndexOf("</product>")+10);
        //3.取出suffixProduct字符串的第一个为</FL>之后的字符串
        suffixProduct = suffixProduct.substring(suffixProduct.indexOf("</FL>")+5);
        //4. 组装finalStr=prefProduct+"</pds>" + suffixProduct
        String finalStr = prefProduct+"</pds>" + suffixProduct;
        System.out.println("suffix::"+finalStr );
        return finalStr;
    }

	@Test
	public void showUnMarshaller() throws Exception {
		String invoiceStr = "<response uri=\"/crm/private/xml/Leads/getRecords\">\n" +
                "    <result>\n" +
                "        <Leads>\n" +
                "            <row no=\"1\">\n" +
                "                <FL val=\"LEADID\">85333000000072001</FL>\n" +
                "\t\t\t\t<FL val=\"Lead Owner\">\n" +
                "                    <![CDATA[ qq qq ]]>\n" +
                "                </FL>\n" +
                "                <FL val=\"Company\">\n" +
                "                    <![CDATA[ qq' company ]]>\n" +
                "                </FL>\n" +
                "\t\t\t</row>\n" +
                "        </Leads>\n" +
                "    </result>\n" +
                "</response>";

        invoiceStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/getRecords_Leads.xml");
        invoiceStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/getRecords_SO.xml");
        invoiceStr = "<response uri=\"/crm/private/xml/Invoices/getRecords\"><result><Invoices><row no=\"1\"><FL val=\"INVOICEID\">85333000000089067</FL><FL val=\"SMOWNERID\">85333000000071001</FL><FL val=\"Invoice Owner\"><![CDATA[tree3170]]></FL><FL val=\"Invoice Number\"><![CDATA[85333000000089069]]></FL><FL val=\"Subject\"><![CDATA[三一重工合同]]></FL><FL val=\"SALESORDERID\">85333000000089051</FL><FL val=\"Sales Order\"><![CDATA[三一重工合同]]></FL><FL val=\"Invoice Date\"><![CDATA[2016-08-23]]></FL><FL val=\"Due Date\"><![CDATA[null]]></FL><FL val=\"ACCOUNTID\">85333000000089007</FL><FL val=\"Account Name\"><![CDATA[三一重工]]></FL><FL val=\"Created Time\"><![CDATA[2016-08-23 00:11:58]]></FL><FL val=\"Modified Time\"><![CDATA[2016-08-23 00:11:58]]></FL><FL val=\"Sub Total\"><![CDATA[72220]]></FL><FL val=\"Discount\"><![CDATA[0]]></FL><FL val=\"Tax\"><![CDATA[0]]></FL><FL val=\"Adjustment\"><![CDATA[0]]></FL><FL val=\"Grand Total\"><![CDATA[72220]]></FL><FL val=\"Product Details\"><product no=\"1\"><FL val=\"Product Id\">85333000000089011</FL><FL val=\"Product Name\"><![CDATA[服务器]]></FL><FL val=\"Unit Price\">7222.0</FL><FL val=\"Quantity\">10.0</FL><FL val=\"Quantity in Stock\">10.0</FL><FL val=\"Total\">72220.0</FL><FL val=\"Discount\">0.0</FL><FL val=\"Total After Discount\">72220.0</FL><FL val=\"List Price\">7222.0</FL><FL val=\"Net Total\">72220.0</FL><FL val=\"Tax\">0.0</FL><FL val=\"Product Description\"><![CDATA[null]]></FL></product></FL><FL val=\"PayMethod\"><![CDATA[null]]></FL><FL val=\"DeliveryMethod\"><![CDATA[null]]></FL><FL val=\"Deposit\"><![CDATA[null]]></FL><FL val=\"ERP_ExchangeRate\"><![CDATA[null]]></FL><FL val=\"OtherCharge\"><![CDATA[null]]></FL><FL val=\"FreightAmount\"><![CDATA[null]]></FL><FL val=\"ERP_Currency\"><![CDATA[null]]></FL><FL val=\"DNNo\"><![CDATA[null]]></FL><FL val=\"ERP ID\"><![CDATA[null]]></FL><FL val=\"PONO\"><![CDATA[null]]></FL><FL val=\"CustomerNo\"><![CDATA[null]]></FL><FL val=\"PaymentTerm\"><![CDATA[null]]></FL><FL val=\"DeliveryAddress\"><![CDATA[null]]></FL><FL val=\"MailAddress\"><![CDATA[null]]></FL><FL val=\"Tel\"><![CDATA[null]]></FL><FL val=\"Email\"><![CDATA[null]]></FL><FL val=\"Fax\"><![CDATA[null]]></FL><FL val=\"Contact\"><![CDATA[null]]></FL><FL val=\"Total\"><![CDATA[null]]></FL><FL val=\"LatestEditTime\"><![CDATA[null]]></FL><FL val=\"LatestEditBy\"><![CDATA[null]]></FL><FL val=\"CreationTime\"><![CDATA[null]]></FL></row></Invoices></result></response>";
//        leadsStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/SO_Records.xml");
        Response response = JaxbUtil.converyToJavaBean(test.convertFLToPdsXmlTag(invoiceStr), Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
		//System.out.println(leadsStr);
		System.out.println(response);
	}
	
}
