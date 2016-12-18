# 这是一个记录关于项目中所有问题汇总的MarkDown

##遇到问题：
* 1. 关于Product Details中的一些字段的解释；关于定价不能修改的问题？
     Total:金额 --> 金额(Total) = 定价(Unit Price) x 数量(Quantity)
     Net Total:总计 --> 总计 = 金额 - 产品折扣(Discount) - 产品税（Tax）
     Sub Total:订单小计 --> 订单小计 = 总计和
     Grand Total:订单累计 --> 订单累计 = 订单小计(Sub Total) - 订单折扣(Discount) - 订单税（Tax） - 订单调整(Adjustment)
     Total After Discount:这是个什么字段，没有在页面显示？
* 2.  > Q: 如果某个字段为null，会被更新吗？
      A: 经测试，只有全为小写的"null"才不会更新，否则会更新

* 3.可能的做法
  a. 取出db中所有最近修改过的record（最近更新的字段，设置一个当前已经更新了的时间，第一次为0，以后每次成功需要保持到文件或者db）
  b. 遍历每条数据，取出id和lastedittime，并到CRM中查询
     I：如果存在CRM中，则判断lastEditTime有没有变化：如果不同，则组织db的数据并更新之
     II: 如果不存在CRM中，则insert到CRM中
  c：取出CRM每条数据，做成一个集合id，查看id是否存在与db中的数据，如果不存在，删除CRM数据

  d.注意只匹配导入的用户（暂时13个用户）、

* 4. 设置拥有者
   a.有Name无ID--》无论存不存在Name，都不会更新拥有者
   b.无ID有Name--》 存在ID--》更新，不存在ID，出错
   c.同时拥有Name和ID--》 ①存在ID，Name不对--》按ID更新
   						  ② 只要ID不存在无论Name是否存在--》出错：4401
   						  ③ ID和Name都存在且对--》正常更新
* 5. 关于13个人的账号
   a. accounts.properties: ERP Name 和 ZOHO ID
      ①、拿到所有的ZOHO用户：getUser--》放到Cache中
      ②、过滤： 拿到DB中的lastEditBy，如果Cache中存在的，DB中这条记录有效

* 6. 必需增加一个手动导入某个功能，可以选择Module，选择时间或者ERP ID，去导入数据  【DONE】
	条件：Module+Time+ERP ID  -->这个周末完成,可以分批也可以分个个来做
* 7. 增加一个选Module，选时间，选ERPID的导出DB数据功能
* 8. 第一次load数据和删除数据时，一定不能删掉客户自己添加的Account（也就是ERPID is null的数据）和交易数据
* 9. 增加一个邮件发送功能，也就是再配置一个Quartz（大概每10天左右发送最近的report到我的邮箱，通过查询ZOHO_EXEC_REPORT表）
    a  邮箱加密
* 10 .定期清除log日志

##项目
* 1. > Q: 关于用户数确认，暂时只支持13个人
     A: 必需由客户给出用户list（做成一个properties，可以自己添加的配置文件），需要校验人数通过ZOHO API(getUsers)
* 2. > Q: Schedule : 5分钟
* 3. > Q: 小心ERP的货品折扣是百分比
* 4. 关于log，错误的log记录到另外的文档中
* 5. 最后有邮件发送功能，当出现错误时发送错误文档到我自己的邮箱，方便查看（开发阶段设置邮件发送标志为打开）
* 6. 重新reload有变化的properties配置文件
* 7. 缓存（暂定）
* 8. Druid每天的执行情况 和Report
* 9. 一定要多注意多线程变量共享情况
* 10. 定时删除日志（暂定30天）
* 11. 在日志中需要打印线程ID（http://www.it610.com/article/468905.htm）
* 12. 沿用装饰模式修改HandleModule等类
* 13. 增加接口，就是说客户直接可以搜索，拿到DB中正确的结果-->以便到时候数据出问题了方便调查是哪条数据出问题
* 14. Report
* 15. API执行次数消耗【confirm】
    a. update/delete必需是每个
    b. update模块Quotes、SO、Invoice时，需要先删除，再add-->
    b. house keep , 搜索每个product存在与Quotes、SO、Invoice，然后也是每条执行删除




