/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   FieldsCollector.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.refect.vo;

/**
 * darlen.crm.refect.vo
 * Description：ZOHO_CRM
 * Created on  2016/10/03 15：20
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        15：20   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FieldsCollector {

    public static Map<String, FieldEntity> getFileds(Object object)
            throws SecurityException, IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, FieldEntity> map = new HashMap<String, FieldEntity> ();

        for (int i = 0; i < fields.length; i++) {

            Object resultObject = invokeMethod(object, fields[i].getName(), null);
            map.put(fields[i].getName(), new FieldEntity(fields[i].getName(), resultObject, fields[i].getType()));
        }

        return map;
    }

    public static Object invokeMethod(Object owner, String fieldname,
                                      Object[] args) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class ownerClass = owner.getClass();

        Method method = null;
        method = ownerClass.getMethod(GetterUtil.toGetter(fieldname));

        Object object = null;
        object = method.invoke(owner);

        return object;
    }

}
