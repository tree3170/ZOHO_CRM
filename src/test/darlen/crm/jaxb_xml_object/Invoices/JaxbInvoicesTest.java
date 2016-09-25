package darlen.crm.jaxb_xml_object.Invoices;

import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.util.CommonUtils;
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
        List<Row> rows = new ArrayList<Row>();
        Row row = new Row();
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
		String soStr = "<response uri=\"/crm/private/xml/Leads/getRecords\">\n" +
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

        soStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/getRecords_Leads.xml");
        soStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/getRecords_SO.xml");
//        leadsStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/SO_Records.xml");
        Response response = JaxbUtil.converyToJavaBean(test.convertFLToPdsXmlTag(soStr), Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
		//System.out.println(leadsStr);
		System.out.println(response);
	}
	
}
