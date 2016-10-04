package darlen.crm.util;

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
//    public static  enum ModuleNameKeys{Leads,Accounts,Contacts,Invoices,Products,Quotes,SalesOrders};
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
    public static final String HTTP_POST_FROM_INDEX="fromIndex";
    public static final String HTTP_POST_TO_INDEX="toIndex";
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


    //ID for ZOHO Modules
    public static final String MODULE_ACCOUNTS_ID="ACCOUNTID";
    public static final String MODULE_PRODUCTS_ID="PRODUCTID";
    public static final String MODULE_SO_ID="SALESORDERID";
    public static final String MODULE_INVOICES_ID="INVOICEID";
    public static final String MODULE_QUOTES_ID="QUOTEID";
    public static final String ERPID="ERP ID";



    /**设置开发模式*/
    public static final String ZOHO_PROPS_DEV_MODE="DEV_MODE";

    /**设置Properties文件名*/

    // for secure folder
    //PropertiesConfiguration初始化，不需要前面的斜杠
    public static final String PROPS_ZOHO_FILE ="secure/zoho.properties";//-->secure/zoho.properties
    //PropertiesConfiguration，不需要前面的斜杠
    public static final String PROPS_DB_FILE ="secure/db.properties";//-->secure/db.properties
    //Properties，需要前面的斜杠
    public static final String PROPS_USER_FILE = "secure/zohoUser.properties";//-->/secure/zohoUser.properties
    public static final String PROPS_PROD_FILE = "secure/Products.properties";//-->/secure/Product.properties
    public static final String PROPS_ACCT_FILE ="secure/Accounts.properties";//-->/secure/Account.properties

    //for mapping folder
    public static final String PROPS_ACCT_DB_MAPPING ="mapping/dbRdAccountsFieldMapping.properties";
    public static final String PROPS_PROD_DB_MAPPING ="mapping/dbRdProductsFieldMapping.properties";
    public static final String PROPS_QUOTE_DB_MAPPING ="mapping/dbRdQuotesFieldMapping.properties";
    public static final String PROPS_SO_DB_MAPPING ="mapping/dbRdSOFieldMapping.properties";
    public static final String PROPS_INVOICE_DB_MAPPING ="mapping/dbRdInvoicesFieldMapping.properties";



    /**Max Add size*/
    public static final int MAX_ADD_SIZE = 100;
    //每次查找时最大的增量for search
    public static final int MAX_FETCH_SIZE = 100;

    /**ZOHO需要做的增删改查操作*/
    public static final int ZOHO_CRUD_ADD = 1;
    public static final int ZOHO_CRUD_UPDATE = 2;
    public static final int ZOHO_CRUD_DELETE = 3;


}
