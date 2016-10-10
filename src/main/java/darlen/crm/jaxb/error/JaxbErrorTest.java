package darlen.crm.jaxb.error;

import darlen.crm.util.JaxbUtil;
import org.junit.Test;


public class JaxbErrorTest {

	@Test
	public void showMarshaller() {
        Response response = new Response();
		Error error = new Error();
        error.setCode("43");
        error.setMessage("1111");
        response.setError(error);
		String str = JaxbUtil.convertToXml(response);
		System.out.println(str);
        System.out.println("123123"+JaxbUtil.converyToJavaBean(str,Response.class));
	}

	@Test
	public void showUnMarshaller() {
		String str = "<response uri=\"/crm/private/xml/Accounts/deleteRecords\">\n" +
                "\t<error>\n" +
                "\t\t<code>4600</code>\n" +
                "\t\t<message>Unable to process your request. Please verify if the name and value is appropriate for the \"id\" parameter.</message>\n" +
                "\t</error>\n" +
                "</response>";
		Response student = JaxbUtil.converyToJavaBean(str, Response.class);
		System.out.println(student);
	}
	
}
