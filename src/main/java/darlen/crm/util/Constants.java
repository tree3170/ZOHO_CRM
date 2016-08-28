package darlen.crm.util;

import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * darlen.crm.util
 * Description：ZOHO_CRM
 * Created on  2016/08/24 22：47
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        22：47   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public interface Constants {
    public static  enum ModuleNameKeys{Leads,Accounts,Contacts,Invoices,Products,Quotes,SalesOrders};
    public static  String MODULE_NAME_KEYS_ALL = "Leads,Accounts,Contacts,Invoices,Products,Quotes,SalesOrders";
    /**定义result中每个module的key , for 每一个result的xml*/
    public static final String ELEMENT_RESULT_KEY="result";
    public static final String ELEMENT_ROW_KEY="row";
    public static final String ELEMENT_LEADS_KEY="Leads";
    public static final String ELEMENT_ACCOUNTS_KEY="Accounts";
    public static final String ELEMENT_CONTACTS_KEY="Contacts";
    public static final String ELEMENT_INVOICES_KEY="Invoices";
    public static final String ELEMENT_PRODUCTS_KEY="Products";
    public static final String ELEMENT_QUOTES_KEY="Quotes";
    public static final String ELEMENT_SALESORDER_KEY="SalesOrders";
    public static final String ELEMENT_ATTR_NO_KEY="no";
    public static final String ELEMENT_ATTR_VAL_KEY="val";
    /**for product details 字段的解析*/
    public static final String ELEMENT_ATTR_PRODDTL_VALUE="Product Details";
    public static final String ELEMENT_PRODUCT_KEY="product";
    /**end */

    /**POST METHOD PARAMS KEY*/
    public static final String HTTP_POST_PARAM_AUTHTOKEN="authtoken";
    public static final String HTTP_POST_PARAM_SCOPE="scope";
    public static final String HTTP_POST_PARAM_NEW_FORMAT="newFormat";
    public static final String HTTP_POST_PARAM_ID="id";
    public static final String HTTP_POST_PARAM_XMLDATA="xmlData";
    public static final String HTTP_POST_PARAM_UTF8="UTF-8";
    public static final String HTTP_POST_PARAM_TARGETURL="TARGET_URL";
    public static final String ZOHO_PROPS_NEWFORMAT_1="newFormat_1";
    public static final String ZOHO_PROPS_NEWFORMAT_2="newFormat_2";
    /** end */

    /**CONNECT DB CONSTANTS*/
    public static final String DB_CONNECT_DRIVER_NAME="DB_DRIVER_NAME";
    public static final String DB_CONNECT_DB_URL="DB_URL";
    public static final String DB_CONNECT_DB_USERNAME="DB_USERNAME";
    public static final String DB_CONNECT_DB_PWD="DB_PWD";
    /** end */

}
