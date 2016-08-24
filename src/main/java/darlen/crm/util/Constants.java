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
    public static final String ELEMENT_SALESORDER_KEY="Sales Order";
    public static final String ELEMENT_ATTR_NO_KEY="no";
    public static final String ELEMENT_ATTR_VAL_KEY="val";
    /**end */
}
