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
   * 调用delete，删除的数据在回收站？怎么理解？如何roll back？
   *Unit Price 与 List Price 分别表示什么？因为我看List Price其实是表示单价那么Unit Price表示什么
   * 每个模块的主键是什么？什么情况下判断是duplicate？

## 问题： Ken
   * 确定日期格式问题，这个是datetime的表示的【datetime】
   * 关于销售订单拥有者，产品拥有者，报价拥有者，发票拥有者--》LatestEditBy【已确定：根据ken的下面comment】
   "千萬別用你自己為创建或者修改的用户，因為公司是按誰開單來發放Bonus的，所有者就是发票、订单中的
   LatestEditBy(User表中的UserName)。 要找CRM中的所有者，你應該將User表發給Elvis或Gary，請他幫你填上CRM賬戶用作Mapping"
   * 关于销售订单，报价或者发票中的customer信息，id和name不能被修改，像adress/tel/mail之类的是可以修改的【已确认】
   * 滙入資料到CRM的流程應該是， Customer表 , Item表, 報價單, 訂單, 發票【已确认】

   # 20160923
   * 是如何计算SO的价格的？需要找Ken confirm
   * LatestEditBy在ERP系统中能被修改吗？因为我看产品等module中存的是name而不是ID，那么如果修改了，产品等module中的所有模块会不会一起跟着修改？




## 问题： 客户
   * 确定UI
   * 确定账户13个账户人

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
