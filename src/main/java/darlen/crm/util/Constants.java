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
    public static final String HTTP_POST_PARAM_SELECTCOLS="selectColumns";
    public static final String HTTP_POST_PARAM_SORTORDER="sortOrderString";
    public static final String HTTP_POST_PARAM_SORTORDER_COL="sortColumnString";
    /** end */

    /**CONNECT DB CONSTANTS*/
    public static final String DB_CONNECT_DRIVER_NAME="DB_DRIVER_NAME";
    public static final String DB_CONNECT_DB_URL="DB_URL";
    public static final String DB_CONNECT_DB_USERNAME="DB_USERNAME";
    public static final String DB_CONNECT_DB_PWD="DB_PWD";
    /** end */

    /**zoho.properties常量*/
    public static final String ZOHO_PROPs_AUTHTOKEN_TREE="authtoken";
    public static final String ZOHO_PROPs_AUTHTOKEN_MATRIX="authtoken_matrix";
    public static final String ZOHO_PROPS_MODULES="MODULES";
    //url for fetch
    public static final String FETCH_INVOICES_URL="FETCH_INVOICES_URL";
    public static final String FETCH_SO_URL="FETCH_SO_URL";
    public static final String FETCH_QUOTES_URL="FETCH_QUOTES_URL";
    public static final String FETCH_ACCOUTNS_URL="FETCH_ACCOUTNS_URL";
    public static final String FETCH_PRODUCTS_URL="FETCH_PRODUCTS_URL";
    //url for INSERT
    public static final String INSERT_INVOICES_URL="INSERT_INVOICES_URL";
    public static final String INSERT_SO_URL="INSERT_SO_URL";
    public static final String INSERT_QUOTES_URL="INSERT_QUOTES_URL";
    public static final String INSERT_ACCOUTNS_URL="INSERT_ACCOUTNS_URL";
    public static final String INSERT_PRODUCTS_URL="INSERT_PRODUCTS_URL";
    //url for UPDATE
    public static final String UPDATE_INVOICES_URL="UPDATE_INVOICES_URL";
    public static final String UPDATE_SO_URL="UPDATE_SO_URL";
    public static final String UPDATE_QUOTES_URL="UPDATE_QUOTES_URL";
    public static final String UPDATE_ACCOUTNS_URL="UPDATE_ACCOUTNS_URL";
    public static final String UPDATE_PRODUCTS_URL="UPDATE_PRODUCTS_URL";
    //url for DELETE
    public static final String DELETE_INVOICES_URL="DELETE_INVOICES_URL";
    public static final String DELETE_SO_URL="DELETE_SO_URL";
    public static final String DELETE_QUOTES_URL="DELETE_QUOTES_URL";
    public static final String DELETE_ACCOUTNS_URL="DELETE_ACCOUTNS_URL";
    public static final String DELETE_PRODUCTS_URL="DELETE_PRODUCTS_URL";




    /**Max Add size*/
    public static final int MAX_ADD_SIZE = 1;

}
