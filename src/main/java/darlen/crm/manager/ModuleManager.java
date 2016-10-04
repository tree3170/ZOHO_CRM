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
import darlen.crm.jaxb.common.ProdRow;
import darlen.crm.manager.handler.*;
import darlen.crm.util.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * darlen.crm.jaxb
 * Description：ZOHO_CRM
 * Created on  2016/09/27 21：39
 *
 * TODO list:
 * 1. getRecords时最大200条这个还没有做限制【doing】 --> 【done】 20160930
 *    每次取100条，解析为Response对象之后,就可以拿到所有的条数，判断条数如果<100，则代表已经取完
 * 3. 实例化本类以后将使用Spring
 * 4. 不同module生成的log放到不同的日志文件中【已完成样例，明天应用到系统中】--> 20160931 【will done】
 * 5. 使用适配器模式、单例模式等改造【doing】
 * 6. 当超过13人该怎么办,会出现怎样的Error【提供的文件超过人数，取前面13人】-->周末完成
 * 当人数properties文件有变动时，重新reload：https://crm.zoho.com.cn/crm/private/xml/Users/getUsers?authtoken=00f65ee9c91b4906dbf4c1bd46bb1452&scope=crmapi&type=AllUsers
 * 从ZOHO更新最新的人员列表进入缓存，以后可以直接用
 * 7. 当API使用次数超过，会出现怎样的Error【doing,稍后查询尝试4000次，遍历最大次数后会出现怎样的情况】--》20160931：晚上11：30开始跑
 * 8. DB的操作，查找-->  20160930完成，如果完成不了，周末一定要完成
 * 9. 每次到ZOHO做完操作后，记录时间到file文件中--> 周末完成
 * 10. Spring Quatz【周末开始】--》周末完成
 * 11. Quotas应用--> 周末完成
 * 12. 确认UI【尽量在这周末】--》周末完成
 * 13. 异常处理情况，比如说连不到网络
 * 14.等DB工作完全做完应用到系统之后，需要把lastEditTime这个时间是否呗修改应用到update方法上
 *
 * 15. 加一个拦截器，在每个方法执行前和执行后打印一句话
 * 16. LastEditTime修改时间这个判断还没加上【20161002】
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
    private  static  IModule module ;

    public IModule getModule() {
        return module;
    }

    public void setModule(IModule module) {
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
        module = QuotesHandler.getInstance();

        //module.build2ZohoXmlSkeleton();
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
     *关于report的格式：：：AccountFailNum|ProductFailNum|QuoteFailNum|SOFailNum|InvoiceFailNum
     *
     * TODO ： 如果执行某个模块出错，那么该怎么处理，是继续下去还是可以执行其他的模块
     * @throws Exception
     */
    public static void exeAllSend()throws Exception{
         //writeFiles();
        //启动时间
        Date startDate = new Date();
        logger.debug("##################################################开始执行Account Module##################################################");
        logger.debug("\n\n\n####################################################################################################");
        List acctsList = exeAccounts();
        logger.debug("##################################################开始执行Product Module##################################################");
        logger.debug("\n\n\n####################################################################################################");
        List prodList = exeProducts();
        //logger.debug("##################################################开始执行Quotes Module##################################################==");
        //logger.debug("\n\n\n####################################################################################################");
        //List quoteList = exeQuotes();
        logger.debug("##################################################开始执行SO Module##################################################==");
        logger.debug("\n\n\n####################################################################################################");
        List soList = exeSO();
        logger.debug("##################################################开始执行Invoices Module##################################################");
        logger.debug("\n\n\n####################################################################################################");
        List invList = exeInvoice();
        //格式：AccountFailNum|ProductFailNum|QuoteFailNum|SOFailNum|InvoiceFailNum
        String insertFailStr = StringUtils.nullToString(acctsList.get(0))+"|"//AccountFailNum
                +StringUtils.nullToString(prodList.get(0))+"|"//ProductFailNum
                //+StringUtils.nullToString(quoteList.get(0))//QuoteFailNum
                +StringUtils.nullToString(0)+"|"//QuoteFailNum
                +StringUtils.nullToString(soList.get(0))+"|"//SOFailNum
                +StringUtils.nullToString(invList.get(0));//InvoiceFailNum

        String updateFailStr = StringUtils.nullToString(acctsList.get(1))+"|"
                +StringUtils.nullToString(prodList.get(1))+"|"
                //+StringUtils.nullToString(quoteList.get(1))
                +StringUtils.nullToString(0)+"|"
                +StringUtils.nullToString(soList.get(1))+"|"
                +StringUtils.nullToString(invList.get(1));

        String deleteFailStr = StringUtils.nullToString(acctsList.get(2))+"|"
                +StringUtils.nullToString(prodList.get(2))+"|"
                //+StringUtils.nullToString(quoteList.get(2))+"|"
                +StringUtils.nullToString(0)+"|"
                +StringUtils.nullToString(soList.get(2))+"|"
                +StringUtils.nullToString(invList.get(2));

        //结束时间new Date()
        Date endDate = new Date();
        List updERPList = new ArrayList();
        updERPList.add(startDate);
        updERPList.add(endDate);
        updERPList.add(insertFailStr);
        updERPList.add(updateFailStr);
        updERPList.add(deleteFailStr);
        CommonUtils.printList(updERPList,"#####最后更新Report");
        //执行更新Report操作
        String sql = "INSERT INTO ZOHO_EXCE_REPORT (START_TIME, END_TIME,INS_FAILED,UPD_FAILED,DEL_FAILED) VALUES(?,?,?,?,?)";
        DBUtils.exeUpdReport(sql, updERPList);
    }



    public static void main(String[] args) throws Exception {
        //1. 环境检测
        //envAutoChecking();
        //2.执行
        exeAllSend();


        exe();
    }
    /**
     * 在使用Manager之前一定要自检，判断环境是否可用，如果不可用，必需停止，排除环境因素后才能继续执行
     * 比如：读写properties、DB连接等等
     * @throws Exception
     */
    public static void envAutoChecking()throws Exception{
        ConfigManager.envAutoChecking();
        logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$环境检测正常,请继续执行下面工作$$$$$$$$$$$$$$$$$$$$$$");
    }
    /**
     * 真是情况是不需要提前写入File的，因为是执行顺序是Accounts，Products，Quote,SO,Invoices
     * 这里是为了方便写入文件
     * 写入Accounts.properties和Product.properties
     * @throws Exception
     */
    public static void testWriteFiles() throws Exception {
        module = AccountsHandler.getInstance();
        module.buildSkeletonFromZohoList();
        module = ProductHandler.getInstance();
        module.buildSkeletonFromZohoList();
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
                //            System.out.println("对象【"+objectType+"】类型为:"+returnTypeClass);
            }catch(Exception e){
                System.out.println(e);
            }
        }


    }
}
