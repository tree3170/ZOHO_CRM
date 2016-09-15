# 这是一个记录关于项目中所有问题汇总的MarkDown

##遇到问题：
1. 关于Product Details中的一些字段的解释；关于定价不能修改的问题？
     Total:金额 --> 金额(Total) = 定价(Unit Price) x 数量(Quantity)
     Net Total:总计 --> 总计 = 金额 - 产品折扣(Discount) - 产品税（Tax）
     Sub Total:订单小计 --> 订单小计 = 总计和
     Grand Total:订单累计 --> 订单累计 = 订单小计(Sub Total) - 订单折扣(Discount) - 订单税（Tax） - 订单调整(Adjustment)
     Total After Discount:这是个什么字段，没有在页面显示？
2.  > Q: 如果某个字段为null，会被更新吗？
      A: 经测试，只有全为小写的"null"才不会更新，否则会更新

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



