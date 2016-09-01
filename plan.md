# 20160831 晚
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



