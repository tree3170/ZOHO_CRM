/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   DBUtils.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util;

import darlen.crm.model.result.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * darlen.crm.util
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
    public static Map<String,String> zohoDBPropsMap = new HashMap<String, String>();

    /**
     * 初始化ZOHO配置文件中的一些字段值
     */
    private synchronized static void initZohoProps() {
        try {
            Properties prop = new Properties();
            prop.load(DBUtils.class.getResourceAsStream("/secure/db.properties"));
            //for url TODO  将来需要把properties中的字段全部放入到Cache或者静态变量中
            for(Map.Entry entry : prop.entrySet()){
                zohoDBPropsMap.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
        } catch(IOException e) {
            logger.error("读取zoho properties出现错误", e);
        }
        CommonUtils.printMap(zohoDBPropsMap,"db.properties的value：：：");
    }

    /**
     * 获取DB连接
     */
    public static Connection getConnection (){
        String driverName = zohoDBPropsMap.get("DB_DRIVER_NAME");//"com.microsoft.sqlserver.jdbc.SQLServerDriver";  //加载JDBC驱动
        String dbURL =zohoDBPropsMap.get("DB_URL"); //"jdbc:sqlserver://localhost:1433; DatabaseName=test";  //连接服务器和数据库test
        String userName = zohoDBPropsMap.get("DB_USERNAME");;//"sa";  //默认用户名
        String userPwd = zohoDBPropsMap.get("DB_PWD");//"zaq1@WSX";  //密码
        Connection dbConn = null;

        try {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
            logger.debug("Connection Successful!");  //如果连接成功 控制台输出Connection Successful!
        } catch (Exception e) {
            logger.error("连接SQL SERVER失败",e);
        }
        return dbConn;
    }

    public static void main(String[] args) throws Exception {
        DBUtils.initZohoProps();
//        CommonUtils.printList(getAccountList(), "打印所有的Customer：：：");
        CommonUtils.printList(getProductList(), "打印所有的 Products：：：");
    }

    /**
     * TODO
     * 1. 在此之前，需要先过滤13个账户
     * 2. 从Cache中拿LastEditBy的Name和ID
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    private static List<Accounts> getAccountList() throws SQLException, ParseException {
        List<Accounts> accountList = new ArrayList<Accounts>();
        String sql = "select * from dbo.Customer ";
        ResultSet rs = exeQuery(sql);
        while (rs != null && rs.next()){
            Accounts account = new Accounts();
            //
            account.setErpID(rs.getString("CustomerID"));
            //TODO:从Cache中拿到Name，并查到ID，然后填上ID和Name作为拥有者设入。如果Name改变了怎么办
            //User --> lastEditBy一致
            //User --> lastEditBy一致
            User user = new User("80487000000076001","marketing");
            account.setUser(user);
            account.setCustomerNO(rs.getString("CustomerRef"));
            account.setAcctName(rs.getString("Name"));
            //TODO: 数据库中Enable是1或者0，但是在ZOHO中是true或者false，需要转换下
            //JDBC BIT 类型的 Java 映射的推荐类型是 Java 布尔型
            boolean enable = rs.getBoolean("Enabled");
            account.setEnabled(String.valueOf(enable));
            account.setPhone(rs.getString("Tel"));
            account.setFax(rs.getString("Fax"));
            account.setContact(rs.getString("Contact"));
            //聯絡人直線电话
            account.setDirect(rs.getString("Direct"));
            account.setDeliveryAddress(rs.getString("DeliveryAddress"));
            account.setEmail(rs.getString("Email"));
            account.setMailAddress(rs.getString("MailAddress"));
            account.setWebsite(rs.getString("Website"));
            //忽略
            account.setCountryID(rs.getString("CountryID"));
            account.setState(rs.getString("state"));
            account.setPostNo(rs.getString("PostNo"));
            account.setCity(rs.getString("City"));
            account.setDeliveryMethod(rs.getString("DeliveryMethod"));
            account.setRemark(rs.getString("Remark"));
            account.setCreationTime(rs.getString("CreationTime"));
            String creationTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("CreationTime"));
            String lastEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
            account.setCreationTime(creationTime);
            account.setLatestEditTime(lastEditTime);
            account.setLatestEditBy(rs.getString("LatestEditBy"));
            accountList.add(account);
        }
        return accountList;
    }

    private static List<Products> getProductList() throws SQLException, ParseException {
        List<Products> productsList = new ArrayList<Products>();
        String sql = "select * from dbo.item ";
        ResultSet rs = exeQuery(sql);
        while (rs != null && rs.next()){
            Products product = new Products();
            product.setErpID(rs.getString("ItemID"));
            // Product Name 产品名称
            product.setProdName(rs.getString("Name"));
            //Product Code产品编码,暂时用不到
            product.setProdCode(rs.getString("ItemRef"));
            //设置Product Owner产品拥有者：与lastEditBy一致
            //TODO：Product Owner产品所有者 ，就是DB中的lastEditBy
            User user = new User();
            user.setUserID("85333000000071039");
            user.setUserName("qq");
            //User user = fetchDevUser(false);
            product.setUser(user);
            //是否隱藏客户
            product.setEnabled(rs.getString("Enabled"));
            //产品分类
            product.setCatagory(rs.getString("Catagory"));
            //产品子分类
            product.setSubCategory(rs.getString("SubCategory"));
            //ItemDesc产品描述
            product.setItemDesc(rs.getString("Description"));
            //Unit单位
            product.setUnit(rs.getString("Unit"));
            //barcode條碼
            product.setBarcode(rs.getString("Barcode"));
            product.setRemark(rs.getString("Remark"));
            product.setLatestEditBy(user.getUserName());
            String latestEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
            product.setLatestEditTime(latestEditTime);
            productsList.add(product);
        }
        logger.debug("Product size:::" + productsList.size());
        return productsList;
    }

    /**
     * TODO:不是直接顯示ID，要顯示PaymentTerm表中的Name字段
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    private static List<SO> getSOList() throws SQLException, ParseException{
        List<SO> soList = new ArrayList<SO>();
        String sql = "select * from dbo.SO ";
        ResultSet rs = exeQuery(sql);
        while (rs != null && rs.next()){
            SO so = new SO();
            /**DB中的SO id*/
            so.setErpID(rs.getString("ItemID"));
            //TODO 销售编号与SoNumber需要确认
            so.setSALESORDERID(rs.getString("ItemRef"));
            //TODO 销售拥有者
    //        so.setOwerID("85333000000071039");
    //        so.setOwner("qq");
            User user = new User("80487000000076001","marketing");
            so.setUser(user);
            so.setSubject(rs.getString("SORef"));
            /**ZOHO生成的字段，似乎没什么作用*/
            so.setSoNumber("ItemRef");
            /**
             * TODO
             * Account id 和Name一般是同时存在的；
             * 如果只存在id，Name可以不对；
             * 如果只存在Name，那么会新创建一个客户；
             * 例外：但如果有ID，那么ID必需存在已经创建的客户中
             */
            so.setACCOUNTID(rs.getString("CustomerID"));//"80487000000096005"
            /**
             * 客户名Account Name：PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
             */
            so.setAcctName(rs.getString("CusName"));//"永昌紙品"
            //报价名称（查找类型）
            so.setQuoteNO(rs.getString("QuoteRef"));
            //客户名ERP_Currency,DB中用CurrencyName表示
            so.setErpCurrency(rs.getString("CurrencyName"));
            //公司联络人Contact
            so.setContact(rs.getString("CusContact"));
            so.setMailAddress(rs.getString("CusMailAddress"));
            so.setEmail(rs.getString("CusEmail"));
            so.setPoNO(rs.getString("CustomerPONo"));
            so.setDeliveryAddress(rs.getString("CusDeliveryAddress"));
            so.setTel(rs.getString("CusTel"));
            so.setFax(rs.getString("CusFax"));
            so.setErpExchangeRate(rs.getString("ExchangeRate"));
            /**TODO:不是直接顯示ID，要顯示PaymentTerm表中的Name字段         */
            so.setPaymentTerm(rs.getString("PaymentTermID"));
            so.setPayMethod(rs.getString("PayMethod"));
            so.setDeliveryMethod(rs.getString("DeliveryMethod"));
            so.setPaymentPeriod(rs.getString("PaymentPeriod"));
            //销售订单日期SODate
            String SODate = ThreadLocalDateUtil.formatDate(new Date());
            so.setErpDueDate(SODate);
            String currentDate = ThreadLocalDateUtil.formatDate(new Date());
            so.setLatestEditTime(currentDate);
            so.setCreationTime("2016-09-01 10:10:10");
            so.setLatestEditBy(user.getUserName());

            /**
             * TODO  , 需要计算： 待定
             * 设置product detail右下角那堆属于SO的字段:Discount,Sub Total,Grand Total
             */
            so.setDiscount("1");
            so.setSubTotal("1");//小计
            so.setGrandTotal("1");//累计
            /**
             * 处理list<ProductDetail>, 根据db中的SOID关联Item_SO表，拿出所有的product Detail,注意为空情况
             */
            List<ProductDetails> pds = handlePdsList(Double.valueOf(so.getErpExchangeRate()));
            so.setPds(pds);
            soList.add(so);


        }
        return soList;
    }

    private static List<Invoices> getInvoiceList() throws SQLException, ParseException{
        List<Invoices> invoicesList = new ArrayList<Invoices>();
        String sql = "select * from dbo.invoices ";
        ResultSet rs = exeQuery(sql);
        while (rs != null && rs.next()){
            Invoices invoices = new Invoices();
            /**DB中的Invoices id*/
            invoices.setErpID(rs.getString("InvoiceID"));
            //Subject主题
            invoices.setInvoiceSubject(rs.getString("InvoiceRef"));
            //TODO 1. 注意拥有者User一定要存在系统中  , 发票拥有者Invoice Owner
            User user = new User("80487000000076001","marketing");
//            User user = fetchDevUser(false);
            invoices.setUser(user);
//        invoices.setOwerID("85333000000071039");
//        invoices.setOwner("qq");
//            Invoice Date发货单日期
            String invoiceDate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("InvoiceDate"));
            invoices.setInvoiceDate(invoiceDate);
            /**
             * TODO：2. 注意SO中的SALESORDERID与Sales Order一定要存在系统
             * 是否只需要SONo
             */
//        SO so = new SO();
//        so.setSubject("PSO30190412");
//        so.setSALESORDERID("85333000000106003");
            //so id 和SO name
//            invoices.setSALESORDERID(rs.getString("PaymentPeriod"));
            invoices.setSoName(rs.getString("SORef"));
//汇率
            invoices.setErp_ExchangeRate(rs.getString("ExchangeRate"));
            /**TODO 不是直接顯示ID，要顯示PaymentTerm表中的Name字段         */
            invoices.setPaymentTerm(rs.getString("PaymentTermID"));
            invoices.setCustomerNo(rs.getString("CusRef"));
            //TODO 似乎没用到Due Date到期日期
            String dueDate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("invoiceDate"));
            invoices.setDueDate(dueDate);
            /**
             * 3. 注意Account一定要存在系统
             * Account id 和Name一般是同时存在的；
             * 如果只存在id，Name可以不对；
             * 如果只存在Name，那么会新创建一个客户；
             * 例外：但如果有ID，那么ID必需存在已经创建的客户中
             */
            invoices.setACCOUNTID(rs.getString("CustomerID"));
            /**
             * PriPac Design & Communication AB, 注意&符号，以后会改成CDATA形式
             */
            invoices.setAcctName(rs.getString("CusName"));
            //Contact公司聯絡人
            invoices.setContact(rs.getString("CusContact"));
            // TODO ： confirm似乎没有这个字段
//            invoices.setEmail(rs.getString("PaymentPeriod"));
            //DeliveryAddress客户发货地址
            invoices.setDeliveryAddress(rs.getString("CusDeliveryAddress"));
            //客户邮寄地址
            invoices.setMailAddress(rs.getString("CusMailAddress"));
            invoices.setFax(rs.getString("CusFax"));
            invoices.setTel(rs.getString("CusTel"));
            invoices.setErp_Currency(rs.getString("CurrencyName"));
            //付款方式
            invoices.setPayMethod(rs.getString("PaymentPeriod"));
            //deliveryMethod
            invoices.setDeliveryMethod(rs.getString("DeliveryMethod"));
            //客户PONo
            invoices.setPoNO(rs.getString("CustomerPONo"));
            //DNNo 送貨單編號
            invoices.setDnNo(rs.getString("DNNo"));
            /**TODO Deposit訂金
             * 匯入時x ExchangeRate換成港幣
             * */
            invoices.setDeposit(rs.getString("Deposit"));
            /**其他费用
             *   匯入時x ExchangeRate換成港幣
             * */
            invoices.setOtherCharge(rs.getString("OtherCharge"));
            /**FreightAmount 运费
             * 匯入時x ExchangeRate換成港幣
             * */
            invoices.setFreightAmount(rs.getString("FreightAmount"));
            invoices.setTotal(rs.getString("Total"));
            String lastEditTime = ThreadLocalDateUtil.formatDate(rs.getTimestamp("LatestEditTime"));
            invoices.setLatestEditTime(lastEditTime);
//            String dueDate = ThreadLocalDateUtil.formatDate(rs.getTimestamp("invoiceDate"));
//            invoices.setCreationTime("2016-09-01 10:10:10");
            invoices.setLatestEditBy(user.getUserName());


            /**
             * 设置product detail右下角那堆属于Invoices的字段:Discount,Sub Total,Grand Total
             */
            /**Customer Discount来自Customer的折扣*/
            invoices.setCusDiscount("100");
            /**Discount来自销售订单中的“折扣”*/
//        invoices.setDiscount("100");
            /**Sub Total 来自销售订单中的“小计”*/
            invoices.setSubTotal("4000");//小计
            /**Grand Total来自销售订单中的“累计”*/
            invoices.setGrandTotal("3000");//累计
            /**
             * 4. 注意Product中的productid 和product name一定要存在与系统中
             * 处理list<ProductDetail>, 根据db中的InvoicesID关联Item_Invoices表，拿出所有的product Detail,注意为空情况
             */
            List<ProductDetails> pds = handlePdsList(Double.valueOf(invoices.getErp_ExchangeRate()));
            invoices.setPds(pds);
            invoicesList.add(invoices);
        }
        return invoicesList;
    }

    /**
     *  根据db中的SOID关联Item_SO表，拿出所有的product Detail,这里假设找到2条数据
     * @param erpExchangeRate
     * @return
     */
    private static List<ProductDetails> handlePdsList(double erpExchangeRate) {
        String sql = "select * from ";
        List<ProductDetails> pds = new ArrayList<ProductDetails>();
        ProductDetails pd =new ProductDetails();
        String realUnitPrice = String.valueOf(0.73 * erpExchangeRate);
        String listPrice = String.valueOf(0.73 * erpExchangeRate);
        pd.setPd_Unit_Price(realUnitPrice);//定价  ： DB-->SOPrice,注意价格要跟Currency一致
        pd.setPd_List_Price(listPrice);//单价  ： DB-->SOPrice,注意价格要跟Currency一致
        pd.setPd_Quantity("10000");//数量
        pd.setPd_Discount("0");//折扣
        pd.setPd_Tax("0");//税，Matrix默认这个字段是0，因为不用税

        pd.setPd_Product_Description("Wesc Cardboard drawer box ");
        /**
         * 注意 product id和Name一定要是已经存在与产品里面的
         */
        pd.setPd_Product_Id("80487000000095003");
        pd.setPd_Product_Name("尼龍背心環保袋");//TODO 需要找ken确认ItemName为【Name】是表示什么意思？是否根据id从Item表找
        pd.setPd_Total("86664.0");//金额
        pd.setPd_Net_Total("86666.0");//总计

        ProductDetails pd2 = pd;
        pd2.setPd_Quantity("10000");//数量
        pds.add(pd2);
        pds.add(pd);
        return pds;
    }

    private static ResultSet exeQuery(String sql) throws SQLException {
        ResultSet rs = null;
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            logger.error("exeQuery出现错误",e);
            throw e;
        }
        return rs;

    }
}
