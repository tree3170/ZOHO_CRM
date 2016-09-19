package darlen.crm.jaxb_xml_object.Accounts;

import darlen.crm.jaxb_xml_object.utils.JaxbUtil;
import darlen.crm.util.CommonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class JaxbAccountsTest {

	@Test
	public void showMarshaller() {
        Response response = new Response();
        Result result = new Result();
        Accounts accounts = new Accounts();
        List<Row> rows = new ArrayList<Row>();
        Row row = new Row();
        row.setNo(1);
        List<FL> fls = new ArrayList<FL>();
        FL fl = new FL();
        fl.setFieldName("Accounts");
        fl.setFieldValue("Darlen");
        fls.add(fl);
        row.setFls(fls);
        rows.add(row);
        accounts.setRows(rows);
        result.setAccounts(accounts);
        response.setResult(result);
        String str = JaxbUtil.convertToXml(response);
        System.out.println(str);
        System.out.println("String to Object::: "+JaxbUtil.converyToJavaBean(str,Response.class));
	}

	@Test
	public void showUnMarshaller() throws Exception {
		String accountsStr = "<response uri=\"/crm/private/xml/Leads/getRecords\">\n" +
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

        accountsStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/getRecords_Leads.xml");
        accountsStr = CommonUtils.getContentsByPathAndName("", "sampledata/records/Accounts_Records.xml");
        System.out.println("leadsStr:::"+accountsStr);
        Response response = JaxbUtil.converyToJavaBean(accountsStr, Response.class); //response.getResult().getLeads().getRows().get(0).getFls().get(1).getFl()
		System.out.println("response object:::"+response);
	}

}
