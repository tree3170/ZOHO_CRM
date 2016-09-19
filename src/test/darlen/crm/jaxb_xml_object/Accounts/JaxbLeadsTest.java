package darlen.crm.jaxb_xml_object.Accounts;

import darlen.crm.jaxb_xml_object.commjaxb.FL;
import darlen.crm.jaxb_xml_object.commjaxb.FieldRows;
import darlen.crm.jaxb_xml_object.commjaxb.Response;
import darlen.crm.jaxb_xml_object.commjaxb.Row;
import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.util.CommonUtils;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


public class JaxbLeadsTest {

	@Test
	public void showMarshaller() {
//        Response<LeadsResult> response = new Response<LeadsResult>();
        Response response = new Response();
        LeadsResult result = new LeadsResult();
        FieldRows fieldRows = new FieldRows();
        List<Row> rows = new ArrayList<Row>();
        List<FL> fls = new ArrayList<FL>();
        Row row = new Row();
        row.setNo(1);
        FL fl = new FL();
        fl.setFieldName("Leads");
        fl.setFieldValue("Darlen");
        fls.add(fl);
        row.setFls(fls);
        rows.add(row);
        fieldRows.setRows(rows);
        result.setFieldRows(fieldRows);
        //response.setResult(result);
//        String str = JaxbUtil.convertToXml(response);
        String str = convertToXml(response);
        System.out.println(str);
        System.out.println("String to Object::: "+JaxbUtil.converyToJavaBean(str,Response.class));
	}

    public static String convertToXml(Object obj) {
        return convertToXml(obj, "UTF-8");
    }

    public static String convertToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Response.class,LeadsResult.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            //去掉默认生成的xml报头文<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
//            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,true);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
