/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   StringUtils.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.util;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * darlen.crm.util
 * Description：ZOHO_CRM
 * Created on  2016/08/25 00：02
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        00：02   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class StringUtils {
    private static Logger logger = Logger.getLogger(StringUtils.class);
    private static String formatPatternDefault = "#0.00";
    private static DecimalFormat df = new DecimalFormat(formatPatternDefault);

    /**
     * 转化字符串数组为一个不为空的字符串（Converts a string to a non-null value.）
     *
     * @param inString the string to be converted
     * @return the result - if inString is null or "null" or "", return ""; else return the trimmed string.
     * @publish
     */
    public static String nullToString(String inString) {
        return ((inString == null || "null".equalsIgnoreCase(inString.trim()) || "".equals(inString.trim())) ? "" : inString.trim());
    }

    /**
     * 转化字符串数组为一个不为空的字符串（Converts a string to a non-null value.）
     *
     * @param inString the string to be converted
     * @param defaultString the default returned string when the inString is null
     * @return the result - if inString is null or "null", return the defaultString; else return the trimmed string.
     * @publish
     */
    public static String nullToString(String inString, String defaultString) {
        return ((inString == null || "null".equalsIgnoreCase(inString.trim()) || "".equalsIgnoreCase(inString.trim())) ? defaultString : inString.trim());
    }

    /**
     * 转化字符串数组为一个不为空的字符串（Converts a string to a non-null value.）
     *
     * @param inObject the inObject to be converted
     * @return the result string
     * @publish
     */
    public static String nullToString(Object inObject) {
        return ((inObject == null || "null".equalsIgnoreCase(inObject.toString().trim())) ? "" : inObject.toString());
    }

    /**
     * 转化Object类型为int类型（Convert an object to integer.）
     *
     * @param inObject any number objects such as Integer, Double, etc. A number string (or any object that its toString returns a valid number string) is also acceptable
     * @return the integer value
     * @publish
     */
    public static int nullToInt(Object inObject) {
        int iRet = 0;
        if (inObject != null) {
            try {
                Double temp = new Double(inObject.toString());
                iRet = temp.intValue();
            } catch (Exception e) {
                iRet = 0;
            }
        }
        return iRet;
    }

    /**
     * Parses a string to a string array by the specified delimiter.
     *
     * @param aValue the string to be parsed
     * @param delim the delimiter
     * @return an array that holds the parsed string fragment.
     * @publish
     */
    public static String[] stringToArray(String aValue, String delim) {
        if (aValue == null){
            return new String[0];
        }
        java.util.StringTokenizer strTokenizer = new java.util.StringTokenizer(aValue, delim);
        String[] oRet = new String[strTokenizer.countTokens()];
        int iRow = 0;
        while (strTokenizer.hasMoreTokens()) {
            oRet[iRow] = strTokenizer.nextToken();
            iRow++;
        }
        return oRet;
    }

    /**
     * Represents a string array in a string with the specified delimiter.
     *
     * @param values the string array
     * @param delim the delimiter
     * @return the string representation of the given string array.
     * @publish
     */
    public static String arrayToString(String[] values, String delim) {
        String sRet = "";
        if (values == null || values.length == 0) {
            return sRet;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(values[0]);
        for (int i = 1; i < values.length; i++) {
            sb.append(delim).append(values[i]);
        }
        sRet = sb.toString();
        return sRet;
    }

    /**
     * Sorts the object list by the specified property.
     * @publish
     *
     * @param list the object list to be sorted
     * @param beanProperty the specified property list.
     */
    public static void sort(List list, String beanProperty) {
        java.util.Collections.sort(list, new ECVComparator(beanProperty));
    }

    private static class ECVComparator implements java.util.Comparator,Serializable {
        private String beanProperty = null;

        public ECVComparator(String beanProperty) {
            this.beanProperty = beanProperty;
        }

        public int compare(Object o1, Object o2) {
            int iRet = 0;
            try {
                String value1 = PropertyUtils.getProperty(o1, this.beanProperty).toString();
                String value2 = PropertyUtils.getProperty(o2, this.beanProperty).toString();
                iRet = value1.toLowerCase().compareTo(value2.toLowerCase());
                // logger.info("value1:" + value1 + "<<<>>>value2:" + value2 + " iRet:" + iRet);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(e.getMessage(), e);
                }
            }
            return iRet;
        }

        /**
         * @return the beanProperty
         */
        String getBeanProperty() {
            return beanProperty;
        }

        /**
         * @param beanProperty the beanProperty to set
         */
        void setBeanProperty(String beanProperty) {
            this.beanProperty = beanProperty;
        }
    }

    /**
     * Formats a given value to a string in the specified format pattern and ROUND_HALF_UP.
     *
     * @param input the number string
     * @param pattern refer to java.text.DecimalFormat pattern
     * @return the formatted number string
     * @publish
     */
    public static String formatNumber(Object input, String pattern) {
        return formatNumber(input, pattern, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Formats a given value to a string in the specified format pattern and rounding.
     *
     * @param input the number string to be formatted
     * @param pattern refer to java.text.DecimalFormat pattern
     * @param roundMode the values are in BigDecimal.ROUND_XXX, such as: ROUND_UP,ROUND_DOWN,ROUND_HALF_UP,ROUND_HALF_DOWN
     * @return the formatted number string
     * @publish
     */
    public static String formatNumber(Object input, String pattern, int roundMode) {
        if (input == null)
        {return "";}
        return formatNumber(input.toString(), pattern, roundMode);
    }

    /**
     * Formats a given value to a string in the specified format pattern and rounding.
     *
     * @param input the number string to be formatted
     * @param pattern refer to java.text.DecimalFormat pattern
     * @param roundMode the values are in BigDecimal.ROUND_XXX, such as: ROUND_UP,ROUND_DOWN,ROUND_HALF_UP,ROUND_HALF_DOWN
     * @return the formatted number string, if pattern is null or empty, return input.
     * @publish
     */
    private static String formatNumber(String input, String pattern, int roundMode) {
        if (input == null || input.trim().equals(""))
        {return "";}
        if (pattern == null || "".equals(pattern.trim()))
        {	return input;}

        int decimalLen = pattern.length() - pattern.indexOf('.') - 1;
        BigDecimal d = new BigDecimal(input.trim());
        d = d.setScale(decimalLen, roundMode);

        DecimalFormat format = new DecimalFormat();
        format.applyLocalizedPattern(pattern);
        return format.format(d.doubleValue());
    }

    /**
     * Formats a given double value to a string with the specified precision.
     * Known bug: If the number of the input double digits is greater than 17, the formatted value will be incorrect For example:
     * 11111111111111111.0000 will be formatted to 11111111111111112.0, Double.toString(double) method will return 1.1111111111111112E16, this is the cause.
     *  Note: Don't call String.valueOf(double) to convert it to a string. The number will be formatted as a computer science notation format when the value is larger than 10^7.
     *
     * @param d the number to be formatted
     * @param scale the format scale
     * @return the formatted number string
     * @publish
     */
    public static String formatNumber(double d, int scale) {
        if (scale < 0)
        {return d + "";}
        StringBuffer pattern = new StringBuffer("0.");
        if (scale != 0) {
            for (int i = 0; i < scale; i++) {
                pattern.append("0");
//				pattern = pattern + "0";
            }
        } else {
            pattern = new StringBuffer("0");
        }
        return formatNumber(d + "", pattern.toString());
    }

    /**
     * Trims the left space of the string.
     *
     * @param str the string to be trimmed
     * @return the trimmed string
     * @publish
     */
    public static String leftTrim(String str) {
        if (str == null) {
            return null;
        }

        if (str.trim().length() == 0) {
            return "";
        }

        int strLength = str.length();

        char[] content = str.toCharArray();
        int offset = 0;

        for (; offset < strLength - 1; offset++) {
            char c = content[offset];

            if (c <= ' ') {
                continue;
            } else {
                break;
            }
        }

        if (offset == 0) {
            return str;
        }

        if (offset == strLength - 1) {
            return "";
        }

        return str.substring(offset);
    }

    /**
     * Trim the string.
     *
     * @param str the string.
     * @return trim a string.
     * @publish
     */
    public static String allTrim(String str) {
        StringBuffer sb = new StringBuffer();
        if (str == null) {
            return null;
        }

        if (str.trim().length() == 0) {
            return "";
        }

        int strLength = str.length();

        char[] content = str.toCharArray();
        for (int offset = 0; offset < strLength; offset++) {
            char c = content[offset];

            if (c != ' ') {
//			} else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Trims the right space of the string.
     *
     * @param str the string to be trimmed
     * @return the trimmed string
     * @publish
     */
    public static String rightTrim(String str) {
        if (str == null) {
            return null;
        }

        if (str.trim().length() == 0) {
            return "";
        }

        int strLength = str.length();
        char[] content = str.toCharArray();

        int offset = strLength - 1;

        for (; offset > 0; offset--) {
            char c = content[offset];

            if (c <= ' ') {
                continue;
            } else {
                break;
            }
        }

        if (offset == strLength - 1) {
            return str;
        }

        if (offset == 0) {
            return "";
        }

        return str.substring(0, (offset + 1));

    }

    /**
     * Converts date to string.
     *
     * @param date given date
     * @return date string with format "yyyy-MM-dd HH:mm:ss"
     * @deprecated
     */
    public static String hiddenDateToString(Date date) {
        String returnString = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null)
        {	return returnString;}
        try {
            returnString = sdf.format(date);
        } catch (Exception ex) {
            returnString = "";
        }

        return returnString;
    }

    /**
     * Checks a user authorization.
     *
     * @param user the user to be authorized.
     * @param moduleName the module that the functions belong to.
     * @param objectType the object type
     * @param logicFunctionExpression the multiple logicACL function names, the linker string is "||" or "&&". The linker "&&" is higher level than the linker "||"
     * @return true if authorized, else false
     * @throws com.ecv.util.exception.XPCException authorization exceptions.
     * @publish
     */
//    public static boolean checkAccess(User user, String moduleName,String objectType, String logicFunctionExpression) throws XPCException {
//        String businessFunctionExpression = getBusinessFunctionExpression(moduleName,objectType,logicFunctionExpression);
//        return checkAccess(user,businessFunctionExpression);
//    }


    /**
     * Checks a user authorization.
     *
     * @param user the user to be authorized.
     * @param moduleName the module that the functions belong to.
     * @param logicalFunctionNameExpression the multiple logicACL function names, the linker string is "||" or "&&". The linker "&&" is higher level than the linker "||"
     * @return true if authorized, else false
     * @throws com.ecv.util.exception.XPCException authorization exceptions.
     * @publish
     */
//    public static boolean checkAccess(User user, String moduleName, String logicalFunctionNameExpression) throws XPCException {
//        String businessFunctionExpression = getBusinessFunctionExpression(moduleName,null,logicalFunctionNameExpression);
//        if ((null == logicalFunctionNameExpression) || ("".equalsIgnoreCase(logicalFunctionNameExpression.trim())))
//        {return true;}
//        return checkAccess(user,businessFunctionExpression);
//    }

    /**
     * Checks a user authorization.
     *
     * @param user the user to be authorized.
     * @param businessFunctionNameExpression the multiple business function names, the linker string is "||" or "&&". The linker "&&" is higher level than the linker "||"
     * @return true if authorized, else false
     * @throws com.ecv.util.exception.XPCException authorization exceptions.
     * @publish
     */
//    public static boolean checkAccess(User user, String businessFunctionNameExpression) throws XPCException {
//        boolean isEditable = false;
//        if ((null == businessFunctionNameExpression) || ("".equalsIgnoreCase(businessFunctionNameExpression.trim())))
//        {	return true;}
//        businessFunctionNameExpression = businessFunctionNameExpression.replace('|', ';');
//        if (businessFunctionNameExpression.indexOf(";;") == -1) {
//            isEditable = checkMuiltAndCon(user, businessFunctionNameExpression);
//            return isEditable;
//        }
//        String[] aclFunctions = businessFunctionNameExpression.split(";;");
//        int count = aclFunctions.length;
//        for (int row = 0; row < count; row++) {
//            businessFunctionNameExpression = aclFunctions[row];
//            isEditable = checkMuiltAndCon(user, businessFunctionNameExpression);
//            if (isEditable)
//            {	break;}
//        }
//        return isEditable;
//    }


//    private static boolean checkMuiltAndCon(User user,  String businessFunctionExp) throws XPCException {
//        boolean isEditable = true;
//        String[] aclFunctions = businessFunctionExp.split("&&");
//        ACLExternalService aclExternalService;
////		try {
//        aclExternalService = (ACLExternalService) ServiceFactory.getExternalService(user, ACLExternalService.class);
////		} catch (XPCException ex) {
////			throw ex;
////		}
//        int count = aclFunctions.length;
//        for (int row = 0; row < count; row++) {
//            String businessFunctionName = aclFunctions[row];
//            isEditable = aclExternalService.checkAccess(businessFunctionName);
//            if (!isEditable)
//            {	break;}
//        }
//        return isEditable;
//    }

    /**
     * For internal use only.
     *
     * @param e exception
     * @param request http requeset
     * @deprecated
     */
    public static void addAttrObjectBeDeleted(Exception e, HttpServletRequest request) {
//        if (e instanceof XPCException) {
//            String code = ((XPCException) e).getCode();
//            if (code != null
//                    && (code.equals("bdErrorBdhasbeenremoved") || code.equals("bd.error.bdObjectHasBeenRemoved") || code.equals("TOM-SYSTEM-00101") || code.equals("TOM-SYSTEM-00102") || code
//                    .equals("XPC-10404")))
//            {	request.setAttribute("ObjectBeDeleted", "true");}
//        }
    }

    /**
     * Changes the Long list to a String List.
     *
     * @param longList the Long list.
     * @return the String list.
     * @publish
     */
    public static List longToStringList(List longList) {
        List stringList = null;
        if (longList == null)
        {return stringList;}
        if (longList.size() == 0)
        {return stringList;}
        if (longList.get(0) instanceof Long) {
            stringList = new ArrayList();
            for (int i = 0; i < longList.size(); i++) {
                String a = String.valueOf(longList.get(i));
                stringList.add(a);
            }
        } else if (longList.get(0) instanceof String) {
            return longList;
        }
        return stringList;
    }

    /**
     * Sort the list that containing keys for internationalization in ascending order.
     *
     * @param i18nKeyList the I18N key list
     * @param user the current user in http session.
     * @publish
     */
//    public static void sortI18NList(List i18nKeyList, User user) {
//        List toSortValueOfI18nKeyList = new ArrayList();
//        Iterator iter = i18nKeyList.iterator();
//        Map mapping = new HashMap();
//        while (iter.hasNext()) {
//            String bundledKey = (String) iter.next();
//            // String message4HTML = I18NUtil.getMessage4HTML(user, bundledKey);
//            String message4HTML = I18NUtil.getMessage(user, bundledKey);
//
//            // create a key-value mapping
//            if (mapping.containsKey(message4HTML)) {
//                List valueList = (List) mapping.get(message4HTML);
//                valueList.add(bundledKey);
//            } else {
//                List valueList = new ArrayList();
//                valueList.add(bundledKey);
//                mapping.put(message4HTML, valueList);
//                toSortValueOfI18nKeyList.add(message4HTML);
//            }
//        }
//
//        Locale locale = user.getLocaleSetting().getOrginalLocale();
//        Collator collator = Collator.getInstance(locale);
//        Collections.sort(toSortValueOfI18nKeyList, collator);
//        iter = toSortValueOfI18nKeyList.iterator();
//        i18nKeyList.clear();
//        while (iter.hasNext()) {
//            Object valueOfI18nKey = iter.next();
//            List valueList = (List) mapping.get(valueOfI18nKey);
//            i18nKeyList.addAll(valueList);
//        }
//
//    }

    /**
     * Sort the data model list.
     *
     * @param user the current user in http session.
     * @param dataModelList data mode list.
     * @param dataModleCalss data mode class.
     * @param fieldName field name.
     * @publish
     */
//    public static void sortDataModelList(User user, List dataModelList, Class dataModleCalss, String fieldName) {
//        try {
//            List sortedDataModelList = new ArrayList();
//            String getFieldMethodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
//            Method method = dataModleCalss.getMethod(getFieldMethodName);
//            List sortedFieldList = new ArrayList();
//            Iterator iter = dataModelList.iterator();
//            while (iter.hasNext()) {
//                Object item = iter.next();
//                Object filedValue = method.invoke(item);
//                if (!sortedFieldList.contains(filedValue)) {
//                    sortedFieldList.add(filedValue);
//                }
//
//            }
//            sortI18NList(sortedFieldList, user);
//            iter = sortedFieldList.iterator();
//            while (iter.hasNext()) {
//                Object obj = iter.next();
//                Iterator dataModeItem = dataModelList.iterator();
//                while (dataModeItem.hasNext()) {
//                    Object item = dataModeItem.next();
//                    Object filedValue = method.invoke(item);
//                    if (obj.toString().equals(filedValue.toString())) {
//                        sortedDataModelList.add(item);
//                    }
//                }
//
//            }
//            dataModelList.clear();
//            dataModelList.addAll(sortedDataModelList);
//
//        } catch (Exception e) {
//            //e.printStackTrace(); // TODO
//            if(log.isErrorEnabled()) {log.error(e);}
//        }
//
//    }

    /**
     * Sort the data model list.
     *
     * @param user the current user in http session.
     * @param dataModelList data mode list.
     * @param dataModleCalss data mode class.
     * @param fieldName field name.
     * @publish
     */
//    public static void sortDataModelListCaseSensitive(User user, List dataModelList, Class dataModleCalss, String fieldName, boolean caseSensitive) {
//        try {
//            List sortedDataModelList = new ArrayList();
//            String getFieldMethodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
//            Method method = dataModleCalss.getMethod(getFieldMethodName);
//            List sortedFieldList = new ArrayList();
//            Iterator iter = dataModelList.iterator();
//            while (iter.hasNext()) {
//                Object item = iter.next();
//                Object fieldValue = method.invoke(item);
//                if (!caseSensitive && fieldValue instanceof String)
//                {
//                    fieldValue = ((String) fieldValue).toUpperCase();
//                }
//                else if (!caseSensitive && fieldValue instanceof Date)
//                {
//                    fieldValue = ((Date) fieldValue).toString();
//                }
//                if (!sortedFieldList.contains(fieldValue)) {
//                    sortedFieldList.add(fieldValue);
//                }
//
//            }
//            sortI18NList(sortedFieldList, user);
//            iter = sortedFieldList.iterator();
//            while (iter.hasNext()) {
//                Object obj = iter.next();
//                Iterator dataModeItem = dataModelList.iterator();
//                while (dataModeItem.hasNext()) {
//                    Object item = dataModeItem.next();
//                    Object fieldValue = method.invoke(item);
//                    if (!caseSensitive && fieldValue instanceof String)
//                    {
//                        fieldValue = ((String) fieldValue).toUpperCase();
//                    }
//                    else if (!caseSensitive && fieldValue instanceof Date)
//                    {
//                        fieldValue = ((Date) fieldValue).toString();
//                    }
//                    if (obj.toString().equals(fieldValue.toString())) {
//                        sortedDataModelList.add(item);
//                    }
//                }
//
//            }
//            dataModelList.clear();
//            dataModelList.addAll(sortedDataModelList);
//
//        } catch (Exception e) {
//            //e.printStackTrace(); // TODO
//            if(log.isErrorEnabled()) {log.error(e);}
//        }
//
//    }

    /**
     * Sort the data model list.
     *
     * @param user the current user in http session.
     * @param dataModelList data model list.
     * @param dataModleCalss data model class
     * @param fieldName field name.
     * @param fieldName2 the 2nd field name.
     * @publish
     */
//    public static void sortDataModelList(User user, List dataModelList, Class dataModleCalss, String fieldName, String fieldName2) {
//        try {
//            List sortedDataModelList = new ArrayList();
//            String getFieldMethodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
//            Method method = dataModleCalss.getMethod(getFieldMethodName);
//            List sortedFieldList = new ArrayList();
//            Iterator iter = dataModelList.iterator();
//            while (iter.hasNext()) {
//                Object item = iter.next();
//                Object filedValue = method.invoke(item);
//                if (!sortedFieldList.contains(filedValue)) {
//                    sortedFieldList.add(filedValue);
//                }
//
//            }
//            sortI18NList(sortedFieldList, user);
//            iter = sortedFieldList.iterator();
//            List sortField2List = new ArrayList();
//            while (iter.hasNext()) {
//                Object obj = iter.next();
//                Iterator dataModeItem = dataModelList.iterator();
//                while (dataModeItem.hasNext()) {
//                    Object item = dataModeItem.next();
//                    Object filedValue = method.invoke(item);
//                    if (obj.toString().equals(filedValue.toString())) {
//                        // sortedDataModelList.add(item);
//                        sortField2List.add(item);
//                    }
//                }
//                if (sortField2List.size() > 1) {
//                    sortDataModelList(user, sortField2List, dataModleCalss, fieldName2);
//                }
//                sortedDataModelList.addAll(sortField2List);
//                sortField2List.clear();
//            }
//            dataModelList.clear();
//            dataModelList.addAll(sortedDataModelList);
//
//        } catch (Exception e) {
//            //e.printStackTrace(); // TODO
//            if(log.isErrorEnabled()){ log.error(e);}
//        }
//
//    }

    /**
     * Put one object to an array list and return the list.
     *
     * @param object the list object
     * @return ArrayList
     */
    public static <T> ArrayList<T> toList(T object) {
        ArrayList<T> list = new ArrayList<T>();
        list.add(object);
        return list;
    }

    /**
     * build the data model list,lose repeat value.
     *
     * @param user the current user in http session.
     * @param dataModelList data mode list.
     * @param dataModleCalss data mode class.
     * @param fieldName field name.
     */
//    public static void buildDataModelList(User user, List dataModelList, Class dataModleCalss, String fieldName) {
//        try {
//            List sortedDataModelList = new ArrayList();
//            String getFieldMethodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
//            Method method = dataModleCalss.getMethod(getFieldMethodName);
//            List sortedFieldList = new ArrayList();
//            Iterator iter = dataModelList.iterator();
//            while (iter.hasNext()) {
//                Object item = iter.next();
//                Object filedValue = method.invoke(item);
//                if (!sortedFieldList.contains(filedValue)) {
//                    sortedFieldList.add(filedValue);
//                    sortedDataModelList.add(item);
//                }
//
//            }
//            dataModelList.clear();
//            dataModelList.addAll(sortedDataModelList);
//
//        } catch (Exception e) {
//            //e.printStackTrace();
//            if(log.isErrorEnabled()){ log.error(e);}
//        }
//
//    }


    /**
     *
     * @param moduleName
     * @param objectType
     * @param logicFunctionExpression
     * @return
     */
    private static String getBusinessFunfunctionExpression(String moduleName,String objectType,String logicFunctionExpression){

        if(logicFunctionExpression == null || logicFunctionExpression.length() == 0){
            return "";
        }

        StringBuffer businessFunctionExpression = new StringBuffer(200);
        StringBuffer functionNameBuffer = new StringBuffer();
        for(int i = 0; i < logicFunctionExpression.length() ; i++){
            char c = logicFunctionExpression.charAt(i);
            boolean isSymbol = c == '|' || c == '&' || c == ' ';
            boolean isLastChar = i == logicFunctionExpression.length() - 1;
            if(isSymbol|| isLastChar){
                if(isLastChar && !isSymbol){
                    functionNameBuffer.append(c);
                }
                if(functionNameBuffer.length() > 0){
                    String logicFunctionName = functionNameBuffer.toString();
                    // BusinessFunctionServiceContext builderContext = new BusinessFunctionServiceContext(moduleName, objectType, logicFunctionName);
                    //tring businessFunctionName = functionBuilder.getBusinessFunction(builderContext);
                    //if(businessFunctionName != null){
                    //  businessFunctionExpression.append(businessFunctionName);
                    //}
//                    else{
//                        businessFunctionExpression.append(logicFunctionName);
//                    }
                    functionNameBuffer.delete(0,functionNameBuffer.length());
                }
                if(isSymbol){
                    businessFunctionExpression.append(c);
                }
            }
            else{
                functionNameBuffer.append(c);
            }
        }

        return businessFunctionExpression.toString();
    }

    public static void main(String[] args){

        final Map<String,String> functionMap = new HashMap<String, String>();

        functionMap.put("PO_POStyle_DOCUMENT_CREATE","POStyle_CReate");
        functionMap.put("PO_POStyle_DOCUMENT_VIEW","POStyle_View");
        functionMap.put("PO_POStyle_DOCUMENT_DELETE","POStyle_Delete");
        functionMap.put("PO_POStyle_DOCUMENT_APPROVE","POStyle_Approve");

//        BusinessFunctionService funBuilder = new BusinessFunctionService(){
//
//            public String getBusinessFunction(BusinessFunctionServiceContext context) {
//                return functionMap.get(context.getModuleName() + "_" + context.getObjectType() + "_" + context.getLogicalFunctionName());
//            }
//        };

//         logger.info(getBusinessFunfunctionExpression("PO","POStyle","DOCUMENT_CREATE || DOCUMENT_VIEW && DOCUMENT_DELETE || DOCUMENT_APPROVE ",funBuilder));
    }

    public static boolean isNotNull(Object object){
        return object != null;
    }

    public static boolean isNull(Object object){
        return object == null;
    }

    public static boolean isNotEmptyString(String str){
        return !isEmptyString(str);
    }

    public static boolean isEmptyString(String str){
        return str == null || str.length() ==0;
    }

    public static String getDateString(Date date){
        if(date == null){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getTimeString(Date date){
        if(date == null){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String convertInt2String(int intValue){
        return String.valueOf(intValue);
    }

    public static int convertString2Int(String str){
        return Integer.parseInt(str);
    }

    public static String convertLong2String(long longValue){
        return String.valueOf(longValue);
    }

    public static long convertString2Long(String str){
        return Long.parseLong(str);
    }

    public static Object getNullValue(){
        return null;
    }

    public static String list2String(List<String> theList, String sepStr) {
        if(theList == null || theList.size() == 0) {
            return "";
        }
        if(StringUtils.nullToString(sepStr).equals("")) {
            sepStr = ",";
        }
        StringBuffer listStr = new StringBuffer();
        boolean haveStarted = false;
        for(String element : theList) {
            if(haveStarted) {
                listStr.append(sepStr).append(element);
            } else {
                listStr.append(element);
                haveStarted = true;
            }
        }

        return listStr.toString();
    }

    public static String map2String(Map<String, String> theMap, String sepStr, String sepStr2) {
        if(theMap == null || theMap.size() == 0) {
            return "";
        }
        if(StringUtils.nullToString(sepStr).equals("")) {
            sepStr = ",";
        }
        if(StringUtils.nullToString(sepStr2).equals("")) {
            sepStr2 = "=";
        }
        StringBuffer mapStr = new StringBuffer();
        boolean haveStarted = false;

        for(Map.Entry<String, String> entry : theMap.entrySet()) {
            String key = entry.getKey();
            if(haveStarted) {
                mapStr.append(sepStr).append(key).append(sepStr2).append(entry.getValue());
            } else {
                mapStr.append(key).append(sepStr2).append(entry.getValue());
                haveStarted = true;
            }
        }

        return mapStr.toString();
    }

    public static List<String> str2List(String theStr, String sepStr) {
        if("".equals(StringUtils.nullToString(theStr))) {
            return null;
        }
        if(StringUtils.nullToString(sepStr).equals("")) {
            sepStr = ",";
        }
        List<String> stringList = new ArrayList<String>();
        for(String element : theStr.split(sepStr)) {
            stringList.add(element);
        }
        return stringList;
    }

    public static Map<String, String> str2Map(String theStr, String sepStr, String sepStr2) {
        if("".equals(StringUtils.nullToString(theStr))) {
            return null;
        }
        if(StringUtils.nullToString(sepStr).equals("")) {
            sepStr = ",";
        }
        if(StringUtils.nullToString(sepStr2).equals("")) {
            sepStr2 = "=";
        }
        Map<String, String> stringMap = new HashMap<String, String>();
        for(String keyValue : theStr.split(sepStr)) {
            String[] keyValuePair = keyValue.split(sepStr2);
            if(keyValuePair.length == 2) {
                stringMap.put(keyValuePair[0], keyValuePair[1]);
            }else if (keyValuePair.length == 1){
                stringMap.put(keyValuePair[0], "");
            }
        }
        return stringMap;
    }
    public static <T> T CONST(T t) { return t; }

    public static boolean isNeedOutputDropdown(HttpServletRequest request) {
        return !request.getHeader("User-Agent").contains("MSIE 6.0;");
    }

    // Modified by Kevin Law for fixing special character issue in dropdown (B2B10-53) Begin
//    public static String getDropdownHtmlForVT(HttpServletRequest request, String name, String[] values, String[] descs, String selectValue, String batchInputOnClick, String batchInputTarget, String batchInputTarget1, String mandatoryCode, String id) {
//        StringBuffer dropdownStr = new StringBuffer("<select name='" + name + "' id='"+ id+ "' class='dropDownBoxEnable' style='z-index:600;width:auto;' "
//                + "onchange='VTResultDependenceDropdownSubmit(this);'" + batchInputOnClick + batchInputTarget + batchInputTarget1 + mandatoryCode + ">");
//
//       // dropdownStr .append( "<option value=''>"+I18NUtil.getMessage(request, "core.common/label.pleaseSelect")+"</option>");
//
//        for (int i = 0; i < values.length; i++) {
//            if ("".equals(values[i])) {
//                continue;
//            }
//            String selected = "";
//            if (values[i].equals(selectValue)) {
//                selected = " selected ";
//            }
//            dropdownStr.append("<option value=\"" + values[i] + "\"" + selected + ">" + descs[i] + "</option>");
////            dropdownStr += "<option value=\"" + values[i] + "\"" + selected + ">" + descs[i] + "</option>";
//        }
//        dropdownStr .append( "</select>");
//        return dropdownStr.toString();
//    }
    //add by JimHuang For fix bug XPC9.2-470 end
}
