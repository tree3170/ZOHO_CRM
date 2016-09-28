/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   ModuleManager.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.manager;

import darlen.crm.manager.handler.AccountsHandler;
import darlen.crm.manager.handler.InvoicesHandler;
import darlen.crm.manager.handler.ProductHandler;
import darlen.crm.manager.handler.SOHandler;
import darlen.crm.util.ModuleNameKeys;

/**
 * darlen.crm.jaxb
 * Description：ZOHO_CRM
 * Created on  2016/09/27 21：39
 *
 * TODO list:
 * 1. getRecords时最大200条这个还没有做限制
 * 2. 对13个人还没有做限制
 * 3. 实例化本类以后将使用Spring
 * 4. 不同module生成的log放到不同的日志文件中
 * 5. 使用适配器模式、单例模式等改造
 * 6. 当超过13人该怎么办,会出现怎样的Error
 * 7. 当API使用次数超过，会出现怎样的Error
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
        module.build2ZohoXmlSkeleton();
//        module.buildDBObjList();
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
//        exeAccounts();
        exeProducts();
//        exeSO();
//        exeInvoice();

        exe();
    }
}
