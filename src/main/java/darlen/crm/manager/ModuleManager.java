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
import darlen.crm.manager.handler.AccountsHandler;
import darlen.crm.manager.handler.InvoicesHandler;
import darlen.crm.manager.handler.ProductHandler;
import darlen.crm.manager.handler.SOHandler;
import darlen.crm.util.JaxbUtil;
import darlen.crm.util.ModuleNameKeys;

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
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：39   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class ModuleManager {
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

    public static void exeAccounts() throws Exception {
        //for Accounts
        module = AccountsHandler.getInstance();
//        module.build2ZohoXmlSkeleton();
//        module.buildDBObjList();
//        module.addRecords();
//        testFetch(1,100);
        module.updateRecords(ModuleNameKeys.Accounts.toString(),"UPDATE");
//            module.delRecords();
//        Thread thread = new Thread();
//        thread.run();
//        for(int i = 0; i< 10000; i++){
//            System.err.println("遍历次数"+i);
//            module.retrieveZohoRecords(ModuleNameKeys.Accounts.toString(), 1, 2);
//        }
    }

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

    public static void exeProducts() throws Exception {
        //for Accounts
        module = ProductHandler.getInstance();
        module.build2ZohoXmlSkeleton();
//        module.buildDBObjList();
    }

    public static void exeSO() throws Exception {
        //for Accounts
        module = SOHandler.getInstance();
        module.build2ZohoXmlSkeleton();
//        module.buildDBObjList();
    }
    public static void exeInvoice() throws Exception {
        //for invoice
        module = InvoicesHandler.getInstance();
//        module.build2ZohoXmlSkeleton();
//        module.build2ZohoXmlSkeleton();
    }

    public static void main(String[] args) throws Exception {
        exeAccounts();
//        exeProducts();
//        exeSO();
//        exeInvoice();

        exe();
    }
}
