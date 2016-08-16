/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   TestFields.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package java.darlen.crm;

/**
 * java.darlen.crm
 * Description：ZOHO_CRM:这是一个测试Fields的一些方法
 * Created on  2016/08/16 08：23
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        08：23   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class TestFields {
    public static void main(String[] args) {
        TestFields tf = new TestFields();
        tf.testClassPath();
    }

    public void testClassPath(){
        System.out.println(this.getClass().getResource(""));
    }
}
