/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ModuleManager.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.jaxb.Accounts.Response;
import darlen.crm.jaxb.common.FL;
import darlen.crm.jaxb.common.ProdRow;
import darlen.crm.manager.handler.*;
import darlen.crm.util.*;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * darlen.crm.jaxb
 * Description：ZOHO_CRM
 * Created on  2016/09/27 21：39
 *
 * 2大功能： 1 ： execAllSend()对所有module执行操作  2 --> 执行house Keep工作
 *
 * TODO list:
 * 1. getRecords时最大200条这个还没有做限制[doing] --> [done] 20160930
 *    每次取100条，解析为Response对象之后,就可以拿到所有的条数，判断条数如果<100，则代表已经取完
 * 3. 实例化本类以后将使用Spring
 * 4. 不同module生成的log放到不同的日志文件中[已完成样例，明天应用到系统中]--> 20160931 [will done]
 * 5. 使用适配器模式、单例模式等改造[doing]
 * 6. 当超过13人该怎么办,会出现怎样的Error[提供的文件超过人数，取前面13人]-->周末完成
 * 当人数properties文件有变动时，重新reload：https://crm.zoho.com.cn/crm/private/xml/Users/getUsers?authtoken=00f65ee9c91b4906dbf4c1bd46bb1452&scope=crmapi&type=AllUsers
 * 从ZOHO更新最新的人员列表进入缓存，以后可以直接用
 * 7. 当API使用次数超过，会出现怎样的Error[doing,稍后查询尝试4000次，遍历最大次数后会出现怎样的情况]--》20160931：晚上11：30开始跑
 * 8. DB的操作，查找-->  20160930完成，如果完成不了，周末一定要完成[done]
 * 9. 每次到ZOHO做完操作后，记录时间到file文件中--> 周末完成
 * 10. Spring Quatz[周末开始]--》周末完成[done]
 * 11. Quotas应用--> 周末完成[done，但还需要改进--》能手动触发]
 * 12. 确认UI[尽量在这周末]--》周末完成
 * 13. 异常处理情况，比如说连不到网络
 * 14.等DB工作完全做完应用到系统之后，需要把lastEditTime这个时间是否呗修改应用到update方法上
 *
 * 15. 加一个拦截器，在每个方法执行前和执行后打印一句话
 * 16. LastEditTime修改时间这个判断还没加上[20161002]
 * 17. 写一个线程，每隔一段时间更新LastEditTime，这样我的程序就能每隔一段时间去ZOHO更新数据
 *
 * 问题：
 * 1. 不能删除相关联的数据
 *
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：39   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class ModuleManager {

    private static Logger logger = Logger.getLogger(ModuleManager.class);
    private  static IModuleHandler module ;

    public IModuleHandler getModule() {
        return module;
    }

    public void setModule(IModuleHandler module) {
        this.module = module;
    }

    public static void exe(){
        //1.add
        //2.update
        //3.delete
        System.out.println("第N次执行结果，｛add/update/delete｝成功多少，失败多少"+ ModuleNameKeys.Accounts);
    }

    public static List exeAccounts() throws Exception {
        //for Accounts
        module = AccountsHandler.getInstance();

//        List zohoXMLList = new ArrayList();
//        zohoXMLList.add(0);
//        zohoXMLList.add(0);
//        zohoXMLList.add(2,Arrays.asList(new String[]{"80487000000100633","80487000000099651","80487000000099635","80487000000098001"}));
//        module.delRecords(ModuleNameKeys.Accounts.toString(), Constants.ZOHO_CRUD_DELETE, zohoXMLList);
//        module.build2ZohoXmlSkeleton();
//        module.buildDBObjList();
//        module.addRecords();
//        testFetch(1,100);
//        module.updateRecords(ModuleNameKeys.Accounts.toString(),"UPDATE");
//            module.delRecords();
//        Thread thread = new Thread();
//        thread.run();
//        for(int i = 0; i< 10000; i++){
//            System.err.println("遍历次数"+i);
//            module.retrieveZohoRecords(ModuleNameKeys.Accounts.toString(), 1, 2);
//        }
        return module.execSend();
    }


    public static List exeProducts() throws Exception {
        //for Accounts
        module = ProductHandler.getInstance();
//        module.build2ZohoXmlSkeleton();
//        module.buildDBObjList();
//        List zohoXMLList =module.build2ZohoXmlSkeleton();
//        module.updateRecords(ModuleNameKeys.Products.toString(), Constants.ZOHO_CRUD_UPDATE,zohoXMLList);
        return  module.execSend();
    }
    public static List exeQuotes() throws Exception {
        //for invoice
        module = QuotesHandler.getInstance();      //module.build2ZohoXmlSkeleton();
        //module.build2ZohoXmlSkeleton();
        return module.execSend();
    }

    public static List exeSO() throws Exception {

        module = SOHandler.getInstance();

        //module.buildSkeletonFromZohoList();
        //module = ProductHandler.getInstance();
        //module.buildSkeletonFromZohoList();
        //
        //module = SOHandler.getInstance();
        //module.build2ZohoXmlSkeleton();
        //module.buildDBObjList();
        return  module.execSend();

    }
    public static List exeInvoice() throws Exception {
        //for invoice
        module = InvoicesHandler.getInstance();

        //module.build2ZohoXmlSkeleton();
        //module.build2ZohoXmlSkeleton();
        return module.execSend();
    }



    /**
     * 执行对所有Module的操作
     * 关于report的格式：：：AccountFailNum|ProductFailNum|QuoteFailNum|SOFailNum|InvoiceFailNum
     * 1、 执行5个Module的操作
     * 2、 如果都成功，更新lastExecSuccessTime.properties
     * 3. 更新DB中的report
     *
     * TODO ： 如果执行某个模块出错，那么该怎么处理，是继续下去还是可以执行其他的模块
     * @throws Exception
     */
    public static List exeAllModuleSend()throws Exception{
        //启动时间
        List list = new ArrayList();
        Date startDate = new Date();
        String insertFailStr = "";
        String updateFailStr = "";
        String deleteFailStr = "";
        String wholeModuleFail = "";

        List acctList = new ArrayList();
        List prodList = new ArrayList();
        List quoteList = new ArrayList();
        List soList = new ArrayList();
        List invList = new ArrayList();
        boolean allSuccess = true;
        String message = "";
        try{
            try{
                 logger.info("①, Accounts##################################################begin execute Account Module##################################################");
                 logger.info("####################################################################################################");
                 acctList = exeAccounts();
                 logger.info("Print exeAccounts result :::"+acctList+"\n\n\n\n\n");
            }catch (Exception e){
                allSuccess = false;
                logger.error("[ModuleManager]， exeAccounts occurs error ",e);
                wholeModuleFail = "ACCT";
                message += "Customer : "+e.getMessage();
            }

            logger.info("②， Products ##################################################begin execute Product Module##################################################");
            logger.info("####################################################################################################");
            try{
                prodList = exeProducts();
                logger.info("Print exeProducts result :::"+prodList+"\n\n\n\n\n");
            }catch (Exception e){
                allSuccess = false;
                logger.error("[ModuleManager], exeProducts occurs error",e);
                wholeModuleFail += "|"+ "PROD";
                message += ", Products : "+e.getMessage();
            }

            logger.info("③, Write Properties##################################################Begin Execute Write Properties : Account.properties,Product.properties...##################################################");
            try{
                rewriteAcctProdProps();
            }catch (Exception e){
                logger.error("[ModuleManager]， testWriteFiles occurs error", e);
            }
            logger.info("##################################################End Execute Write Properties Account.properties,Product.properties##################################################\n\n\n\n\n");

            logger.info("④, Quotes ##################################################Begin Execute Quotes Module##################################################==");
            logger.info("####################################################################################################");
            try{
                quoteList = exeQuotes();
                logger.info("Print exeQuotes result :::"+quoteList+"\n\n\n\n\n");
            }catch (Exception e){
                allSuccess = false;
                logger.error("[ModuleManager], exeQuotes occurs error",e);
                wholeModuleFail += "|"+ "QUOTES";
                message += ", Quotes : "+e.getMessage();
            }

            logger.info("⑤, SO ##################################################Begin Execute SO Module##################################################==");
            logger.info("####################################################################################################");
            try{
                soList = exeSO();
                logger.info("Print exeSO result :::"+soList+"\n\n\n\n\n");
            }catch (Exception e){
                logger.error("[ModuleManager],  exeSO occurs error",e);
                wholeModuleFail += "|"+ "SO";
                message += ", SO : "+e.getMessage();
            }

            logger.info("⑥, Invoices ##################################################Begin Execute Invoices Module##################################################");
            logger.info("####################################################################################################");
            try{
                invList = exeInvoice();
                logger.info("Print exeInvoice result :::"+invList+"\n\n\n\n\n");
            }catch (Exception e){
                allSuccess = false;
                logger.error("[ModuleManager]， Execute Invoice List occurs error ",e);
                wholeModuleFail += "|"+ "Invoice";
                message += ", Invoices : "+e.getMessage();
            }
            //格式：AccountFailNum|ProductFailNum|QuoteFailNum|SOFailNum|InvoiceFailNum
            boolean isAcctListEmpty = CommonUtils.isEmptyList(acctList);
            boolean isProdListEmpty = CommonUtils.isEmptyList(prodList);
            boolean isQuotesListEmpty = CommonUtils.isEmptyList(quoteList);
            boolean isSoListEmpty = CommonUtils.isEmptyList(soList);
            boolean isInvListEmpty = CommonUtils.isEmptyList(invList);
            insertFailStr = ( isAcctListEmpty? "": StringUtils.nullToString(acctList.get(0))+"|")//AccountFailNum
                    + ( isProdListEmpty? "": StringUtils.nullToString(prodList.get(0))+"|")//ProductFailNum
                    + ( isQuotesListEmpty ? "": StringUtils.nullToString(quoteList.get(0))+"|")//QuoteFailNum
                    + ( isSoListEmpty? "": StringUtils.nullToString(soList.get(0))+"|")//SOFailNum
                    + ( isInvListEmpty ? "": StringUtils.nullToString(invList.get(0)));//InvoiceFailNum

            updateFailStr = ( isAcctListEmpty? "":StringUtils.nullToString(acctList.get(1))+"|")
                    +( isProdListEmpty? "":StringUtils.nullToString(prodList.get(1))+"|")
                    +( isQuotesListEmpty ? "": StringUtils.nullToString(quoteList.get(1))+"|")
                    +( isSoListEmpty? "":StringUtils.nullToString(soList.get(1))+"|")
                    +( isInvListEmpty? "":StringUtils.nullToString(invList.get(1)));

            deleteFailStr = ( isAcctListEmpty? "":StringUtils.nullToString(acctList.get(2))+"|")
                    +( isProdListEmpty? "":StringUtils.nullToString(prodList.get(2))+"|")
                    +( isQuotesListEmpty ? "": StringUtils.nullToString(quoteList.get(2))+"|")
                    +( isSoListEmpty? "":StringUtils.nullToString(soList.get(2))+"|")
                    +( isInvListEmpty? "":StringUtils.nullToString(invList.get(2)));


            //如果都执行成功，那么更新lastExecSuccessTime.properties
            if(allSuccess){
                logger.info("⑦, update LAST_EXEC_SUCCESS_TIME ##################################################Begin Execute Invoices Module##################################################");
                logger.info("####################################################################################################");
                Map<String,String> map = new HashMap<String, String>();
                map.put(Constants.LAST_EXEC_SUCCESS_TIME,ThreadLocalDateUtil.formatDate(new Date()));
                ConfigManager.writeVal2Props(map,Constants.PROPS_TIME_FILE);
            }

        }catch (Exception e) {
            allSuccess = false;
            logger.error("[exeAllModuleSend], all error ",e);
            message += ", all : "+e.getMessage();
            throw e;
        }finally {
            logger.info("⑧, update Report ####################################################################################################");
            //结束时间new Date()
            Date endDate = new Date();
            List updERPList = new ArrayList();
            updERPList.add(startDate);
            updERPList.add(endDate);
            updERPList.add(insertFailStr);
            updERPList.add(updateFailStr);
            updERPList.add(deleteFailStr);
            updERPList.add(wholeModuleFail);
            CommonUtils.printList(updERPList,"#####Late Update Report");
            ////执行更新Report操作
            String sql = "INSERT INTO ZOHO_EXCE_REPORT (START_TIME, END_TIME,INS_FAILED,UPD_FAILED,DEL_FAILED,WHOLEFAIL) VALUES(?,?,?,?,?,?)";
            DBUtils.exeUpdReport(sql, updERPList);
            logger.debug("################################################"
                    +Constants.COMMENT_PREFIX+"$$$Late Execute Time:"+(endDate.getTime()-startDate.getTime())/1000+"(s)");
        }
        list.add(0,allSuccess);
        list.add(1, message);
        return list;
    }


    /**
     * 执行House Keep： 暂定每天晚上12点，由于ZOHO对Product的删除限定，删除顺序为Accounts，Quotes，SO，Invoice，Product
     *
     * 经测试，删除Account--> 相关Invoice记录也会删除，SO也会删除
     *
     * 如何删除PRODUCT及相关联的QUOTES/SO/INVOICE模块：（暂时不考虑ERP ID是否存在与DB）
     * 1. 拿出ZOHO中所有的ERP ID is EMPTY or DUPLICATE的数据，然后遍历QUOTES/SO/INVOICE
     * 2. 上面PRODUCT的ERP ID存在与三个模块中，加入删除列表，
     * 3. 同时检测三大模块的ERP ID is EMPTY or DUPLICATE的数据， 加入删除列表
     * 4. 删除完三大列表的所有相关的PRODUCT之后，最后删除PRODUCT
     *
     * 注意:  注意删除顺序，由于ZOHO的一些限制，主要是针对Product，因为product跟Quates、Invoices、SO有关联，
     * 如果不删除后面三大模块，那么Product会删不掉，但是调用API的时候也会显示删除成功
     * @param  deleteAll ， 是否删除所有Module的数据
     */
    public static void execAllModuleHouseKeep(boolean deleteAll) throws Exception {
        //1, for Account
        long startDate = new Date().getTime();
        logger.debug("##############1. Begin Accounts HouseKeep#################\n\n\n\n\n");
        IModuleHandler acctModule = AccountsHandler.getInstance();
       // List  zohoAcctCompList =  acctModule.buildSkeletonFromZohoList();
        //拿出ZOHO中ERP为空或者Dulplicate的record，直接删除
        List delZohoIDList = getDelZohoIDs(acctModule,deleteAll);//(List)zohoAcctCompList.get(2);
        logger.debug("Delete Module [Accounts] ZOHO IDList : "+Constants.COMMENT_PREFIX+"[House Keep]"+delZohoIDList);
        acctModule.delRecords(ModuleNameKeys.Accounts.toString(),Constants.ZOHO_CRUD_DELETE,delZohoIDList);

        logger.debug("##############Begin Product HouseKeep#################\n\n\n\n\n");
        //2. for Product,获取需要删除的所有Product的ID集合
        IModuleHandler prodModule = ProductHandler.getInstance();
        List delProdIDList = getDelZohoIDs(prodModule,deleteAll);

        //if(null != zohoCompList && zohoCompList.size() >=3){
        //
        //    module.delRecords(ModuleNameKeys.Accounts.toString(),Constants.ZOHO_CRUD_DELETE,zohoCompList);
        //}

        // 删除Quotes，SO，Invoices
       if(delProdIDList.size() > 0){
            //3. for Quotes
           logger.debug("##############3. Begin [QUOTES] HouseKeep#################\n\n\n\n\n");
            module = QuotesHandler.getInstance();
            execModuleHouseKeep(delProdIDList,ModuleNameKeys.Quotes.toString());
            //4. for SO
           logger.debug("##############4. Begin [SO] HouseKeep#################\n\n\n\n\n");
            module = SOHandler.getInstance();
            execModuleHouseKeep(delProdIDList,ModuleNameKeys.SalesOrders.toString());
            //5. for Invoice
           logger.debug("##############5. Begin [INVOICES] HouseKeep#################\n\n\n\n\n");
            module = InvoicesHandler.getInstance();
            execModuleHouseKeep(delProdIDList,ModuleNameKeys.Invoices.toString());
        }
        //6. 最后删除Product
        logger.debug("##############6. Last Begin [Products] HouseKeep#################\n\n\n\n\n");
        logger.debug("删除模块 [Products]的ZOHO ID集合为"+Constants.COMMENT_PREFIX+"[House Keep]"+delZohoIDList);
        prodModule.delRecords(ModuleNameKeys.Products.toString(),Constants.ZOHO_CRUD_DELETE,delProdIDList);

        // 7. 如何housekeep止呕， 一定要把lastExecSuccess.properties置为0。以便下次能把所有数据插入
        if(deleteAll){
            logger.debug("##############7. Last Rest LAST_EXEC_SUCCESS_TIME为0#################\n\n\n\n\n");
            Map<String,String> map = new HashMap<String, String>();
            map.put(Constants.LAST_EXEC_SUCCESS_TIME,"0");
            ConfigManager.writeVal2Props(map,Constants.PROPS_TIME_FILE);
        }

        logger.debug("################################################"
                +Constants.COMMENT_PREFIX+"$$$Last Execute[execAllModuleHouseKeep] time :"+(new Date().getTime()-startDate)/1000+"秒");


    //    TODO 删除报表的table中以前的数据，暂定是1个月的


    }

    /**
     * 获取所有需要删除的Product的ID集合
     * 1. 正常模式-->仅仅删除 ERP为空或者Dulplicate的Product的ID集合
     * 2. 开发模式-->删除所有
     * @return
     * @throws Exception
     */
    private static List getDelZohoIDs(IModuleHandler module,boolean deleteAll) throws Exception {
        List  zohoProdCompList =  module.buildSkeletonFromZohoList();
        List<String> delZohoIDList = new ArrayList<String>();

        /**
         * 1. 如果仅仅删除 ERP为空或者Dulplicate的Product的ID集合--》正常环境中使用
         * */
        delZohoIDList = (List)zohoProdCompList.get(2);

        if(deleteAll){
            /**
             * 2, 如果要删除所有的Product的ID集合 --》开发时候使用
             */
            Map<String,String> allProdErpZohoIDMap = (Map<String,String>)zohoProdCompList.get(0);
            delZohoIDList = new ArrayList<String>();
            for(Map.Entry<String,String> entry : allProdErpZohoIDMap.entrySet()){
                if(!"-1".equals(entry.getKey())){//忽略ERP ID 为-1的Account模块
                    String zohoID = entry.getValue();
                    delZohoIDList.add(zohoID);;
                }
            }
        }

        return delZohoIDList;

    }

    /**
     * 根据需要删除的Product ID的集合delProdIDList，获取需要删除的所有SO记录，并执行删除
     *
     * 好像关联关系是：
     * Accounts跟Invoice关联，只要删掉Accounts，那么Invoice，so，quotes就都可以删除了，但Accounts只能用系统的，不能自定义一个查找类型的字段
     * Product跟SO关联，只要删除SO，相应Product就可以删除了
     *e
     * 1. 拿到ZOHO中ERP ID为空或者Duplicate记录的集合（delSOZohoIDList），并且拿出ZOHO ID和相应的所有的product ID的集合（zohoIDProdIDsMap）
     * 2. 遍历所有的zohoIDProdIDsMap， 取出某条SO中包含的所有Product的ZOHO ID集合，查看集合中的ZOHO ID是否存在于需要删除的Product集合中
     * 3. 如果是，则终止遍历，并把当前module的id加入删除list
     *
     * @param delProdIDList
     * @throws Exception
     */
    public static void execModuleHouseKeep(List delProdIDList,String moduleName) throws Exception {
        //删除Quotes和Products
        if(ModuleNameKeys.Quotes.equals(moduleName)){
            module = QuotesHandler.getInstance();
        }else if (ModuleNameKeys.SalesOrders.equals(moduleName)){
            module = SOHandler.getInstance();
        }else if (ModuleNameKeys.Invoices.equals(moduleName)){
            module = InvoicesHandler.getInstance();
        }

        List  zohoCompList =  module.buildSkeletonFromZohoList();
        //List delAllSOZohoIDList = new ArrayList();
        //1. 拿到ZOHO中Customer的ERP ID为空或者Duplicate记录的集合（delSOZohoIDList），
        // 并且拿出ZOHO ID和相应的所有的product ID的集合（zohoIDProdIDsMap）
        List<String> delZohoIDList = (List<String>)zohoCompList.get(2);
        Map<String,List> acctZohoIDProdIDsMap = (Map<String,List>)zohoCompList.get(3);
        Map<String,String> erpZohoIDMap = (Map<String,String>)zohoCompList.get(0);

        boolean isCustomerIDEmpty = false;
        //2. 遍历所有的acctZohoIDProdIDsMap， 取出某条SO中包含的所有Product的ZOHO ID集合，
        // 并查看集合中的ZOHO ID是否存在于需要删除的Product集合（delProdIDList）中
        for(Map.Entry<String,List> entry : acctZohoIDProdIDsMap.entrySet()){
            String customerID = entry.getKey();
            //如果Customer ID 为空,那么需要直接删除Quotes的Zoho ID
            if(StringUtils.isEmptyString(customerID)){
                isCustomerIDEmpty = true;
            }else{
            ////否则，仅仅只需要删除Customer的zoho ID就可以删除关联的Quotes/SO/Invoices记录
            //    List prodsIDs = entry.getValue();
            //    //3. 如果delProdIDList包含当前module的某个产品，则终止遍历，并把当前module的id加入删除list
            //    for(Object o : prodsIDs){
            //        if(delProdIDList.contains(o)){
            //            delZohoIDList.add(entry.getKey());
            //            break;
            //        }
            //    }
            }

        }

        if(isCustomerIDEmpty){
            //如果Cusomer ID，需要主动先去删除Quotes/SO/Invoice的ZOHO ID
            for(Map.Entry<String,String> entry : erpZohoIDMap.entrySet()){
                delZohoIDList.add(entry.getValue());
            }
            logger.debug(" 删除模块 ["+moduleName+"]的Accounts ZOHO ID集合为"+Constants.COMMENT_PREFIX+"[House Keep]"+delZohoIDList);
            module.delRecords(moduleName, Constants.ZOHO_CRUD_DELETE, delZohoIDList);
        }else{
        //    不需要做任何事，因为我们再第一步已经把跟Account删掉了，那么就代表已经把所有的Quotes/SO/Invoice删掉了，不需要再删除了
        }

        //logger.debug(" 删除模块 ["+moduleName+"]的Accounts ZOHO ID集合为"+Constants.COMMENT_PREFIX+"[House Keep]"+delZohoIDList);
        //NOTICE: 注意这里是删除Accounts模块，因为删除相应的Account ZOHO ID之后，相关联的Quotes，Invoice，SO模块也会相应删除
        //module.delRecords(ModuleNameKeys.SalesOrders.toString(), Constants.ZOHO_CRUD_DELETE, delZohoIDList);
        //module.delRecords(ModuleNameKeys.Accounts.toString(), Constants.ZOHO_CRUD_DELETE, delZohoIDList);
    }


    public static void main(String[] args) throws Exception {
        //1. 环境检测
        //envAutoChecking();
        //2.执行
        //exeAllModuleSend();
        //3. 执行House Keep
        //execAllModuleHouseKeep(true);
        //exeProducts();
        //exeSO();
        //exeInvoice();
        //module = InvoicesHandler.getInstance();
        //List  zohoSOCompList =  module.buildSkeletonFromZohoList();

        ////1, for Account
        //IModuleHandler acctModule = AccountsHandler.getInstance();
        //// List  zohoAcctCompList =  acctModule.buildSkeletonFromZohoList();
        ////拿出ZOHO中ERP为空或者Dulplicate的record，直接删除
        //List delZohoIDList = getDelZohoIDs(acctModule);//(List)zohoAcctCompList.get(2);
        //logger.debug("删除模块[Accounts]的ZOHO ID集合为"+Constants.COMMENT_PREFIX+"[House Keep]"+delZohoIDList);
        //acctModule.delRecords(ModuleNameKeys.Accounts.toString(),Constants.ZOHO_CRUD_DELETE,delZohoIDList);

        //rewriteAcctProdProps();
        //exe();
    }
    /**
     * 在使用Manager之前一定要自检，判断环境是否可用，如果不可用，必需停止，排除环境因素后才能继续执行
     * 比如：读写properties、DB连接等等
     * @throws Exception
     */
    public static List envAutoChecking()throws Exception{
        List list = ConfigManager.envAutoChecking();
        if(list.size() > 0){
            int result = StringUtils.nullToInt(list.get(0));
            if(result != 0){
                logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$环境检测不正常,请排除所有错误后再进行工作$$$$$$$$$$$$$$$$$$$$$$");
            }else{
                logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$环境检测正常,请继续执行下面工作$$$$$$$$$$$$$$$$$$$$$$");
            }
        }
        return list;

    }
    /**
     * 真是情况是不需要提前写入File的，因为是执行顺序是Accounts，Products，Quote,SO,Invoices
     * 这里是为了方便写入文件
     * 写入Accounts.properties和Product.properties
     * @throws Exception
     */
    public static void rewriteAcctProdProps() throws Exception {
        module = AccountsHandler.getInstance();
        //module.buildSkeletonFromZohoList();
        List<ProdRow> zohoAcctRows = new ArrayList<ProdRow>();
        AccountsHandler.retrieveAllRowsFromZoho(1, Constants.MAX_FETCH_SIZE, zohoAcctRows);
        getAndWriteProps(zohoAcctRows,Constants.MODULE_ACCOUNTS_ID, Constants.ERPID,ModuleNameKeys.Accounts.toString());

        module = ProductHandler.getInstance();
        //module.buildSkeletonFromZohoList();
        List<ProdRow> zohoProdRows = new ArrayList<ProdRow>();
        ProductHandler.retrieveAllRowsFromZoho(1, Constants.MAX_FETCH_SIZE, zohoProdRows);
        getAndWriteProps(zohoProdRows,Constants.MODULE_PRODUCTS_ID, Constants.ERPID,ModuleNameKeys.Products.toString());


    }


    /**
     * 根据从ZOHO获取的Rows，提取ERP ID 和ZOHO ID， 并且将相应的值写入Product和Account的配置文件中
     * @param rows
     * @param zohoIDName
     * @param erpIDName
     * @param moduleName
     * @throws Exception
     */
    private static void getAndWriteProps(List<ProdRow> rows, String zohoIDName, String erpIDName,String moduleName) throws Exception {
        //List<ProdRow> rows = new ArrayList<ProdRow>();
        //AccountsHandler.retrieveAllRowsFromZoho(1, Constants.MAX_FETCH_SIZE, rows);
        Map<String,String> erpZohoIDMap = new HashMap<String, String>();
        //Constants.MODULE_ACCOUNTS_ID, Constants.ERPID
        for (int i = 0; i < rows.size() ; i++){
            String zohoID = "";
            String erpID = "";
            List<FL> fls = rows.get(i).getFls();
            for(FL fl : fls){
                String fieldName = fl.getFieldName();
                String fieldVal = fl.getFieldValue();
                if(zohoIDName.equals(fieldName) && !StringUtils.isEmptyString(fieldVal)){
                    zohoID = fieldVal;
                }
                if(erpIDName.equals(fieldName)){
                    erpID = fieldVal;
                }

            }
            erpZohoIDMap.put(erpID, zohoID);
         }
        //当Module是Product或者是Account的时候，需要把所有的ERPID 和ZOHOID分别写入不同文件中，为了以后在数据库读取Accounts和Product使用
        if(ModuleNameKeys.Accounts.toString().equals(moduleName)  || ModuleNameKeys.Products.toString().equals(moduleName)){
            ConfigManager.writeVal2Props(erpZohoIDMap,ModuleNameKeys.Accounts.toString().
                    equals(moduleName) ? Constants.PROPS_ACCT_FILE : Constants.PROPS_PROD_FILE);
        }
    }

    /**
     *
     * @param fromIndex
     * @param toIndex
     * @throws Exception
     */
    private static void testFetch(int fromIndex,int toIndex) throws Exception {
//         fromIndex = 1;  toIndex = 100;
        String zohoStr = module.retrieveZohoRecords(ModuleNameKeys.Accounts.toString(), fromIndex, toIndex);
        Response response = JaxbUtil.converyToJavaBean(zohoStr, Response.class);
        if(response.getResult()!= null){
            List<ProdRow> rows = response.getResult().getAccounts().getRows();
            if(rows.size() == 100){
                System.err.println("遍历次数："+((fromIndex/100)+1));
                testFetch(fromIndex+100,toIndex+100);
            }
        }
    }


    /**
     * 测试获取对象类型
     */
    private static void testRefect(){
        List list = new ArrayList();
        list.add(1);
        list.add("test");
        //    module = AccountsHandler.getInstance();
        list.add(new Date());

        for(Object o : list){
            if(o instanceof  Integer){
                //            System.out.println("Integer");
            }
        }

        for(int i = 0; i <list.size();i ++){
            try{
                Object o = list.get(i);
                //1. 获取对象类型
                String objectType = list.get(i).getClass().getName();
                System.out.println("对象类型："+objectType);
                if( o instanceof  Integer){
                    System.out.println("Integer");
                }else if(o instanceof String){
                    System.out.println("String");
                }else  if(o instanceof  Date){
                    System.out.println("Integer");
                }
                //2. 获取对象中Method返回类型
                //            Method method = list.get(i).getClass().getMethod("get",null);
                //            Class returnTypeClass = method.getReturnType();
                //            System.out.println("对象["+objectType+"]类型为:"+returnTypeClass);
            }catch(Exception e){
                System.out.println(e);
            }
        }


    }
}
