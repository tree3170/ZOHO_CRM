# 这是一个记录关于平常遇到的问题的一个MarkDown

##20160901 confrirm mapping from ken
  ### 解决：
  * 1.关于dropdown-->统一使用inputbox
  * 2.新定义字段用英文
  * 3.关于invoice和SO主题，因为ERP不提供，所以使用Invoice编号和SO编号代替
  * 4.关于产品、发票、订单所有者，得使用lastEditBy(所有者就是发票、订单中的LatestEditBy(User表中的UserName))
  * 5.关于每个功能的ID，做成一个隐藏字段
  ###问题：
  * 1.ERP在每个function中的ID，能不能做成一个readable的在CRM上？
  * 2.发货单编号等，这个字段是否可以修改，如果不可以，那么我需要重新定义一个字段
  * 3.查找类型的字段是否可以被API修改：经测试是可以用ID+Name修改的
  * 4.关于发票和销售订单中的的产品部分有很大的问题，因为ZOHO这边关于产品在发票中的显示是不能自定义调整字段，


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
