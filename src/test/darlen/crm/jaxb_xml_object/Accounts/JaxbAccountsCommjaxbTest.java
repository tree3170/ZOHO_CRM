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
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


public class JaxbAccountsCommjaxbTest {

	@Test
	public void showMarshaller() {
        Response<AccountsResult> response = new Response<AccountsResult>();
//        Response response = new Response();
        AccountsResult result = new AccountsResult();
        FieldRows fieldRows = new FieldRows();
        List<Row> rows = new ArrayList<Row>();
        List<FL> fls = new ArrayList<FL>();
        Row row = new Row();
        row.setNo(1);
        FL fl = new FL();
        fl.setFieldName("Accounts");
        fl.setFieldValue("Darlen");
        fls.add(fl);
        row.setFls(fls);
        rows.add(row);
        fieldRows.setRows(rows);
        result.setFieldRows(fieldRows);
        response.setResult(result);
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
            JAXBContext context = JAXBContext.newInstance(darlen.crm.jaxb_xml_object.commjaxb.Response.class,AccountsResult.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            //去掉默认生成的xml报头文<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
//            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,"true");

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * xml转换成JavaBean
     * @param xml
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T converyToJavaBean(String xml, Class<T> c) {
        T t = null;
        try {
//            JAXBContext context = JAXBContext.newInstance(c);
            JAXBContext context = JAXBContext.newInstance(c,AccountsResult.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
//            unmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            unmarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //去掉默认生成的xml报头文<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//            unmarshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

	@Test
	public void showUnMarshaller() throws Exception {
		String accountsStr = "<response>\n" +
                "    <result xsi:type=\"accountsResult\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "        <Accounts>\n" +
                "            <row no=\"1\">\n" +
                "                <FL val=\"Accounts\">Darlen</FL>\n" +
                "            </row>\n" +
                "        </Accounts>\n" +
                "    </result>\n" +
                "</response>";

        // leadsStr = CommonUtils.getContentsByPathAndName("","sampledata/records/Accounts_Records.xml");
        System.out.println("AccountsStr:::"+accountsStr);
//        Response response = JaxbUtil.converyToJavaBean(leadsStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
        Response response = converyToJavaBean(accountsStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
		System.out.println("response object:::"+response);
	}
	
}
