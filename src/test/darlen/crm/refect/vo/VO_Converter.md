## 介绍：这个工具类是偶尔发现的，现在想来，很好用
参照这个，以后要尝试做出一个把ResultSet映射到相应Model上的功能...

有时候，需要动态获取对象的属性值。
比如，给你一个List，要你遍历这个List的对象的属性，而这个List里的对象并不固定。比如，这次User，下次可能是Company。
e.g. 这次我需要做一个Excel导出的工具类，导出的批量数据是以List类型传入的，List里的对象自然每次都不同，这取决于需要导出什么信息。
为了使用方便，将对象的属性名与属性值存于Map当中，使用时就可以直接遍历Map了。
此次的思路是通过反射和Getter方法取得值，然后记录在一个Map当中。

## 结果
    {
    	username=FieldEntity[fieldname=username,value=user109,clazz=classjava.lang.String,errorMsg=[]],
        registrationDate=FieldEntity[fieldname=registrationDate,value=MonOct0315: 22: 51CST2016,clazz=classjava.util.Date,errorMsg=[]],
        eBlog=FieldEntity[fieldname=eBlog,value=http: //www.cnblogs.com/nick-huang/,clazz=classjava.lang.String,errorMsg=[]],
        password=FieldEntity[fieldname=password,value=pwd109,clazz=classjava.lang.String,errorMsg=[]]
   }


## 详情参考：
http://www.cnblogs.com/nick-huang/p/3831849.html