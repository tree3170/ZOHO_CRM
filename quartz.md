#Quartz Markdown
http://localhost:8080/zoho/quartzMain.jsp

1. quartzMain.jsp

2. QuartzController.java
   注意这里装配不是装配SchedulerFactoryBean startQuertz
   而是装配：Scheduler scheduler;
    @Autowired
    Scheduler scheduler;

3. spring-quartz.xml
SchedulerFactoryBean-->CronTriggerFactoryBean-->MethodInvokingJobDetailFactoryBean-->QuartzManager

4. QuartzManager.java


参考：
http://www.cnblogs.com/daxin/archive/2013/05/29/3107178.html
