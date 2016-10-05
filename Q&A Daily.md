# 这是一个记录关于每天遇到的问题和解决的问题的MarkDown

## 问题： 王继
   * product detail能修改吗？【不能修改】
   * 自定义的字段如果国际化？【不支持】
   * add可以同时多条，那么delete和update呢？因为只传id，所以是不是代表只能一次删除或者更新1条数据？不支持批量删除或者修改操作？如果不支持，但是每天的最大API执行量为3000，这个该如何处理？
     deleteRecords ：您可以使用这种方法删除所选的记录（您必须指定记录的唯一ID)并移动到回收站。此方法适用于 所有模块。但使用这种方法，您不能将记录永久从您的帐号删除。
     getRecords拿到多条数据，跟数据库匹配，去做更新删除添加操作，例如：当更新的list中>1， 那么我是否需要每一条去执行updateRecords？
     【add最大支持100条，update/delete只能每次一条】
   * 如何确定每个功能的记录数？我该如何判断我的数据是超过还是小于200条呢？【无API可以取记录数，只能每次遍历100条试试，最大200条】

   # 20160923
   * 调用delete，删除的数据在回收站？怎么理解？如何roll back？https://crm.zoho.com.cn/crm/ShowRBList.do
   * Unit Price 与 List Price 分别表示什么？因为我看List Price其实是表示单价那么Unit Price表示什么
   * 每个模块的主键是什么？什么情况下判断是duplicate？  因为我每次插入几乎相同的数据都能成功

   # 20161002
   * 关于产品存在与其他module时不能删掉【估计只能手动先删除关联的模块，最后再删除产品。。。】， 还有product detail不能删除的问题
   不能删除Invoice和Quotes
   https://crm.zoho.com.cn/crm/private/xml/SalesOrders/deleteRecords?id=80487000000100803&authtoken=00f65ee9c91b4906dbf4c1bd46bb1452&scope=crmapi
   * update模块Quotes、SO、Invoice时，如果有模块中的product删除情况，那此时API是做不到删除某个产品的，比如说以前三个产品，现在改成了2个产品
   * selectedColumns 不能包含Product里面的字段
   * 如果我修改了Module的名字，后面发现不支持国际化，所以想重用以前Module的名字，该怎么办？

## 问题： Ken
   * 确定日期格式问题，这个是datetime的表示的【datetime】
   * 关于销售订单拥有者，产品拥有者，报价拥有者，发票拥有者--》LatestEditBy【已确定：根据ken的下面comment】
   "千萬別用你自己為创建或者修改的用户，因為公司是按誰開單來發放Bonus的，所有者就是发票、订单中的
   LatestEditBy(User表中的UserName)。 要找CRM中的所有者，你應該將User表發給Elvis或Gary，請他幫你填上CRM賬戶用作Mapping"
   * 关于销售订单，报价或者发票中的customer信息，id和name不能被修改，像adress/tel/mail之类的是可以修改的【已确认】
   * 滙入資料到CRM的流程應該是， Customer表 , Item表, 報價單, 訂單, 發票【已确认】


   # 20160923
   * 是如何计算SO的价格的？需要找Ken confirm【待确认】
   * LatestEditBy在ERP系统中能被修改吗？因为我看产品等module中存的是name而不是ID，那么如果修改了，产品等module中的所有模块会不会一起跟着修改？【待确认】
   * 明天对SO、Invoices数据的正确性需要找用户和Ken确认正确性【待确认】
   * 关于Quotes，SO，Invoices中的Product是否能删掉在ERP中？？【待确认】

   #20160930
   * 关于LatestEditTime和CreationTime为空的情况【待确认】




## 问题： 客户
   * 确定UI
   * 确定账户13个账户人

# 20161005
* 1. 跑所有数据【doing，优先级Ⅲ】-->前提是写出删除程序能删除所有数据【删除数据DONE】--》明天跑所有数据
* 2. 优化log【doing，优先级Ⅳ】--》优化了一部分，需要跑所有数据时再优化
* 3. UI更改【doing，优先级Ⅱ】 --> 20161006
* 4. 后天：log分类问题  --> 20161006
* 5. Quartz可以随时触发问题，当遇到不可遇因素，能否主动停止Quartz
* 6. 至少需要2个自定义异常类： 1--> 前端执行情况  2-->最后面post的时候如果出错，因为这个时候是会有执行结果的（想了下或许不要）
* 7. jaxb -->CDATA 【done】
* 8. 关于Remark这个字段需要确认【CONFIRM】
* 9. 完成一个删除功能，【doing，优先级Ⅰ】
	先判断Product是否有效（ERP ID是否与DB一致），
	如果无效，则拿出ZOHO ID，分别查询在Quotes、Invoices、SO中存在此产品的模块，
	如果product ID存在于这些模块中，则先删除这些模块，最后再删除product模块
* 10. 每次到ZOHO做完操作后，记录时间到file文件中【doing ，优先级Ⅱ】
* 11. 解析返回的response是成功还是失败【】-->明天完成commonPostMethod
* 12. 明天完成对LastestEditTime的判断build2Zoho3PartObj  [TODO]

# 20161004
* 1. 删除顺序(invoice/so/quote/product/account)【待定】【API Import：DONE】
【待定，因为Product使用APi删除不了的情况是ERP ID不在DB中或者为空，这种情况下是不存在的，
 例外：如果客户直接使用UI创建的话，这种情况不予维护。 为了区分这种情况，添加一个字段API Import，标志是否使用API导入【done】】
* 2. 获取FilePath，取不到就换另外一种方式获取【DONE】
* 3. 环境检测【doing】，主要是filepath和DB连接情况【DONE】
* 4. report 调整【start time,end time,add,update,delete】【DONE】
* 5. UI confirm【95%，除了Remark字段】
* 6. 增加接口，就是说客户直接可以搜索，拿到DB中正确的结果-->以便到时候数据出问题了方便调查是哪条数据出问题【TODO】
* 7. 当Prod ID或者AccountsID或者userID为空时， throw exception，因为就算发到ZOHO也是会出错【doing】
* 8. 关于invoice/so/quote需要删掉了才能添加-->没有更新，因为产品如果删除了，就更新不了--》confirm to Ken--> 有没有可能删除产品？？？
* 9. quartz 在我需要的时候启动而不是Web启动时就启用【TODO】
* 10 有时间写一个公共的异常类，有可能不是在这个项目完成【TODO】
* 11. 晚上继续跑整个程序，然后优化继续log...【doing】


# 20161003
* 1. 实现ZOHO_EXEC_REPORT：这是一个关于report的表，里面会记录操作时间、Insert失败次数、Update失败次数、Delete失败次数、ModuleName【done】
* 2. 每次所有程序执行***完***，更新执行时间，以便每次调用是LatestEditTime是否在执行时间之后，如果是，则可以放入ZOHO列表中
* 3. 实现Connection线程池为SQL
* 4. 引入Spring MVC，先把框架搭建起来，然后测试数据库连接情况，测试各个properties等文件是否可以拿到，然后搭建一个简单的页面，点击按钮，执行一次操作（今晚的工作）【80%】
* ， 还是先引用Quartz，等这个ok之后我就有时间慢慢做接下来的一些东西
* 5. 明天一定要完成Quartz引入和Email框架的引入

# 20161002
* 1. LastEditTime修改时间这个判断还没加上【20161003】【一定完成20161003】
* 2. 每次到ZOHO做完操作后，记录时间到file文件中--> 周末完成【doing】【一定完成20161003】
* 3. 完成Quotes的测试【20161003】
* 4. 明天找客户确认UI问题和13个账户问题【确认好了把UI修改好】
* 5. 把Spring Quatz引用进来（前提是log必需优化，同时log必需分类），明天或者后天完成，有时间研究下Email
* 6. 找Ken把Quotes的相关数据拿过来【doing】
* 7. 尽快把Quatz引进来 【优先级1】  【done】
* 8. 关于report问题，每次add多少条，update多少条，delete多少条，分别成功多少，失败多少【优先级2】
* 9. 关于产品存在与其他module时不能删掉，但是API显示删除成功【估计只能手动删除相应其他模块后再才能删除。。】， 还有product detail不能删除的问题

#20161001
* 1. LastEditTime修改时间这个判断还没加上【20161002】
* 2. 完成对DBUtils中的Product，Invoice，SO的测试【done】
* 3. 每次到ZOHO做完操作后，记录时间到file文件中--> 周末完成【doing】
* 4. 明天找客户确认UI问题和13个账户问题
* 5. 把Spring Quatz引用进来（前提是log必需优化，同时log必需分类），明天或者后天完成，有时间研究下Email


# 20160929(优先级按*号长度)
 * 1. getRecords时最大200条这个还没有做限制【doing】 --> 【done】 20160929
 *    每次取100条，解析为Response对象之后,就可以拿到所有的条数，判断条数如果<100，则代表已经取完
 * 2. 把Spring应用进来
 * 3. 实例化本类以后将使用Spring
 * 4. *****************不同module生成的log放到不同的日志文件中【已完成样例，20160930晚上应用到系统中】--> 20160930 【doing】
 * 5. 使用适配器模式、单例模式等改造【doing】
 * 6. *************当超过13人该怎么办,会出现怎样的Error【提供的文件超过人数，取前面13人】-->周末完成
 *     当人数properties文件有变动时，重新reload：https://crm.zoho.com.cn/crm/private/xml/Users/getUsers?authtoken=00f65ee9c91b4906dbf4c1bd46bb1452&scope=crmapi&type=AllUsers
 * 从ZOHO更新最新的人员列表进入缓存，以后可以直接用
 * 7. ***********************当API使用次数超过，会出现怎样的Error【doing,稍后查询尝试4000次，遍历最大次数后会出现怎样的情况】--》20160930：晚上11：30开始跑【doing】
 * 8. ********************DB的操作，查找-->  20160930完成，如果完成不了，周末一定要完成  ********今天到公司去做连DB操作，晚上正式连接  20160930【doing】
 * 9. *****************每次到ZOHO做完操作后，记录时间到file文件中--> 周末完成【doing】
 * 10. ********Spr4ing Quatz【周末开始】--》周末完成
 * 11. ********Quotas应用--> 找用户确认并周末完成
 * 12 .****确认UI【尽量在这周末】--》周末完成【doing】
 * 13. ***邮件发送周末，如果完不成国庆前2天一定要完成了
 * 14. report-->每天执行情况，成功多少失败多少
 * 15. 连接池

# 20160928
* 1. 今天基本完成了对4个功能的重构，还算满意，不过以后多看设计模式，把里面的精髓应用进来
* 2. 明天早上如果起得来，需要把comment写好，把log应用，把TODO里面尽量能完成：比如说只能13个人，读200条限制，增删改
* 3. 后天完成DB的读写，并且按自己理解把一些数值填入，等项目接近完工找KEN再确认数值

# 20160927
* 1. 白天遇到了困难关于发送邮件，明天继续研究
* 2. 明天白天需要研究IST中的适配器模式，晚上应用到自己的项目
* 3. 4大功能已经移到了正式环境中，并且晚上refactory 项目，明天早上有时间的话继续refacory项目
* 4. 关于log在不同形式下需*要记不同的log，这个需要加快完成（比如分module，分error case，分时段等）

## 20160926
* 当出错的时候，解析出错的信息【TODO】<response uri="/crm/private/xml/Products/getRecords"><nodata><code>4422</code><message>There is no data to show</message></nodata></response>

## 20160926 晚
* 1. 完成了对Account、Product、SO、Invoices的所有测试操作并且已经转移到Matrix平台上
* 2. 明天对SO、Invoices数据的正确性进行调试，并需要找用户和Ken确认正确性
* 3. log的记录，邮件的发送，Quatz的运用得尽快加入系统来

## 20160926 早
* 1. HandleInvoice早上完成了对数据库字段的填写，晚上完成增删改查的测试
* 2. 今天一定要转移到matrix上并且测试完
* 3. 如果有可能，把log也需要整理下
* 4. 尽快在这一两天把Quatz运用进来，搭建起Spring MVC环境
* 5. 这周如果在公司有时间先需要研究Quatz，然后研究Spring发邮件功能，尽量把发邮件功能整理进来
## 20160923
   * 确认将test转移到Matrix程序中

## 20160921
   明天找Bruce和Eddie确定db数据能否导入到SQL Server 2005【已确认】

## 20160918
   ###问题：王继
   * 如何确定每个功能的记录数？ getRecords貌似只能一次性最大取到200条？
   * product detail能修改吗？

## 20160915 关于产品中的几个字段确认
  ###问题
  > Q: 关于产品中的金额，总计，小计，累计分别对应什么字段，有什么区别, 可不可以修改Product detail里面的字段；如何处理税，页面不可以修改
    A: 经本人测试，大致已经确认，可能还有小小的东西需要找ZOHO确认
     Total:金额 --> 金额(Total) = 定价(Unit Price) x 数量(Quantity)
     Net Total:总计 --> 总计 = 金额 - 产品折扣(Discount) - 产品税（Tax）
     Sub Total:订单小计 --> 订单小计 = 总计和
     Grand Total:订单累计 -->订单累计 = 订单小计(Sub Total) - 订单折扣(Discount，不是百分比，是数值) - 订单税（Tax） - 订单调整(Adjustment)
     Total After Discount:这是个什么字段，没有在页面显示？
     Unit Price 与 List Price 分别表示什么？因为我看List Price其实是表示单价那么Unit Price表示什么
    Q:因为导出到excel中的数据为乱的，所以必需找Ken确认时间和日期格式

##20160901 confrirm mapping from ken
  ### 解决：
  * 1.关于dropdown-->统一使用inputbox
  * 2.新定义字段用英文
  * 3.关于invoice和SO主题，因为ERP不提供，所以使用Invoice编号和SO编号代替
  * 4.关于产品、发票、订单所有者，得使用lastEditBy(所有者就是发票、订单中的LatestEditBy(User表中的UserName))
  * 5.关于每个功能的ID，做成一个隐藏字段
  ###问题：
  * 1.ERP在每个function中的ID，能不能做成一个readable的在CRM上？Tree经确认，可以做成一个隐藏的字段
  * 2.发货单编号等，这个字段是否可以修改，如果不可以，那么我需要重新定义一个字段：Tree确认
  * 3.查找类型的字段是否可以被API修改：Tree经确认，测试是可以用ID+Name修改的
  * 5.关于客户中的Contact和direct，你说放入到联络人中，但是你给的表和数据中没有联络人啊，但是ZOHO这边的确是有联络人这个function的，但是我觉得可以直接录入ERP数据中在客户表中的contact名字和direct，用不到ZOHO中联络人这个function。需Ken确认
  * 4.关于发票和销售订单中的的产品部分有很大的问题，因为ZOHO这边关于产品在发票中的显示是不能自定义调整字段，需Ken确认
 20160926:
  * 6.  关于发票和销售订单中的CreateUserID,这个是不是跟LastEditBy一一对应，有没有可能不同？
  而且在ERP系统中，是不是只有属于哪个人的发票或者销售订单才能去修改，有没有可能其他人修改？
  *7. 关于发票中的email为“CusEmail”的情况如何处理


## 20160828(DB对象到Record对象直接的mapping关系解决)
    > Q: db 数据如何和java object的mapping问题?  UpdLeadsRdsTest.java
      A: 比如说更新<strong>(需要先删除再插入)</strong>某一条Leads数据
       * 1. 先列出一个db的javaObject（DBLeads.java）
       * 2. 生成CRM中Record的Object（RdLeads.java），仅仅只需要获取必要的字段，如果说id，name，modify time,created by
       * 3. 准备一个properties(dbRdLeadsFieldMapping.properties)
       * 4. 利用反射原理, 遍历 DBLeads对象，取出每个fieldname和fieldVal , 并放入一个LeadsDBMap中（如果没值或者为空，则不变：：注意）
       * 5. 遍历LeadsDBMap，将key value分别设入FL（field name,field value）从而得到List<FL> fls，并组成response对象
       * 6. 利用jaxb将response对象转换为xml，post到ZOHO，从而得到最终结果

    > Q: jaxb转换SO对象时的问题(product detail)。
      A: 因为FL对象既可以是string对象，也可以是Product detail对象，所以我的做法是：
        >  a. 当由result的response xml转换为java对象response时，将对应的product detail对象的FL元素替换成pds，所以整个Row现在有三个对象，一个val，一个fl，一个pds
        >  b. 当由reponse对象要转换为xml时，先转换成xml，然后直接替换pds为FL

    > Q: 如何区分ERP数据是删除、更新还是添加了，比如Leads
      A: 1. 必需把CRM中的Leads的ID，modified time和db中的id，modified time放入Session Cache中，
         2. 取出数据库最新更新的数据，比如说5分钟前或者说是1天前的数据
         3. 对db取出的数据每条遍历，这里可以check到ERP修改的数据和插入的数据，check不到删除的数据，并更新db cache
              a. 如果db中id在session cache中找不到，那么直接放入插入map中
              b. 如果db中id存在于session cache中，那么查看修改时间，如果db的修改时间>=cache中的，则放入修改的map中，否则忽略
         4. 对crm cache中的数据每条遍历，如果id不存在与db的cache，则把这条数据加入到删除的Map中， 移除crm中cache这条记录，并更新cache




## 20160827
    Q:用了jaxb之后，能不能设置空的或者null的value不设置到java对象中，详情参考JaxbLeadsTest.java
    A:

## 20160826
    Q:ZOHO技术人员（Andy）：关于zoho中文乱码问题，当我使用TestGetLeadsRds去update数据时，出现了中文乱码问题<FL val="Salutation"><![CDATA[先生]]>
    A: post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Constants.HTTP_POST_PARAM_UTF8);
    Q:ZOHO咨询人员(许)：关于账户使用的人（15），客户（），联系人区别
    A: 客户和联系人是一对多的关系， 多个联系人可以对应一个客户，其实可以看作客户是一个公司，然后联系人是公司接洽的人员
    Q:Andy:Last Activity Time 与Modified time 有什么区别
    A:
    Q:测试关于系统字段能不能修改问题：modified time , modified by , created time ,create by
    A:使用TestGetLeadsRds类中update方法，最终结果是虽然这些字段更新不会成功，但是不影响整个结果
    Q: 关于产品： Confirm Unit Price and List Price 区别  , confirm 累计/总计、小计的区别？？？
    A:
