/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   QuartzManager.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.quartz;

import darlen.crm.manager.ConfigManager;
import darlen.crm.manager.ModuleManager;
import darlen.crm.model.mail.MailMail;
import darlen.crm.util.Constants;
import darlen.crm.util.ThreadLocalDateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.ParseException;
import java.util.Date;

/**
 * darlen.crm.quartz
 * Description：ZOHO_CRM
 * Created on  2016/10/03 21：50
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        21：50   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class QuartzManager {
    private static Logger logger = Logger.getLogger(QuartzManager.class);


    private SchedulerFactoryBean startQuertz;


    public SchedulerFactoryBean getStartQuertz() {
        return startQuertz;
    }
    //@Autowired
    public void setStartQuertz(SchedulerFactoryBean startQuertz) {
        this.startQuertz = startQuertz;
    }


    public void exe() throws Exception {
        logger.debug("\n\n\n\n\n\n\n"+Constants.COMMENT_PREFIX+"\n" +
                "##########################################################################################################\n" +
                "####\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t #####\n" +
                "####                       BEGIN JOB["+ThreadLocalDateUtil.formatDate(new Date())+"]                                            #####\n" +
                "#### \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t #####\n" +
                "##########################################################################################################");
        logger.debug("Begin QuartzManager...");
        //System.err.println("test 。。。");
        ModuleManager.exeAllModuleSend();
//        System.out.println(ConfigManager.get("secure/db.properties", "DB_USERNAME"));
//        System.out.println("======"+ConfigManager.getProdfromProps("marketing"));
//        System.out.println(ConfigManager.get(Constants.PROPS_DB_FILE,"DB_USERNAME"));
//        System.out.println("打印时间：date:" + ThreadLocalDateUtil.formatDate(new Date()) + "; marketing =" + ConfigManager.getProdfromProps("marketing"));
//        ConfigManager.envAutoChecking();
        logger.debug("End QuartzManager...\n\n\n\n\n\n");
    }
}
