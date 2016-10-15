
# 交付
* 1. 客户所有DB数据需要给我
* 2. 环境测试（邮件，properties的读取和写入）


# 20160926 晚
* 1. 已经完成了早上的第一二点
* 2. log的记录，邮件的发送，Quatz的运用得尽快加入系统来

# 20160926 早
* 1. HandleInvoice早上完成了对数据库字段的填写，晚上完成增删改查的测试
* 2. 今天一定要转移到matrix上并且测试完
* 3. 如果有可能，把log也需要整理下
* 4. 尽快在这一两天把Quatz运用进来，搭建起Spring MVC环境
* 5. 这周如果在公司有时间先需要研究Quatz，然后研究Spring发邮件功能，尽量把发邮件功能整理进来

#20160924 晚
*1. 今天一天完成了对HandleSO的优化和一些调试，现在基本能保证能和正常运行环境一致，包含增删改查，同时编写了HanldSO的思维导图
*2. 明天同样优化HandleAccount的方法和编写思维导图，并且完成接下来的HanleProduct和HandleInvoice代码的编写，
争取抽取类似的接口或者方法，大家共同调用
*3. 环境需要从tree3170正式转入Matrix
*4. 需要准备对db的增删改查的编写，同时当更新了数据之后，要记录新的更新成功的时间
*5. 日志的编写，需要处理不同情况写入不同文档中
*6.
这周需要将这4个function的test方法全部完成，有时间的话注意13个账户的问题，如果没时间放到下周
下周需要把这4个function全部转入真正的环境中，并加入邮件发送功能
下下周也就是国庆需要放入Quatz里面去跑，
最后一周就需要检查和log的记录情况


# 20160923 晚
*1. 继续优化了HandleSO的add方法（最大只能添加100条）
*2. 明天完成脑图，并且编写HandleProduct和HandleInvoice
*3. 准备log的编写：如何处理log

## 20160923
* 完成对Account和SO的update和delete的操作，并尝试优化和写markdown并画出图形(百度脑图：http://naotu.baidu.com/)关于这两个部分的逻辑
* 这周末需要完成HandleProduct和HandleInvoice的编写
注意：
1. 因为update和delete是只能单条，所以这部分需要优化
2. 同时getRecord一次也只能拿到200条，这个程序也需要优化
3. Add最大也只支持100， 这也是需要优化的地方




##20160919
* 明天和王继确认些问题，参照daily.md
* 明天继续完成HandleSO.java

## 20160916
* 已确认UI修改【Gary】

## 20160915 中秋
* 完成客户账号新界面的修改【TODO】,明天微信群里面找客户确认--> 20160916已找Gary确认
* 完成对新界面的增删改查【95%】
* 20160916完成对新的界面的java与XML的互相转换（Accounts/SalesOrders）
* <Strong>***关于产品中的几个字段确认(详情参考Q&A Daily)</Strong> : 大致已经确定
* 这周结束前必需研究出Druid用法，并编写MD和应用在项目上，
* 下周开始需要正式读取db数据并完成数据的导入
* 找Ken确认时间和日期格式

## 20160903
* 继续完整修改对SO和Leads的注释
* 把GitHub上的项目转移到Bitbucket上

## 20160831 晚
* 完成LeadsRdsMockTest.java，
* 20160901 对product detail部分代码优化改良

## 20160829 早：
* 20160828的作息推迟2天
* 20160829-20160830 : 制作ERP和CRM的mapping excel和截图发给Ken



##20160828
* 1. 抽取公共的Post method（executePostMethod：map）和一些公共的Constants, 参考：LeadsRdsTest.java   【finish】
* 2. 完成对Leads从db读取到java object，然后拿到并对比CRM中数据，
1.拿到db record，解析出crm中的record，查看时间，如果db时间>record时间则update（db record映射到对象中，再通过jaxb转换为xml，post到CRM去更新数据）
> db 数据如何和java object的mapping问题（）， 详细见LeadsRdsMockTest.java
* 3. 晚，还没完成对从数据读取之后然后映射到xml上最后发送到ZOHO，明天早上继续完成。


##20160827 晚
* 1. 明天白天利用jaxb生成的xml做一条增删改查的记录
```
    【最主要是对生成的xml做校验，<strong>`如何取到值塞进对应属性`</strong>，难度5星】，详细见LeadsRdsMockTest.java
```
* 2. 搭建起spring mvc环境，搭建起druid环境【20160828-20160830】

## 20160827 早：
* 1. 完成Q&A中的jaxb中value为空不设置到java对象中的问题
* 2. 早上已经完成了对Leads的解析成Object，今天至少完成完成对SO的解析
* 3. 白天在公司有多的时间完成对Jaxb的一些语法，历史来历等解读,具体解读方式参考!

## 20160826 晚
* 1. 明天一定得完成xml to java bean的映射,使用jaxb2
    【20160827早：50%,已经实现了Leads的解析，接下来完成SO】
    【20160827晚：100%，完成SO解析，比较曲折关于对product detail转换为Java object】
* 2. 下周得定义出整套Spring MVC方案，Quartz方案

## 20160825 晚，
* 1. 学习markdown语法，争取每天能有一些东西和进度更新在我们的这个note的folder下 -->in progress
* 2. 明后两天尽量完成利用dom parse xml result--》Map--》parse 成 Object（Leads,SO）--》xml(如果为空则不parse到xml中，不包含ID)
* 3. 大后天建立起

## 20160822:
* 1.写一个基于Json解析Leads and Invoice的result实例【pending】 --> 转为【todo】，因为后面可能不会用到解析json的result数据
* 2.写一个基于XML解析Leads and Invoice的result实例【todo】 --> 20160826 【finish】，详情参考XmlParse类
```javascript
invoice=报价+产品+客户（Accounts）+联系人（Contacts）
```

## 参考在线MarkDowm编辑器
* http://jbt.github.io/markdown-editor
* http://mahua.jser.me/
* https://www.zybuluo.com/mdeditor



