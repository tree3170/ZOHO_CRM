/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   DBUtils.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util;

import darlen.crm.manager.ConfigManager;
import darlen.crm.model.result.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

/**
 * darlen.crm.util
 * TODO
 * 1. DB Object设置好 [ done]
 * 2. 13个人设置好 [ done]
 * 3. 当last edit time与ZOHO的不一致才去修改
 *
 *
 * 0. 编写configuration类
 *
 * 1. 测试4个Module是否已经ok
 * 2 .测试13个人的账号是否ok
 *
 * Description：ZOHO_CRM
 * Created on  2016/09/30 21：37
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：37   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class DBUtils {
    private static Logger logger = Logger.getLogger(DBUtils.class);
    public static final String ACCT_SQL = "select  zoho.customerid as ERPID ,zoho.* from dbo.Customer as zoho  where 1 =1 ";
    public static final String PROD_SQL = "select zoho.itemid as ERPID ,* from dbo.item  as zoho where 1 =1 ";
    public static final String QUOTES_SQL = "SELECT zoho.QuoteID AS ERPID, zoho.CustomerID, zoho.QuoteRef, zoho.CUSNAME AS CUSTOMERNAME, zoho.EXCHANGERATE AS EXGRATE ,zoho.LatestEditBy, \n" +
            "iq.Item_QuoteID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, iq.QuotePrice AS PROD_UNITPRICE,  iq.QUANTITY AS PROD_QUANTITY, iq.ITEMDISCOUNT AS PROD_DISCOUNT, iq.DESCRIPTION AS PROD_DESC , \n" +
            "pt.Name as PayTerm,\n" +
            "zoho.*\n" +
            " FROM Quote zoho\n" +
            "LEFT JOIN  Item_Quote iq  ON zoho.QuoteID = iq.QuoteID\n" +
            "LEFT JOIN  ITEM item  ON iq.ItemID = item.ITEMID\n" +
            "LEFT JOIN  PaymentTerm pt  ON pt.PaymentTermID = zoho.PaymentTermID\n" +
            "where 1 =1\n" +
            //"and zoho.QuoteID in (13,16,27)\n" +
            "and item.ITEMID is not null\n" ;
            //"and zoho.LatestEditBy in ('Pik Fai Chan','marketin','Gary Tang')" ;
            //"and item.LatestEditBy in ('Pik Fai Chan','marketin','Gary Tang')\n" +
            public static final String SO_SQL = "SELECT zoho.SOID AS ERPID, zoho.CustomerID, zoho.soREF, zoho.CUSNAME AS CUSTOMERNAME, zoho.EXCHANGERATE AS EXGRATE , zoho.LatestEditBy ,\n" +
            "itemso.ITEM_SOID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, itemso.SOPRICE AS PROD_UNITPRICE,  itemso.QUANTITY AS PROD_QUANTITY, itemso.ITEMDISCOUNT AS PROD_DISCOUNT, itemso.DESCRIPTION AS PROD_DESC , \n" +
            "pt.Name as PayTerm,\n" +
            "zoho.*\n" +
            " FROM SO zoho\n" +
            "LEFT JOIN  ITEM_SO itemso  ON zoho.SOID = itemso.SOID\n" +
            "LEFT JOIN  ITEM item  ON itemso.itemid = item.ITEMID\n" +
            "LEFT JOIN  PaymentTerm pt  ON pt.PaymentTermID = zoho.PaymentTermID\n" +
            "where 1 = 1 \n" +
            //"and zoho.SOID in (13,16,27)\n" +
            "and item.ITEMID is not null\n" ;
            //"and zoho.LatestEditBy in ('Pik Fai Chan','marketin','Gary Tang')\n" +
            //"and item.LatestEditBy in ('Pik Fai Chan','marketin','Gary Tang')" ;
            public static final String INVOICE_SQL = "SELECT zoho.InvoiceID AS ERPID, zoho.InvoiceRef,zoho.CustomerID,zoho.CUSNAME AS CUSTOMERNAME, zoho.EXCHANGERATE AS EXGRATE ,zoho.LatestEditBy,\n" +
                    "item_inv.Item_InvoiceID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, item_inv.InvoicePrice AS PROD_UNITPRICE,  item_inv.QUANTITY AS PROD_QUANTITY, item_inv.ITEMDISCOUNT AS PROD_DISCOUNT, item_inv.DESCRIPTION AS PROD_DESC , \n" +
                    "pt.Name as PayTerm,\n" +
                    "zoho.*\n" +
                    " FROM Invoice zoho\n" +
                    "LEFT JOIN  ITEM_INVOICE item_inv  ON zoho.InvoiceID = item_inv.InvoiceID\n" +
                    "LEFT JOIN  ITEM item  ON item_inv.itemid = item.ITEMID\n" +
                    "LEFT JOIN  PaymentTerm pt  ON pt.PaymentTermID = zoho.PaymentTermID\n" +
                    "where 1= 1 \n" +
                    //"and zoho.InvoiceID in(8,12,145)\n" +
                    "and item.ITEMID is not null\n" ;

    /**
     * 获取DB连接
     */
    public static Connection getConnection () throws SQLException, ClassNotFoundException, IOException, ConfigurationException {
        String driverName = ConfigManager.get(Constants.PROPS_DB_FILE,"DB_DRIVER_NAME");//zohoDBPropsMap.get("DB_DRIVER_NAME");//"com.microsoft.sqlserver.jdbc.SQLServerDriver";  //加载JDBC驱动
        String dbURL =ConfigManager.get(Constants.PROPS_DB_FILE,"DB_URL");//zohoDBPropsMap.get("DB_URL"); //"jdbc:sqlserver://localhost:1433; DatabaseName=test";  //连接服务器和数据库test
        String userName = ConfigManager.get(Constants.PROPS_DB_FILE,"DB_USERNAME");//zohoDBPropsMap.get("DB_USERNAME");;//"sa";  //默认用户名
        String userPwd = ConfigManager.get(Constants.PROPS_DB_FILE,"DB_PWD");//zohoDBPropsMap.get("DB_PWD");//"zaq1@WSX";  //密码
        Connection dbConn = null;

        try {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
            logger.info("Connection Successful!");  //如果连接成功 控制台输出Connection Successful!
        } catch (SQLException e) {
            logger.error("SQLException , 连接DB失败",e);
            throw  e;
        }catch (ClassNotFoundException e){
            logger.error("ClassNotFoundException ...",e);
            throw  e;
        }
        return dbConn;
    }

    public static void main(String[] args) throws Exception {
        getConnection();
//        CommonUtils.printList(getAccountMap(), "打印所有的Customer：：：");
//        CommonUtils.printList(getProductMap(), "打印所有的 Products：：：");
//        getSOMap();
//        getInvoiceMap();
//        CommonUtils.printList(getSOMap(), "打印所有的销售订单：：：");
//        getAccountMap();



    }

    /**
     * 获取根据ZOHO User过滤得到相应的完整SQL
     * @param sql
     * @return
     * @throws IOException
     * @throws ConfigurationException
     */
    public static String getWholeSqlWithFilterUser(String sql, String moduleName,String orderBy) throws IOException, ConfigurationException {
        List<String> allUsers = ConfigManager.getAllZohoUserName();
        if(allUsers!= null && allUsers.size() > 0){
            sql += "\n and upper(zoho.LatestEditBy) in (" ;
            for(String name : allUsers){
                sql += "upper('"+name+"')" + ",";
            }
            sql = sql.substring(0,sql.length()-1);
            sql += ") \n";
        }
        sql += orderBy;
        //logger.debug(" [ getWholeSqlWithFilterUser], module="+moduleName+ Constants.COMMENT_PREFIX+"\t sql = "+sql);
        return sql;
    }

    /**
     * 如果ZOHO所有的用户账号中包含LastEditBy，则返回true，否则返回false
     * @param lastEditBy
     * @return
     */
    private static boolean containERPAcct(String lastEditBy) throws IOException, ConfigurationException {
        //accountPropsMap.containsKey(lastEditBy)
        if(!StringUtils.isEmptyString(lastEditBy) && !"".equals(ConfigManager.getZohoUserfromProps(lastEditBy))){
            return true;
        }
        logger.debug("#不包含ERP ACCT["+lastEditBy+"]， 忽略...");
        return false;
    }

    /**
     * TODO
     * 1. 在此之前，需要先过滤13个账户
     * 2. 从Cache中拿LastEditBy的Name和ID
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public synchronized static List getAccountMap(boolean isSepatateRun,String sqlWithErpIDs) throws Exception {
        List dbAcctList = new ArrayList();
        Map<String,String> dbIDEditTimeMap = new HashMap<String, String>();
        Map<String,Object> dbIDModuleObjMap = new TreeMap<String, Object>(new Comparator<String>(){

            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(String o1, String o2) {
                //按照Key ： ERP ID倒序排序
                //return o1.compareTo(o2);
                return StringUtils.nullToInt(o2) - StringUtils.nullToInt(o1);
            }
        });
        dbAcctList.add(0,dbIDModuleObjMap);
        dbAcctList.add(1,dbIDEditTimeMap);
//        List<Accounts> accountList = new ArrayList<Accounts>();

                // and CustomerID in (1,8,14,20)"; //暂时只用三条数据
        String sql = getWholeSqlWithFilterUser(ACCT_SQL,ModuleNameKeys.Accounts.toString()," ORDER BY zoho.customerID DESC");
        if(isSepatateRun){
            sql = sqlWithErpIDs;
        }
        ResultSet rs = exeQuery(sql);
        while (rs != null && rs.next()){
            String lastEditBy = StringUtils.nullToString(rs.getString("LatestEditBy"));
            if(containERPAcct(lastEditBy)){
                Accounts account = new Accounts();
                // ERP ID
                String erpID = StringUtils.nullToString(rs.getString("CustomerID"));
                account.setErpID(erpID);
                //TODO:从Cache中拿到Name，并查到ID，然后填上ID和Name作为拥有者设入。如果Name改变了怎么办
                //User --> lastEditBy一致
                //User --> lastEditBy一致
//                User user = new User("80487000000076001","marketing");
                User user = new User(ConfigManager.getZohoUserfromProps(lastEditBy),lastEditBy);
                account.setUser(user);
                boolean isUserExist = checkUserAndAcctExist("","",user,
                        erpID,lastEditBy,ModuleNameKeys.Accounts.toString());
                if(!isUserExist){
                    continue;
                }
                //客戶編號
                account.setCustomerNO(StringUtils.nullToString(rs.getString("CustomerRef")));
                //客户公司名
                account.setAcctName(StringUtils.nullToString(rs.getString("Name")));
                //TODO: 数据库中Enable是1或者0，但是在ZOHO中是true或者false，需要转换下
                //是否隱藏客户            JDBC BIT 类型的 Java 映射的推荐类型是 Java 布尔型:
                boolean enable = rs.getBoolean("Enabled");
                account.setEnabled(StringUtils.nullToString(enable));
                account.setPhone(StringUtils.nullToString(rs.getString("Tel")));
                account.setFax(StringUtils.nullToString(rs.getString("Fax")));
                //公司聯絡人
                account.setContact(StringUtils.nullToString(rs.getString("Contact")));
                //聯絡人直線电话
                account.setDirect(StringUtils.nullToString(rs.getString("Direct")));
                //发货地址
                account.setDeliveryAddress(StringUtils.nullToString(rs.getString("DeliveryAddress")));
                account.setEmail(StringUtils.nullToString(rs.getString("Email")));
                //邮寄地址
                account.setMailAddress(StringUtils.nullToString(rs.getString("MailAddress")));
                //客户网站
                account.setWebsite(StringUtils.nullToString(rs.getString("Website")));
                //国家ID (他們沒有用此功能)
    //            account.setCountryID(rs.getString("CountryID"));
                //州
                account.setState(StringUtils.nullToString(rs.getString("state")));
                //邮编
                account.setPostNo(StringUtils.nullToString(rs.getString("PostNo")));
                //城市
                account.setCity(StringUtils.nullToString(rs.getString("City")));
                //配送方式
                account.setDeliveryMethod(StringUtils.nullToString(rs.getString("DeliveryMethod")));
                //備註
                account.setRemark(StringUtils.nullToString(rs.getString("Remark")));
                account.setCreationTime(StringUtils.nullToString(rs.getString("CreationTime")));
                String creationTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("CreationTime"));
                //TODO 暂时测试阶段用最新的时间
//                boolean isDevMod  = "1".equals(ConfigManager.get(Constants.PROPS_ZOHO_FILE,Constants.ZOHO_PROPS_DEV_MODE));
//                String lastEditTime = ConfigManager.isDevMod() ? ThreadLocalDateUtil.formatDate(new Date()):ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                String lastEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                account.setCreationTime(creationTime);
                account.setLatestEditTime(lastEditTime);

                account.setLatestEditBy(lastEditBy);
//                accountList.add(account);
                dbIDModuleObjMap.put(erpID,account);
                dbIDEditTimeMap.put(erpID,lastEditTime);
            }
        }
        return dbAcctList;
    }

    /**
     * 关于Product模块，不需要过滤LastestEditBy，因为在ERP中不会有这个checking
     * @return
     * @throws Exception
     */
    public synchronized static List getProductMap(boolean isSepatateRun,String sqlWithErpIDs) throws Exception {
        List dbModuleList = new ArrayList();
        Map<String,String> dbIDEditTimeMap = new HashMap<String, String>();
        Map<String,Object> dbIDModuleObjMap = new TreeMap<String, Object>(new Comparator<String>(){

            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(String o1, String o2) {
                //按照Key ： ERP ID倒序排序
                //return o1.compareTo(o2);
                return StringUtils.nullToInt(o2) - StringUtils.nullToInt(o1);
            }
        });
        dbModuleList.add(0,dbIDModuleObjMap);
        dbModuleList.add(1,dbIDEditTimeMap);
//        List<Products> productsList = new ArrayList<Products>();
        String sql = PROD_SQL + "order by zoho.ItemID DESC";
        if(isSepatateRun){
            sql = sqlWithErpIDs;
        }
                //+ "where itemid in (6,9,10,130)"; //暂时只用三条数据
        ResultSet rs = exeQuery(sql);
        while (rs != null && rs.next()){
            String lastEditBy = StringUtils.nullToString(rs.getString("LatestEditBy"));

            //if(containERPAcct(lastEditBy)){
                Products product = new Products();
                String erpID = StringUtils.nullToString(rs.getString("ItemID"));
                //从Cache中拿出erpID对应的
                product.setErpID(erpID);
                // Product Name 产品名称
                product.setProdName(StringUtils.nullToString(rs.getString("Name")));
                //Product Code产品编码
                product.setProdCode(StringUtils.nullToString(rs.getString("ItemRef")));
                //设置Product Owner产品拥有者：与lastEditBy一致
                //TODO：Product Owner产品所有者 ，就是DB中的lastEditBy
                User user = new User();
                user.setUserID(ConfigManager.getZohoUserfromProps(lastEditBy));//accountPropsMap.get(lastEditBy)
                user.setUserName(lastEditBy);
                //User user = fetchDevUser(false);
                product.setUser(user);
                //是否隱藏客户
                product.setEnabled(StringUtils.nullToString(rs.getString("Enabled")));
                //产品分类
                product.setCatagory(StringUtils.nullToString(rs.getString("Catagory")));
                //产品子分类
                product.setSubCategory(StringUtils.nullToString(rs.getString("SubCategory")));
                product.setPacking(StringUtils.nullToString(rs.getString("Packing")));
                product.setProdSize(StringUtils.nullToString(rs.getString("ProductSize")));
                //ItemDesc产品描述
                product.setItemDesc(StringUtils.nullToString(rs.getString("Description")));
                //Unit单位
                product.setUnit(StringUtils.nullToString(rs.getString("Unit")));
                //barcode條碼
                product.setBarcode(StringUtils.nullToString(rs.getString("Barcode")));
                product.setRemark(StringUtils.nullToString(rs.getString("Remark")));
                product.setLatestEditBy(lastEditBy);
//                boolean isDevMod  = "1".equals(ConfigManager.get(Constants.PROPS_ZOHO_FILE,Constants.ZOHO_PROPS_DEV_MODE));
//                String latestEditTime = ConfigManager.isDevMod() ? ThreadLocalDateUtil.formatDate(new Date()):ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                String latestEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
//                String latestEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                product.setLatestEditTime(latestEditTime);
//                productsList.add(product);
                dbIDModuleObjMap.put(erpID,product);
                dbIDEditTimeMap.put(erpID,latestEditTime);
            //}
        }
        logger.info("Product size:::" + dbIDModuleObjMap.size());
        return dbModuleList;
    }

    /**
     * TODO:不是直接顯示ID，要顯示PaymentTerm表中的Name字段
     * TODO: 如何显示
     * 1. 重点在处理各种ID和Name  ：
     *    a.AccountID --> Accounts.properties
     *    b.ProductID --> Products.properties
     *    c.UserID --> zohoUser.properties
     *    d.PaytermID --> payterm.properties
     * 2. 重点在Product Detail的处理：处理多个Product和1个Product情况
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public synchronized static List getQuotesList(boolean isSepatateRun,String sqlWithErpIDs) throws Exception{
        List dbModuleList = new ArrayList();
        Map<String,String> dbIDEditTimeMap = new HashMap<String, String>();
        Map<String,Object> dbIDModuleObjMap  = new TreeMap<String, Object>(new Comparator<String>(){

            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(String o1, String o2) {
                //按照Key ： ERP ID倒序排序
                //return o1.compareTo(o2);
                return StringUtils.nullToInt(o2) - StringUtils.nullToInt(o1);
            }
        });
        dbModuleList.add(0,dbIDModuleObjMap);
        dbModuleList.add(1,dbIDEditTimeMap);
//        List<Quotes> moduleList = new ArrayList<Quotes>();
//        String sql = "select s1.soid , s1.cusName,s2.soid ,s2.Item_SOID,s2.ItemID,s2.ItemName from  item_SO s2  left join  SO s1 on s1.soid = s2.SOID";
//        sql = "select s1.soid , s1.cusName,s2.Item_SOID,s2.ItemID,s2.ItemName, item.Name as itemName\n" +
//                " from SO s1 \n" +
//                "left join  item_SO s2  on s1.soid = s2.SOID\n" +
//                "left join  item item  on s2.itemid = item.itemid\n" +
//                " order by s1.soid\n";
//        sql = "SELECT q.QuoteID AS ERPID, q.CustomerID, q.QuoteRef, q.CUSNAME AS CUSTOMERNAME, q.EXCHANGERATE AS EXGRATE ,\n" +
//                "iq.Item_QuoteID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, iq.QuotePrice AS PROD_UNITPRICE,  iq.QUANTITY AS PROD_QUANTITY, iq.ITEMDISCOUNT AS PROD_DISCOUNT, iq.DESCRIPTION AS PROD_DESC , \n" +
//                "q.*\n" +
//                " FROM Quote q\n" +
//                "LEFT JOIN  Item_Quote iq  ON q.QuoteID = iq.QuoteID\n" +
//                "LEFT JOIN  ITEM item  ON iq.ItemID = item.ITEMID\n" +
//                "where 1 =1\n" +
//                //"and q.QuoteID in (13,16,27)\n" +
//                "and item.ITEMID is not null" +
//                " ORDER BY q.QuoteID";
        String sql = getWholeSqlWithFilterUser(QUOTES_SQL,ModuleNameKeys.Quotes.toString()," ORDER BY zoho.QuoteID DESC");
        if(isSepatateRun){
            sql = sqlWithErpIDs;
        }
        ResultSet rs = exeQuery(sql);
        String preErpID = "";
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        Quotes quotes = null;
        while (rs != null && rs.next()){
            String lastEditBy = StringUtils.nullToString(rs.getString("LatestEditBy"));
            //if(containERPAcct(lastEditBy)){
                /**DB中的SO id*/
                String curErpID = StringUtils.nullToString(rs.getString("QuoteID"));
                //相同SO，代表这个SO有多个Product，不需要新创建SO，只需要把product添加到已有的List<ProductDetails> pds中
                if(preErpID.equals(curErpID) && quotes != null){
                    /**
                     * 处理list<ProductDetail>, 根据db中的SOID关联Item_SO表，拿出所有的product Detail,注意为空情况
                     Double.valueOf(so.getErpExchangeRate())
                     */
                    ProductDetails productDetails = assembleProduct(rs);
                    //如果 因为Prod ID造成ProductDetails为空的情况，忽略这条product
                    if( null != productDetails){
                        pds.add(assembleProduct(rs));
                        quotes.setPds(pds);
                    }
                } else{//代表不同SO,需要重新创建SO对象

                    //在新一条数据之前，判断如果上一条数据product detail是空的话 , 那么需要移除上一条记录
                    if(quotes == null || quotes.getPds() == null || quotes.getPds().size() > 0){
                        //TODO 因为不存在Product Detail，所以忽略整条数据
                        dbIDModuleObjMap.remove(curErpID);
                        dbIDEditTimeMap.remove(curErpID);
                    }

                    //erpid 不相同之前，先把上一个SO添加到list中（排除第一条数据对第一次遍历SO）
//                    if(null != quotes )moduleList.add(quotes);
                    //因为是不同的SO对象了，所以把SO添加到list之后，需要重新创建SO和Product对象, 并且把preErpID指向当前的ERP ID
                    quotes = new Quotes();
                    pds = new ArrayList<ProductDetails>();
                    preErpID = curErpID;
                    quotes.setErpID(curErpID);
                    //TODO 销售编号与SoNumber需要确认,不需要，这个字段是ZOHO ID,从配置文件中获取
                    //                so.setSALESORDERID(rs.getString("ItemRef"));
                    //TODO 销售拥有者
                    //        so.setOwerID("85333000000071039");
                    //        so.setOwner("qq");
//                    User user = new User("80487000000076001","marketing");
                    User user = new User(ConfigManager.getZohoUserfromProps(lastEditBy),lastEditBy);
                    quotes.setUser(user);
                    String soNumber =StringUtils.nullToString(rs.getString("QuoteRef"));
                    /**SONumber 和Subject一致*/
                    quotes.setSubject(soNumber);
                    quotes.setQuotesNo(soNumber);
                    /**
                     * TODO
                     * Account id 和Name一般是同时存在的；
                     * 如果只存在id，Name可以不对；
                     * 如果只存在Name，那么会新创建一个客户；
                     * 例外：但如果有ID，那么ID必需存在已经创建的客户中
                     */
                    String erpAcctID = StringUtils.nullToString(rs.getString("CustomerID"));
                    String zohoAcctID = ConfigManager.getAcctsfromProps(erpAcctID);
                    //logger.debug(" [ getQuotesList] ,ERP ID = "+curErpID+", CustomerID ="+erpAcctID+", ZOHO Account ID="+zohoAcctID);
                    //TODO：CONFIRM， 如果找不到AccoutnID,则默认为空
                    if(!StringUtils.isEmptyString(zohoAcctID)){
                        quotes.setCustID(zohoAcctID);//"80487000000096005"
                        quotes.setACCOUNTID(zohoAcctID);
                        quotes.setCustName(StringUtils.nullToString(rs.getString("CusName")));
                    }else{
                        //TODO：CONFIRM  如果找不到Accounts ID， 那么用为空，默认就是Admin账号
                    }
                    boolean isUserAndAcctExist = checkUserAndAcctExist(erpAcctID,zohoAcctID,user,
                            curErpID,lastEditBy,ModuleNameKeys.Quotes.toString());
                    if(!isUserAndAcctExist){
                        continue;
                    }
                    quotes.setCustNO(StringUtils.nullToString(rs.getString("CusRef")));

                    /**
                     * 客户名Account Name：PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
                     */
                    //so.setAcctName(rs.getString("CusName"));//"永昌紙品"
                    //报价名称
                    quotes.setQuotesNo(StringUtils.nullToString(rs.getString("QuoteRef")));
                    //客户名ERP_Currency,DB中用CurrencyName表示
                    quotes.setErpCurrency(StringUtils.nullToString(rs.getString("CurrencyName")));
                    //公司联络人Contact
                    quotes.setContact(StringUtils.nullToString(rs.getString("CusContact")));
                    //客户邮件地址
//                    quotes.setMailAddress(StringUtils.nullToString(rs.getString("CusMailAddress")));
//                    quotes.setEmail(StringUtils.nullToString(rs.getString("CusEmail")));
                    //客户PONo
//                    quotes.setPoNO(StringUtils.nullToString(rs.getString("CustomerPONo")));
//                    quotes.setDeliveryAddress(rs.getString("CusDeliveryAddress"));
//                    quotes.setTel(StringUtils.nullToString(rs.getString("CusTel")));
//                    quotes.setFax(StringUtils.nullToString(rs.getString("CusFax")));
                    BigDecimal exchangeRate = rs.getBigDecimal("ExchangeRate");
                    quotes.setErpExchangeRate(exchangeRate.toString());
                    /**TODO:不是直接顯示ID，要顯示PaymentTerm表中的Name字段         */
                    quotes.setPaymentTerm(StringUtils.nullToString(rs.getString("PayTerm")));
                    quotes.setPayMethod(StringUtils.nullToString(rs.getString("PayMethod")));
//                    quotes.setDeliveryMethod(StringUtils.nullToString(rs.getString("DeliveryMethod")));
//                    quotes.setPaymentPeriod(StringUtils.nullToString(rs.getString("PaymentPeriod")));
                    //销售订单日期SODate
                    String quoteDate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("QuoteDate"));
                    quotes.setQuotesDate(quoteDate);
//                    boolean isDevMod  = "1".equals(ConfigManager.get(Constants.PROPS_ZOHO_FILE,Constants.ZOHO_PROPS_DEV_MODE));
//                    String latestEditTime = ConfigManager.isDevMod() ? ThreadLocalDateUtil.formatDate(new Date()):ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                    String latestEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
//                    String currentDate = ThreadLocalDateUtil.formatDate(new Date());
                    quotes.setLatestEditTime(latestEditTime);
//                    so.setCreationTime("2016-09-01 10:10:10");
                    quotes.setLatestEditBy(user.getUserName());

                    /**
                     * TODO  , 需要计算： 待定
                     * 设置product detail右下角那堆属于SO的字段:Discount,Sub Total,Grand Total
                     */
                    quotes.setDiscount("0");
                    quotes.setSubTotal("0");//小计
                    quotes.setGrandTotal("0");//累计
                    //Customer Discount == Discount
                    BigDecimal cusDiscount = rs.getBigDecimal("CusDiscount");
                    quotes.setCusDiscount(StringUtils.nullToString(cusDiscount.multiply(exchangeRate)));
                    /**Discount来自销售订单中的“折扣” */
                    quotes.setDiscount(StringUtils.nullToString(cusDiscount.multiply(exchangeRate)));
                    /**Sub Total 来自销售订单中的“小计”*/
                    //TODO : 小计-->估计是total,但是在SO中没有Total，那么就需要自己去计算
                    //so.setSubTotal(String.valueOf(total.multiply(exgRate)));
                    /**Grand Total来自销售订单中的“累计”*/
                    //TODO : 累计-->估计应该是total-折扣
                    //so.setGrandTotal(String.valueOf(total.multiply(exgRate).subtract(cusDiscount)));
                    //处理第一条Product
                    ProductDetails productDetails = assembleProduct(rs);
                    //如果 因为Prod ID造成ProductDetails为空的情况，忽略这条product
                    if( null != productDetails){
                        pds.add(productDetails);
                        quotes.setPds(pds);
                        dbIDModuleObjMap.put(curErpID,quotes);
                        dbIDEditTimeMap.put(curErpID,latestEditTime);
                    }

                //}
            }
        }
        setGrandTotal(ModuleNameKeys.Quotes.toString(),dbIDModuleObjMap);
        return dbModuleList;
//        return moduleList;
    }

    /**
     * 设置SO/Quotes中的总额
     * @param moduleName
     * @param dbIDModuleObjMap
     */
    private static void setGrandTotal(String moduleName, Map<String,Object> dbIDModuleObjMap){
        if(ModuleNameKeys.Quotes.toString().equals(moduleName)){
            for(Map.Entry entry:dbIDModuleObjMap.entrySet()){
                Quotes quotes = (Quotes)entry.getValue() ;
                List<ProductDetails> pds = quotes.getPds();
                BigDecimal grandTotal = getAllPdsTotal(pds);
                quotes.setGrandTotal(StringUtils.nullToString(grandTotal));
            }
        }
        if(ModuleNameKeys.SalesOrders.toString().equals(moduleName)){
            for(Map.Entry entry:dbIDModuleObjMap.entrySet()){
                SO so = (SO)entry.getValue() ;
                List<ProductDetails> pds = so.getPds();
                BigDecimal grandTotal = getAllPdsTotal(pds);
                so.setGrandTotal(StringUtils.nullToString(grandTotal));
            }
        }
    }

    /**
     * 获取所有价格的总额
     * @param pds
     * @return
     */
    private static BigDecimal getAllPdsTotal(List<ProductDetails> pds){
        BigDecimal grandTotal = new BigDecimal("0");
        for(int i = 0; i< pds.size(); i++){
            ProductDetails pd = pds.get(i);
            BigDecimal netTotal = new BigDecimal(pd.getPd_Net_Total());
            grandTotal = grandTotal.add(netTotal);
        }
        return  grandTotal;
    }

    /**
     * TODO:不是直接顯示ID，要顯示PaymentTerm表中的Name字段
     * TODO: 如何显示
     * 1. 重点在处理各种ID和Name  ：
     *    a.AccountID --> Accounts.properties
     *    b.ProductID --> Products.properties
     *    c.UserID --> zohoUser.properties
     *    d.PaytermID --> payterm.properties
     * 2. 重点在Product Detail的处理：处理多个Product和1个Product情况
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public synchronized static List getSOMap(boolean isSepatateRun,String sqlWithErpIDs) throws Exception{
        List dbModuleList = new ArrayList();
        Map<String,String> dbIDEditTimeMap = new HashMap<String, String>();
        Map<String,Object> dbIDModuleObjMap = new TreeMap<String, Object>(new Comparator<String>(){

            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(String o1, String o2) {
                //按照Key ： ERP ID倒序排序
                //return o1.compareTo(o2);
                return StringUtils.nullToInt(o2) - StringUtils.nullToInt(o1);
            }
        });
        dbModuleList.add(0,dbIDModuleObjMap);
        dbModuleList.add(1,dbIDEditTimeMap);
        List<SO> moduleList = new ArrayList<SO>();
        //String sql = "select s1.soid , s1.cusName,s2.soid ,s2.Item_SOID,s2.ItemID,s2.ItemName from  item_SO s2  left join  SO s1 on s1.soid = s2.SOID";
        //sql = "select s1.soid , s1.cusName,s2.Item_SOID,s2.ItemID,s2.ItemName, item.Name as itemName\n" +
        //        " from SO s1 \n" +
        //        "left join  item_SO s2  on s1.soid = s2.SOID\n" +
        //        "left join  item item  on s2.itemid = item.itemid\n" +
        //        " order by s1.soid\n";
        //sql = "SELECT so.SOID AS ERPID, so.CUSNAME AS CUSTOMERNAME, so.EXCHANGERATE AS EXGRATE ,\n" +
        //        "itemso.ITEM_SOID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, itemso.SOPRICE AS PROD_UNITPRICE,  itemso.QUANTITY AS PROD_QUANTITY, itemso.ITEMDISCOUNT AS PROD_DISCOUNT, itemso.DESCRIPTION AS PROD_DESC , \n" +
        //        "so.*\n" +
        //        " FROM SO so\n" +
        //        "LEFT JOIN  ITEM_SO itemso  ON so.SOID = itemso.SOID\n" +
        //        "LEFT JOIN  ITEM item  ON itemso.itemid = item.ITEMID\n" +
        //        "where 1 = 1 \n" +
        //        //"and so.SOID in (13,16,27)\n" +//暂时只用三条数据
        //        "and item.ITEMID is not null \n" +
        //        " ORDER BY SO.SOID";
        String sql = getWholeSqlWithFilterUser(SO_SQL,ModuleNameKeys.SalesOrders.toString()," ORDER BY zoho.SOID DESC");
        if(isSepatateRun){
            sql = sqlWithErpIDs;
        }
        ResultSet rs = exeQuery(sql);
        String preErpID = "";
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        SO so = null;
        while (rs != null && rs.next()){
            String lastEditBy = StringUtils.nullToString(rs.getString("LatestEditBy"));
            //if(containERPAcct(lastEditBy)){
                /**DB中的SO id*/
                String curErpID = StringUtils.nullToString(rs.getString("SOID"));
                //相同SO，代表这个SO有多个Product，不需要新创建SO，只需要把product添加到已有的List<ProductDetails> pds中
                if(preErpID.equals(curErpID) && so != null){
                    /**
                     * 处理list<ProductDetail>, 根据db中的SOID关联Item_SO表，拿出所有的product Detail,注意为空情况
                     Double.valueOf(so.getErpExchangeRate())
                     */
                    ProductDetails productDetails = assembleProduct(rs);
                    //如果 因为Prod ID造成ProductDetails为空的情况，忽略这条product
                    if( null != productDetails){
                        pds.add(assembleProduct(rs));
                        so.setPds(pds);
                    }
                } else{//代表不同SO,需要重新创建SO对象
                    //在新一条数据之前，判断如果上一条数据product detail是空的话 , 那么需要移除上一条记录
                    if(so == null || so.getPds() == null || so.getPds().size() > 0){
                        //TODO
                        dbIDModuleObjMap.remove(curErpID);
                        dbIDEditTimeMap.remove(curErpID);
                    }
                    //因为erpid 不相同之前，先把上一个SO添加到list中（排除第一条数据对第一次遍历SO）
                    if(null != so )moduleList.add(so);
                    //因为是不同的SO对象了，所以把SO添加到list之后，需要重新创建SO和Product对象, 并且把preErpID指向当前的ERP ID
                    so = new SO();
                    pds = new ArrayList<ProductDetails>();
                    preErpID = curErpID;
                    so.setErpID(curErpID);
                    //TODO 销售编号与SoNumber需要确认,不需要，这个字段是ZOHO ID,从配置文件中获取
    //                so.setSALESORDERID(rs.getString("ItemRef"));
                    //TODO 销售拥有者
            //        so.setOwerID("85333000000071039");
            //        so.setOwner("qq");
//                    User user = new User("80487000000076001","marketing");
                    User user = new User(ConfigManager.getZohoUserfromProps(lastEditBy),lastEditBy);
                    so.setUser(user);
                    String soNumber =StringUtils.nullToString(rs.getString("SORef"));
                    /**SONumber 和Subject一致*/
                    so.setSubject(soNumber);
                    so.setSoNumber(soNumber);
                    /**
                     * TODO
                     * Account id 和Name一般是同时存在的；
                     * 如果只存在id，Name可以不对；
                     * 如果只存在Name，那么会新创建一个客户；
                     * 例外：但如果有ID，那么ID必需存在已经创建的客户中
                     */
                     String erpAcctID = StringUtils.nullToString(rs.getString("CustomerID"));
                     String zohoAcctID = ConfigManager.getAcctsfromProps(erpAcctID);
                    //logger.debug(" [ getSOMap],ERP ID = "+curErpID+", CustomerID ="+erpAcctID+", ZOHO Account ID="+zohoAcctID);
                    boolean isUserExist = checkUserAndAcctExist(erpAcctID,zohoAcctID,user,
                            curErpID,lastEditBy,ModuleNameKeys.SalesOrders.toString());
                    if(!isUserExist){
                        continue;
                    }
                    //TODO：CONFIRM， 如果找不到AccoutnID,则默认为空
                    if(!StringUtils.isEmptyString(zohoAcctID)){
                        so.setACCOUNTID(zohoAcctID);
                    }else{
                        //TODO：CONFIRM  如果找不到Accounts ID， 那么用为空
                    }
                    //so.setACCOUNTID(zohoAcctID);//"80487000000096005"
                    so.setCustomerNO(StringUtils.nullToString(rs.getString("CusRef")));
                    /**
                     * 客户名Account Name：PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
                     */
                    //so.setAcctName(rs.getString("CusName"));//"永昌紙品"
                    //报价名称（查找类型）
                    so.setQuoteNO(StringUtils.nullToString(rs.getString("QuoteRef")));
                    //客户名ERP_Currency,DB中用CurrencyName表示
                    so.setErpCurrency(StringUtils.nullToString(rs.getString("CurrencyName")));
                    //公司联络人Contact
                    so.setContact(StringUtils.nullToString(rs.getString("CusContact")));
                    //客户邮件地址
                    so.setMailAddress(StringUtils.nullToString(rs.getString("CusMailAddress")));
                    so.setEmail(StringUtils.nullToString(rs.getString("CusEmail")));
                    //客户PONo
                    so.setPoNO(StringUtils.nullToString(rs.getString("CustomerPONo")));
                    so.setDeliveryAddress(rs.getString("CusDeliveryAddress"));
                    so.setTel(StringUtils.nullToString(rs.getString("CusTel")));
                    so.setFax(StringUtils.nullToString(rs.getString("CusFax")));
                    BigDecimal exchangeRate = rs.getBigDecimal("ExchangeRate");
                    so.setErpExchangeRate(exchangeRate.toString());
                    /**TODO:不是直接顯示ID，要顯示PaymentTerm表中的Name字段  PaymentTermID       */
                    so.setPaymentTerm(StringUtils.nullToString(rs.getString("PayTerm")));
                    so.setPayMethod(StringUtils.nullToString(rs.getString("PayMethod")));
                    so.setDeliveryMethod(StringUtils.nullToString(rs.getString("DeliveryMethod")));
                    so.setPaymentPeriod(StringUtils.nullToString(rs.getString("PaymentPeriod")));
                    //销售订单日期SODate
                    String SODate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("SODate"));
                    so.setErpDueDate(SODate);
//                    boolean isDevMod  = "1".equals(ConfigManager.get(Constants.PROPS_ZOHO_FILE,Constants.ZOHO_PROPS_DEV_MODE));
//                    String latestEditTime = ConfigManager.isDevMod() ? ThreadLocalDateUtil.formatDate(new Date()):ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                    String latestEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
//                    String currentDate = ThreadLocalDateUtil.formatDate(new Date());
                    so.setLatestEditTime(latestEditTime);
//                    so.setCreationTime("2016-09-01 10:10:10");
                    so.setLatestEditBy(user.getUserName());

                    /**
                     * TODO  , 需要计算： 待定
                     * 设置product detail右下角那堆属于SO的字段:Discount,Sub Total,Grand Total
                     */
                    so.setDiscount("0");
                    so.setSubTotal("0");//小计
                    so.setGrandTotal("0");//累计
                    //Customer Discount == Discount
                    BigDecimal cusDiscount = rs.getBigDecimal("CusDiscount");
                    so.setCusDiscount(StringUtils.nullToString(cusDiscount.multiply(exchangeRate)));
                    /**Discount来自销售订单中的“折扣” */
                    so.setDiscount(StringUtils.nullToString(cusDiscount.multiply(exchangeRate)));
                    /**Sub Total 来自销售订单中的“小计”*/
                    //TODO : 小计-->估计是total,但是在SO中没有Total，那么就需要自己去计算
                    //so.setSubTotal(String.valueOf(total.multiply(exgRate)));
                    /**Grand Total来自销售订单中的“累计”*/
                    //TODO : 累计-->估计应该是total-折扣
                    //so.setGrandTotal(String.valueOf(total.multiply(exgRate).subtract(cusDiscount)));
                    //处理第一条Product
                    ProductDetails productDetails = assembleProduct(rs);
                    //如果 因为Prod ID造成ProductDetails为空的情况，忽略这条product
                    if( null != productDetails){
                        pds.add(productDetails);
                        so.setPds(pds);
                        dbIDModuleObjMap.put(curErpID,so);
                        dbIDEditTimeMap.put(curErpID,latestEditTime);
                    }
                //}
            }
        }
        setGrandTotal(ModuleNameKeys.SalesOrders.toString(),dbIDModuleObjMap);
        return dbModuleList;
    }

    /**
     *  检测当前用户和客户是否存在与ZOHO
     * @param erpAcctID   DB Account ID
     * @param zohoAcctID  根据erpAcctID得到的ZOHO中的Account ID
     * @param user      ZOHO User ID
     * @param curErpID    当前模块ID
     * @param lastEditBy    最后更新人
     * @param module      Module Name
     * @return
     */
    private static boolean checkUserAndAcctExist(String erpAcctID,String zohoAcctID, User user,
                           String curErpID, String lastEditBy, String module) {
        boolean exist = true;
        //if(StringUtils.isEmptyString(zohoAcctID)|| StringUtils.isEmptyString(user.getUserID())){
        if(StringUtils.isEmptyString(user.getUserID())){
            //for Accounts only check the User
            if(ModuleNameKeys.Accounts.toString().equals(module) && StringUtils.isEmptyString(user.getUserID())){
                exist = false;
            }else{
                //for Quotes/SO/Invoice ,  need check Account ID and User at the same time
                exist = false;
            }
            // for Product no need to check ,  because all product has import
        }
        if(!exist){
            logger.debug("\n [DBUtils: "+module+"]======>IGNORE 不存在:::  "
                    + "\n Because of "
                    //" the DB Account ID[" + erpAcctID + "] not exist in ZOHO Account module, zohoAcctID is empty[" +StringUtils.isEmptyString(zohoAcctID)+"] "
                    //+ "\n or "
                    + "User ["+lastEditBy+"] not exist in ZOHO User, User["+user.getUserID()+":" +user.getUserName()+"] is empty["+StringUtils.isEmptyString(user.getUserID())+"]"
                    + "\n Current Module["+module+"] ERP ID = " + curErpID + ", Customer ID =" + erpAcctID + ", ZOHO Account ID=" + zohoAcctID +", Last Edit By="+lastEditBy);
        }
        return exist;
    }


    /**
     * TODO:不是直接顯示ID，要顯示PaymentTerm表中的Name字段
     * TODO: 如何显示
     * 1. 重点在处理各种ID和Name  ：
     *    a.AccountID --> Accounts.properties
     *    b.ProductID --> Products.properties
     *    c.UserID --> zohoUser.properties
     *    d.PaytermID --> payterm.properties
     *    e. SO -->把这个改成Input
     * 2. 重点在Product Detail的处理：处理多个Product和1个Product情况
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public synchronized static List getInvoiceMap(boolean isSepatateRun,String sqlWithErpIDs) throws Exception{
        List dbModuleList = new ArrayList();
        Map<String,String> dbIDEditTimeMap = new HashMap<String, String>();
        Map<String,Object> dbIDModuleObjMap = new TreeMap<String, Object>(new Comparator<String>(){

            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(String o1, String o2) {
                //按照Key ： ERP ID倒序排序
                //return o1.compareTo(o2);
                return StringUtils.nullToInt(o2) - StringUtils.nullToInt(o1);
            }
        });
        dbModuleList.add(0,dbIDModuleObjMap);
        dbModuleList.add(1,dbIDEditTimeMap);
//        List<Invoices> moduleList = new ArrayList<Invoices>();
//        String sql = "SELECT inv.InvoiceID AS ERPID, inv.CUSNAME AS CUSTOMERNAME, inv.EXCHANGERATE AS EXGRATE ,\n" +
//                "item_inv.Item_InvoiceID,item.ITEMID AS PROD_ID, item.NAME AS PROD_NAME, item_inv.InvoicePrice AS PROD_UNITPRICE,  item_inv.QUANTITY AS PROD_QUANTITY, item_inv.ITEMDISCOUNT AS PROD_DISCOUNT, item_inv.DESCRIPTION AS PROD_DESC , \n" +
//                "inv.*\n" +
//                " FROM Invoice inv\n" +
//                "LEFT JOIN  ITEM_INVOICE item_inv  ON inv.InvoiceID = item_inv.InvoiceID\n" +
//                "LEFT JOIN  ITEM item  ON item_inv.itemid = item.ITEMID\n" +
//                "where 1=1 and " +
//                //" inv.InvoiceID in (8,12,145)\n" +
//                "  item.ITEMID is not null \n" + //暂时只用三条数据
//                " ORDER BY inv.InvoiceID";
        String sql = getWholeSqlWithFilterUser(INVOICE_SQL,ModuleNameKeys.Invoices.toString(), " ORDER BY zoho.InvoiceID DESC");
        if(isSepatateRun){
            sql = sqlWithErpIDs;
        }
        ResultSet rs = exeQuery(sql);
        String preErpID = "";
        Invoices invoices = null;
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        while (rs != null && rs.next()){
            String lastEditBy = StringUtils.nullToString(rs.getString("LatestEditBy"));
            //if(containERPAcct(lastEditBy)){
                String curErpID = StringUtils.nullToString(rs.getString("InvoiceID"));
                //相同ERPID，代表这个Module有多个Product，不需要新创建Module，只需要把product添加到已有的List<ProductDetails> pds中
                if(preErpID.equals(curErpID) && invoices != null){
                    /**
                     * 处理list<ProductDetail>, 根据db中的SOID关联Item_SO表，拿出所有的product Detail,注意为空情况
                     Double.valueOf(so.getErpExchangeRate())
                     */
                    ProductDetails productDetails = assembleProduct(rs);
                    //如果 因为Prod ID造成ProductDetails为空的情况，忽略这条product
                    if( null != productDetails){
                        pds.add(productDetails);
                        invoices.setPds(pds);
                    }
                } else{//代表不同SO,需要重新创建SO对象
                    // 上一条数据
                    //如果没有product detail , 那么需要移除整条记录
                    if(invoices == null || invoices.getPds() == null || invoices.getPds().size() > 0){
                        //TODO
                        dbIDModuleObjMap.remove(curErpID);
                        dbIDEditTimeMap.remove(curErpID);
                    }
                    //erpid 不相同之前，先把上一个Module添加到list中（排除第一条数据对第一次遍历Module）
                    //因为是不同的SO对象了，所以把SO添加到list之后，需要重新创建Module和pds对象, 并且把preErpID指向当前的ERP ID
                    invoices = new Invoices();
                    pds = new ArrayList<ProductDetails>();
                    preErpID = curErpID;
                    /**DB中的Invoices id*/
                    invoices.setErpID(curErpID);
                    //Subject主题
                    String invoiceNumber = StringUtils.nullToString(rs.getString("InvoiceRef"));
                    invoices.setInvoiceSubject(invoiceNumber);
                    invoices.setInvoiceNumber(invoiceNumber);
                    //TODO 1. 注意拥有者User一定要存在系统中  , 发票拥有者Invoice Owner
//                    User user = new User("80487000000076001","marketing");
                    User user = new User(ConfigManager.getZohoUserfromProps(lastEditBy),lastEditBy);
    //            User user = fetchDevUser(false);
                    invoices.setUser(user);
    //            Invoice Date发货单日期
                    String invoiceDate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("InvoiceDate"));
                    invoices.setInvoiceDate(invoiceDate);
                    /**
                     * TODO：2. 注意SO中的SALESORDERID与Sales Order一定要存在系统
                     * 是否只需要SONo  暂时改成input，如果客户实在要求，则也需要把这个写入到一个文件中
                     */
                    //so id 和SO name
                    //invoices.setSALESORDERID(rs.getString("PaymentPeriod"));
                    invoices.setSoName(StringUtils.nullToString(rs.getString("SORef")));
                    //汇率
                    BigDecimal exgRate = rs.getBigDecimal("ExchangeRate");
                    invoices.setErp_ExchangeRate(StringUtils.nullToString(exgRate));
                    /**TODO 不是直接顯示ID，要顯示PaymentTerm表中的Name字段   PaymentTermID      */
                    invoices.setPaymentTerm(StringUtils.nullToString(rs.getString("PayTerm")));
                    invoices.setCustomerNo(rs.getString("CusRef"));
                    //TODO 似乎没用到Due Date到期日期
                    String dueDate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("invoiceDate"));
                    invoices.setDueDate(dueDate);
                    /**
                     * 3. 注意Account一定要存在系统,需要取ZOHO Acct ID
                     * Account id 和Name一般是同时存在的；
                     * 如果只存在id，Name可以不对；
                     * 如果只存在Name，那么会新创建一个客户；
                     * 例外：但如果有ID，那么ID必需存在已经创建的客户中
                     */
                    String erpAcctID = StringUtils.nullToString(rs.getString("CustomerID"));
                    String zohoAcctID = ConfigManager.getAcctsfromProps(erpAcctID);
                    //logger.debug("[ Invoices ], ERP ID = "+curErpID+", CustomerID ="+rs.getString("CustomerID")+", ZOHO Account ID="+zohoAcctID);

                    boolean isUserExist = checkUserAndAcctExist(erpAcctID,zohoAcctID,user,
                            curErpID,lastEditBy,ModuleNameKeys.Invoices.toString());
                    if(!isUserExist){
                        continue;
                    }
                    //TODO：CONFIRM， 如果找不到AccoutnID,则默认为空
                    if(!StringUtils.isEmptyString(zohoAcctID)){
                        invoices.setACCOUNTID(zohoAcctID);
                        invoices.setAcctName(StringUtils.nullToString(rs.getString("CusName")));
                    }else{
                        //TODO：CONFIRM  如果找不到Accounts ID， 那么用为空
                    }
                    //Contact公司聯絡人
                    invoices.setContact(StringUtils.nullToString(rs.getString("CusContact")));
                     invoices.setEmail(StringUtils.nullToString(rs.getString("CusEmail")));
                    //DeliveryAddress客户发货地址
                    invoices.setDeliveryAddress(StringUtils.nullToString(rs.getString("CusDeliveryAddress")));
                    //客户邮寄地址
                    invoices.setMailAddress(StringUtils.nullToString(rs.getString("CusMailAddress")));
                    invoices.setFax(StringUtils.nullToString(rs.getString("CusFax")));
                    invoices.setTel(StringUtils.nullToString(rs.getString("CusTel")));
                    invoices.setErp_Currency(StringUtils.nullToString(rs.getString("CurrencyName")));
                    //付款方式
                    invoices.setPayMethod(StringUtils.nullToString(rs.getString("PayMethod")));
                    //deliveryMethod
                    invoices.setDeliveryMethod(StringUtils.nullToString(rs.getString("DeliveryMethod")));
                    //客户PONo
                    invoices.setPoNO(StringUtils.nullToString(rs.getString("CustomerPONo")));
                    //DNNo 送貨單編號
                    invoices.setDnNo(StringUtils.nullToString(rs.getString("DNNo")));
                    /**TODO Deposit訂金
                     * 匯入時x ExchangeRate換成港幣
                     * */
                     BigDecimal deposit = rs.getBigDecimal("Deposit");
                     invoices.setDeposit(StringUtils.nullToString(deposit.multiply(exgRate)));
                    /**其他费用
                     *   匯入時x ExchangeRate換成港幣
                     * */
                    BigDecimal otherCharge = rs.getBigDecimal("OtherCharge");
                    invoices.setOtherCharge(StringUtils.nullToString(otherCharge.multiply(exgRate)));
                    /**FreightAmount 运费
                     * 匯入時x ExchangeRate換成港幣
                     * */
                    BigDecimal freightAmount = rs.getBigDecimal("FreightAmount");
                    invoices.setFreightAmount(StringUtils.nullToString(freightAmount.multiply(exgRate)));
                    /**
                     * 匯入時x ExchangeRate換成港幣
                     */
                    BigDecimal total = rs.getBigDecimal("Total");
                    invoices.setTotal(StringUtils.nullToString(total.multiply(exgRate)));
//                    boolean isDevMod  = "1".equals(ConfigManager.get(Constants.PROPS_ZOHO_FILE,Constants.ZOHO_PROPS_DEV_MODE));
//                    String latestEditTime = ConfigManager.isDevMod() ? ThreadLocalDateUtil.formatDate(new Date()):ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                    String latestEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
//                    String lastEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
                    invoices.setLatestEditTime(latestEditTime);
    //            String dueDate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("invoiceDate"));
    //            invoices.setCreationTime("2016-09-01 10:10:10");
                    invoices.setLatestEditBy(user.getUserName());


                    /**
                     * 设置product detail右下角那堆属于Invoices的字段:Discount,Sub Total,Grand Total
                     */
                    /**
                     * TODO : 是百分比还是已经是计算出来的数值，暂时按数值计算
                     * Customer Discount客户折扣来自Customer的折扣  实际上Discount == cusDiscount
                     * */
                    BigDecimal cusDiscount = rs.getBigDecimal("CusDiscount");
                    invoices.setCusDiscount(StringUtils.nullToString(cusDiscount.multiply(exgRate)));
                    /**Discount来自销售订单中的“折扣” */
                   invoices.setDiscount(StringUtils.nullToString(cusDiscount.multiply(exgRate)));
                    /**Sub Total 来自销售订单中的“小计”*/
                    //TODO : 小计-->估计是total
                    invoices.setSubTotal(StringUtils.nullToString(total.multiply(exgRate)));
                    /**Grand Total来自销售订单中的“累计”*/
                    //TODO : 累计-->估计应该是total-折扣
                    invoices.setGrandTotal(StringUtils.nullToString(total.multiply(exgRate).subtract(cusDiscount)));

                    //处理第一条Product
                    ProductDetails productDetails = assembleProduct(rs);
                    //如果 因为Prod ID造成ProductDetails为空的情况，忽略这条product
                    if( null != productDetails){
                        pds.add(productDetails);
                        invoices.setPds(pds);
                        dbIDModuleObjMap.put(curErpID,invoices);
                        dbIDEditTimeMap.put(curErpID,latestEditTime);
                    }
                //}
            }

        }
        return dbModuleList;
    }

    /**
     *  根据db中的SOID关联Item_SO表，拿出所有的product Detail,这里假设找到2条数据
     * @param rs
     * @return
     */
    private static ProductDetails assembleProduct(ResultSet rs) throws Exception {
//        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        ProductDetails pd =new ProductDetails();
        //ERP ID -->某条数据的DB ID
        String erpID = StringUtils.nullToString(rs.getString("erpID"));
        //PROD_ID-->某条数据中产品的ID
        String prodID = ConfigManager.getProdfromProps(StringUtils.nullToString(rs.getString("PROD_ID")));
        //TODO 当Prod ID或者AccountsID或者userID为空时， throw exception，因为就算发到ZOHO也是会出错
        if(StringUtils.isEmptyString(prodID)){
            //TODO 如果真的存在Prod ID为空， 需要remove这个product，如果这个模块中product仅仅只有1个，那么remove整条模块数据
            logger.debug("#### [ assembleProduct：ignore], 因为Prod ID在Product中不存在，所以忽略这条Product，"
                    +Constants.COMMENT_PREFIX+"原ProdID = "+rs.getString("PROD_ID")+", ERP ID = "+erpID);
            return null;
        }
        String prodName = StringUtils.nullToString(rs.getString("PROD_NAME"));
        BigDecimal unitPrice = rs.getBigDecimal("PROD_UNITPRICE") == null ? new BigDecimal(0): rs.getBigDecimal("PROD_UNITPRICE") ;
        BigDecimal exchangeRate = rs.getBigDecimal("PROD_UNITPRICE") == null ? new BigDecimal(0): rs.getBigDecimal("EXGRATE");
        //通过exchangerate转化后的单价（定价）
        BigDecimal realUnitPrice_dec = unitPrice.multiply(exchangeRate);
        String realUnitPrice = StringUtils.nullToString(realUnitPrice_dec);
        String listPrice = StringUtils.nullToString(realUnitPrice_dec);
        BigDecimal quantity = rs.getBigDecimal("PROD_QUANTITY");
        BigDecimal itemDiscount = rs.getBigDecimal("PROD_DISCOUNT");
        String prodDesc = rs.getString("PROD_DESC");
        //金额 = 数量 * 单价（定价）
        BigDecimal total = quantity.multiply(realUnitPrice_dec);
        //总计 = 金额 - 金额 * 折扣（因为折扣是百分比）
        BigDecimal netTotal = total.subtract(total.multiply(itemDiscount));
        logger.debug("打印Product Detail：ERPID="+erpID+", prodID = "+prodID+", prodName = "+prodName +", unitPrice=" +
                ""+unitPrice+", exchangeRate="+exchangeRate+", realUnitPrice="+realUnitPrice+
                ", listPrice= "+listPrice+", quantity="+quantity+", itemDiscount="+itemDiscount+
                ", prodDesc = "+prodDesc+", total="+total+", netTotal="+netTotal);
        /**
         * 注意 product id和Name一定要是已经存在与产品里面的,item ID 和ItemName
         */
        pd.setPd_Product_Id(prodID);
        //产品名字Product Name
        //TODO 需要找ken确认ItemName为 [ Name]是表示什么意思？是否根据id从Item表找--》Name
        pd.setPd_Product_Name(prodName);
        //定价 (￥)：单价Unit Price
        pd.setPd_Unit_Price(realUnitPrice);//定价  ： DB-->SOPrice,注意价格要跟Currency一致
        //List Price单价
        pd.setPd_List_Price(listPrice);//单价  ： DB-->SOPrice,注意价格要跟Currency一致
        //数量
        pd.setPd_Quantity(StringUtils.nullToString(quantity.toString()));//数量
        pd.setPd_Discount(StringUtils.nullToString(itemDiscount));//折扣
        //税，Matrix默认这个字段是0，因为不用税
        pd.setPd_Tax("0");
        pd.setPd_Product_Description(prodDesc);
        //金额 = 数量*定价
        pd.setPd_Total(StringUtils.nullToString(total));//金额
        //总计=金额-折扣(金额*%折扣)
        pd.setPd_Net_Total(StringUtils.nullToString(netTotal));//总计
        return pd;
    }



    private static ResultSet exeQuery(String sql) throws SQLException, IOException, ConfigurationException, ClassNotFoundException {
        ResultSet rs = null;
        Connection conn = getConnection();
        logger.info(" [ exeQuery], sql =\n[ "+sql+" ]");
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            logger.error("exeQuery出现错误",e);
            throw e;
        }
        return rs;
    }
    public static ResultSet exeQuery(String sql,List list) throws SQLException, IOException, ConfigurationException, ClassNotFoundException {
        ResultSet rs = null;
        Connection conn = getConnection();
        logger.info(" [ exeQuery], sql =\n[ "+sql+" ]");
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            setPSParams(ps,list);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            logger.error("exeQuery出现错误",e);
            throw e;
        }
        return rs;
    }

    /**
     * 更新ERP report 表
     * @param sql
     * @param list
     * @throws SQLException
     * @throws IOException
     * @throws ConfigurationException
     */
    public static void exeUpdReport(String sql,List list) throws SQLException, IOException, ConfigurationException, ClassNotFoundException {
        ResultSet rs = null;
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
              //
//            ps.setDate(1,new java.sql.Date(new Date().getTime()));
            setPSParams(ps,list);
//            ps.setTimestamp(1, new Timestamp(new Date().getTime()));//"getdate()");//
//            ps.setInt(2,1);
//            ps.setInt(3,2);
//            ps.setInt(4,4);
//            ps.setString(5,"Accounts");
             ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("exeUpd出现错误, sql="+sql,e);
        }
    }

    /**
     * 更新ERP report 表
     * @param sql
     * @param list
     * @throws SQLException
     * @throws IOException
     * @throws ConfigurationException
     */
    public static List<Report> fetchReportByTime(String sql,List list) throws Exception {
        ResultSet rs = null;
        //ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
        Connection conn = getConnection();
        List<Report> reports = new ArrayList<Report>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            setPSParams(ps,list);
            rs = ps.executeQuery();

            while (rs.next()){
                Report report = new Report();
                report.setREPORTID(rs.getString("REPORTID"));
                String startTime  = ThreadLocalDateUtil.formatDate(rs.getTimestamp("START_TIME"));
                String endTime  = ThreadLocalDateUtil.formatDate(rs.getTimestamp("END_TIME"));
                report.setSTART_TIME(startTime);
                report.setEND_TIME(endTime);
                report.setINS_FAILED(rs.getString("INS_FAILED"));
                report.setUPD_FAILED(rs.getString("UPD_FAILED"));
                report.setDEL_FAILED(rs.getString("DEL_FAILED"));
                report.setWHOLEFAIL(rs.getString("WHOLEFAIL"));
                reports.add(report);
            }

        } catch (SQLException e) {
            logger.error("exeUpd出现错误, sql="+sql,e);
        }
        return reports;
    }

    /**
     * 通用的设置PreparedStatement的功能
     * @param ps
     * @param list
     * 问题：为什么java.sql.Date类型的数据要设置Timestamp类型：如果设置Date类型，那么会损失精度
     *  java.sql.date与java.util.date区别以及数据库中插入带时分秒的时间
     * http://blog.csdn.net/tanqian351/article/details/51684006
     * @throws SQLException
     */
    private static void setPSParams(PreparedStatement ps, List list) throws SQLException {
        for(int  i = 0; i< list.size(); i++){
            Object o  = list.get(i);
            String objectType = list.get(i).getClass().getName();
            if( "java.lang.Integer".equals(objectType)){
                ps.setInt(i+1, StringUtils.nullToInt(o));
            }else if("java.lang.String".equals(objectType)){
                ps.setString(i + 1, StringUtils.nullToString(o));
            }else  if("java.util.Date".equals(objectType)){
                ps.setTimestamp(i + 1, new Timestamp(((Date)o).getTime()));
                //ps.setTimestamp(i + 1, (Timestamp)o);
            }else{//默认设为string
                ps.setString(i + 1, StringUtils.nullToString(o));
            }

            //TODO BigDecemal,
//                if( o instanceof  Integer){
//
//                }else if(o instanceof String){
//
//                }else  if(o instanceof  Date){
//
//                }
        }
    }
}
