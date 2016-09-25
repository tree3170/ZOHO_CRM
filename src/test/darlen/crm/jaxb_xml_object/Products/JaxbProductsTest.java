package darlen.crm.jaxb_xml_object.Products;

import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.util.CommonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class JaxbProductsTest {

	@Test
	public void showMarshaller() {
        Response response = new Response();
        Result result = new Result();
        Products products = new Products();
        List<Row> rows = new ArrayList<Row>();
        Row row = new Row();
        row.setNo(1);
        List<FL> fls = new ArrayList<FL>();
        FL fl = new FL();
//        fl.setVal("LEADID");
//        fl.setFl("123123");
        fl.setFieldName("LEADID");
        fl.setFieldValue("Darlen");
        fls.add(fl);
        row.setFls(fls);
        rows.add(row);
        products.setRows(rows);
        result.setProducts(products);
        response.setResult(result);
        String str = JaxbUtil.convertToXml(response);
        System.out.println(str);
        System.out.println("String to Object::: "+JaxbUtil.converyToJavaBean(str,Response.class));
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

        leadsStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/getRecords_Leads.xml");
        System.out.println("leadsStr:::"+leadsStr);
        Response response = JaxbUtil.converyToJavaBean(leadsStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
		System.out.println("response object:::"+response);
	}
	
}
