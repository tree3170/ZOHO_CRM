/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   QuartzController.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.controller;

import darlen.crm.model.mail.MailMail;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * darlen.crm.controller
 * Description：ZOHO_CRM
 * Created on  2016/10/08 23：32
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        23：32   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
@Controller
public class QuartzController {
    private static Logger logger = Logger.getLogger(QuartzController.class);

    //@Autowired
    //private SchedulerFactoryBean startQuertz;
    @Autowired
    Scheduler scheduler;

    @Autowired
    private MailMail mail;
    //public SchedulerFactoryBean getStartQuertz() {
    //    return startQuertz;
    //}
    ////@Autowired
    //public void setStartQuertz(SchedulerFactoryBean startQuertz) {
    //    this.startQuertz = startQuertz;
    //}
    //localhost:8080/zoho/start
    @ResponseBody
    @RequestMapping("/start")
    public String start( HttpServletRequest request){
        logger.debug("start QuartzController...");
        //WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        //SchedulerFactoryBean startQuertz = (SchedulerFactoryBean) applicationContext.getBean("startQuertz");
        //startQuertz.start();
        //StdScheduler scheduler =  (StdScheduler)applicationContext.getBean("startQuertz");
        try {
            if(!scheduler.isStarted()){
                scheduler.start();
            }else{
                logger.debug("Now Job is started. ");
            }
        } catch (SchedulerException e) {
            logger.error("Start Quartz 出现错误",e);
            return "1";
        }
        //request.setAttribute("user",user);
        return "0";
    }
    @ResponseBody
    @RequestMapping("/stop")
    public String stop(String id, HttpServletRequest request){
        logger.debug("stop QuartzController...");
        //WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        //SchedulerFactoryBean startQuertz = (SchedulerFactoryBean) applicationContext.getBean("startQuertz");
        //startQuertz.start();
        //StdScheduler scheduler =  (StdScheduler)applicationContext.getBean("startQuertz");
        //try {
        try {
            if(scheduler.isInStandbyMode() || scheduler.isShutdown()){
                scheduler.standby();
            }else{
                logger.debug("Now Job is stopped. ");
            }
        } catch (SchedulerException e) {
            logger.error("Stop Quartz 出现错误", e);
            return "1";
        }
        //} catch (SchedulerException e) {
        //    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        //}
        //request.setAttribute("user",user);
        return "0";
    }

    //public SchedulerFactoryBean getStartQuertz() {
    //    return startQuertz;
    //}
    //
    //public void setStartQuertz(SchedulerFactoryBean startQuertz) {
    //    this.startQuertz = startQuertz;
    //}
}
