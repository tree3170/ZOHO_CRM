package darlen.crm.util;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import darlen.crm.manager.ConfigManager;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Jaxb2工具类
 * @author		Darlen liu
 * @create
 */
public class JaxbUtil {
    private static Logger logger = Logger.getLogger(JaxbUtil.class);

	/**
	 * JavaBean转换成xml
	 * 默认编码UTF-8
	 * @param obj
	 * @param obj
	 * @return 
	 */
	public static String convertToXml(Object obj) {
		return convertToXml(obj, "UTF-8");
	}

	/**
	 * JavaBean转换成xml
	 * @param obj
	 * @param encoding 
	 * @return 
	 */
	public static String convertToXml(Object obj, String encoding) {
        logger.debug("entering the [JaxbUtil] convertToXml...");
		String result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            //去掉默认生成的xml报头文<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
            //去掉默认生成的Namespace
//            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,true);
            //转换所有的适配字符，包括xml实体字符&lt;和&gt;，xml实体字符在好多处理xml
            //的框架中是处理不了的，除非序列化。
            marshaller.setProperty("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler",
                    new CharacterEscapeHandler() {
                        @Override
                        public void escape(char[] ch, int start,int length, boolean isAttVal,
                                           Writer writer) throws IOException {
                            writer.write(ch, start, length);
                        }
                    });

			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			result = writer.toString();
		} catch (Exception e) {
			logger.error("#[JaxbUtil] convertToXml occurs error",e);
		}
        try {
            //如果是开发模式，需要打印这个log
            if( ConfigManager.isDevMod()){
                 logger.debug("end the [JaxbUtil] convertToXml..."+result);
            }
        } catch (Exception e) {
            logger.error("打印由Java Bean转换成的XML的log时出错...",e);
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
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}
}
