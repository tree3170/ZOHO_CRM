package darlen.crm.jaxb_xml_object.SO;

import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.util.CommonUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class JaxbSOTest {
    private static  JaxbSOTest test;
    @BeforeClass
    public static void  getInstance(){
        test = new JaxbSOTest();
    }

	@Test
	public void showMarshaller() {
        Response response = new Response();
        Result result = new Result();
        SO so = new SO();
        List<Row> rows = new ArrayList<Row>();
        Row row = new Row();
        row.setNo(1);
        List<FL> fls = new ArrayList<FL>();
        FL fl = new FL();
        fl.setFieldName("SOID");
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
        so.setRows(rows);
        result.setSo(so);
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

    private String convertFLToPdsXmlTag(String str){
        //由 Product Details之前的FL转换为pds:如何找到前后匹配的FL
        String prex = str.replace("<FL val=\"Product Details\">","<pds val=\"Product Details\">");
        //System.out.println("prex::"+prex );
        String suffix = prex.substring(0,prex.lastIndexOf("</FL>")) +"</pds>"+prex.substring(prex.lastIndexOf("</FL>")+5);
        System.out.println("suffix::"+suffix );
        return suffix;
    }

	@Test
	public void showUnMarshaller() throws Exception {
		String leadsStr = "<response uri=\"/crm/private/xml/Leads/getRecords\">\n" +
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

        leadsStr = CommonUtils.getJsonStringByPathAndName("", "sampledata/records/getRecords_Leads.xml");
        leadsStr = CommonUtils.getJsonStringByPathAndName("", "sampledata/records/getRecords_SO.xml");
        Response response = JaxbUtil.converyToJavaBean(test.convertFLToPdsXmlTag(leadsStr), Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
		//System.out.println(leadsStr);
		System.out.println(response);
	}
	
}
