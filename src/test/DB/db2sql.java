/**
 * Copyright Isocra Ltd 2004
 * You can use, modify and freely distribute this file as long as you credit Isocra Ltd.
 * There is no explicit or implied guarantee of functionality associated with this file, use it at your own risk.
 *
 * http://www.tuicool.com/articles/2eQNZf
 */

package DB;

import darlen.crm.util.CommonUtils;
import darlen.crm.util.DBUtils;

import java.io.*;
import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.Properties;

/**
 * This class connects to a database and dumps all the tables and contents out to stdout in the form of
 * a set of SQL executable statements
 * 此类连接到数据库，并以一组SQL可执行语句的形式将所有表和内容转储到文件中
 */
public class db2sql {
    private static  String DB_USERNAME="sa";
    private static  String DB_PWD="zaq1@WSX";
    private static  String DB_URL="jdbc:sqlserver://localhost:1433; DatabaseName=Biz_Matrix00";
    private static  String DB_DRIVER_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver";

    /** Dump the whole database to an SQL string */
    public static String dumpDB(Properties props) {
        String driverClassName = DB_DRIVER_NAME;
        String driverURL = DB_URL;
        // Default to not having a quote character
        String columnNameQuote = "@@@@";//props.getProperty("columnName.quoteChar", "");
        DatabaseMetaData dbMetaData = null;
        Connection dbConn = null;
        try {
            Class.forName(driverClassName);
            dbConn = DBUtils.getConnection();//DriverManager.getConnection(driverURL, props);
            dbMetaData = dbConn.getMetaData();
        }
        catch( Exception e ) {
            System.err.println("Unable to connect to database: "+e);
            return null;
        }

        try {
            StringBuffer result = new StringBuffer();
            String catalog = "Biz_Matrix00";//props.getProperty("catalog");//数据库名
            String schema = "dbo";//props.getProperty("schemaPattern");
            String tables = props.getProperty("tableName");
            /**
             * getTables()方法，该方法需要传进4个参数：
             第一个是数据库名称，对于MySQL，则对应相应的数据库，对于Oracle来说，则是对应相应的数据库实例，可以不填，也可以直接使用Connection的实例对象中的getCatalog()方法返回的值填充；
             第二个是模式，可以理解为数据库的登录名，而对于Oracle也可以理解成对该数据库操作的所有者的登录名。对于Oracle要特别注意，其登陆名必须是大写，不然的话是无法获取到相应的数据，而MySQL则不做强制要求。
             第三个是表名称，一般情况下如果要获取所有的表的话，可以直接设置为null，如果设置为特定的表名称，则返回该表的具体信息。
             第四个是类型标准,以数组形式传值，有"TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"这几个经典的类型，一般使用”TABLE”，即获取所有类型为TABLE的表
             它返回一个ResultSet对象，有10列，详细的显示了表的类型：
             */
            ResultSet rs = dbMetaData.getTables(catalog, schema, "Customer", null);
            if (! rs.next()) {
                System.err.println("Unable to find any tables matching: catalog="+catalog+" schema="+schema+" tables="+tables);
                rs.close();
            } else {
                /**
                *  Right, we have some tables, so we can go to work.
                * the details we have are
                * TABLE_CAT String => table catalog (may be null)表类别
                * TABLE_SCHEM String => table schema (may be null)表模式
                * TABLE_NAME String => table name表名称
                * TABLE_TYPE String => 表类型table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
                * REMARKS String => 表的解释性注释explanatory comment on the table
                * TYPE_CAT String => 类型的类别the types catalog (may be null)
                * TYPE_SCHEM String => 类型模式the types schema (may be null)
                * TYPE_NAME String => 类型名称type name (may be null)
                * SELF_REFERENCING_COL_NAME String => 有类型表的指定name of the designated "identifier" column of a typed table (may be null)
                * REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
                * We will ignore the schema and stuff, because people might want to import it somewhere else
                * We will also ignore any tables that aren't of type TABLE for now.
                * We use a do-while because we've already caled rs.next to see if there are any rows
                */
                do {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");
                    if ("TABLE".equalsIgnoreCase(tableType)) {
                        result.append("\n\n-- "+tableName);
                        result.append("\nCREATE TABLE "+tableName+" (\n");
                        ResultSet tableMetaData = dbMetaData.getColumns(null, null, tableName, "%");
                        boolean firstLine = true;
                        while (tableMetaData.next()) {
                            if (firstLine) {
                                firstLine = false;
                            } else {
                                // If we're not the first line, then finish the previous line with a comma
                                result.append(",\n");
                            }
                            String columnName = tableMetaData.getString("COLUMN_NAME");
                            String columnType = tableMetaData.getString("TYPE_NAME");
                            // WARNING: this may give daft answers for some types on some databases (eg JDBC-ODBC link)
                            int columnSize = tableMetaData.getInt("COLUMN_SIZE");
                            String nullable = tableMetaData.getString("IS_NULLABLE");
                            String nullString = "NULL";
                            if ("NO".equalsIgnoreCase(nullable)) {
                                nullString = "NOT NULL";
                            }
                            result.append("    "+columnNameQuote+columnName+columnNameQuote+" "+columnType+" ("+columnSize+")"+" "+nullString);
                        }
                        tableMetaData.close();

                        // Now we need to put the primary key constraint
                        try {
                            ResultSet primaryKeys = dbMetaData.getPrimaryKeys(catalog, schema, tableName);
                            // What we might get:
                            // TABLE_CAT String => table catalog (may be null)
                            // TABLE_SCHEM String => table schema (may be null)
                            // TABLE_NAME String => table name
                            // COLUMN_NAME String => column name
                            // KEY_SEQ short => sequence number within primary key
                            // PK_NAME String => primary key name (may be null)
                            String primaryKeyName = null;
                            StringBuffer primaryKeyColumns = new StringBuffer();
                            while (primaryKeys.next()) {
                                String thisKeyName = primaryKeys.getString("PK_NAME");
                                if ((thisKeyName != null && primaryKeyName == null)
                                        || (thisKeyName == null && primaryKeyName != null)
                                        || (thisKeyName != null && ! thisKeyName.equals(primaryKeyName))
                                        || (primaryKeyName != null && ! primaryKeyName.equals(thisKeyName))) {
                                    // the keynames aren't the same, so output all that we have so far (if anything)
                                    // and start a new primary key entry
                                    if (primaryKeyColumns.length() > 0) {
                                        // There's something to output
                                        result.append(",\n    PRIMARY KEY ");
                                        if (primaryKeyName != null) { result.append(primaryKeyName); }
                                        result.append("("+primaryKeyColumns.toString()+")");
                                    }
                                    // Start again with the new name
                                    primaryKeyColumns = new StringBuffer();
                                    primaryKeyName = thisKeyName;
                                }
                                // Now append the column
                                if (primaryKeyColumns.length() > 0) {
                                    primaryKeyColumns.append(", ");
                                }
                                primaryKeyColumns.append(primaryKeys.getString("COLUMN_NAME"));
                            }
                            if (primaryKeyColumns.length() > 0) {
                                // There's something to output
                                result.append(",\n    PRIMARY KEY ");
                                if (primaryKeyName != null) { result.append(primaryKeyName); }
                                result.append(" ("+primaryKeyColumns.toString()+")");
                            }
                        } catch (SQLException e) {
                            // NB you will get this exception with the JDBC-ODBC link because it says
                            // [Microsoft][ODBC Driver Manager] Driver does not support this function
                            System.err.println("Unable to get primary keys for table "+tableName+" because "+e);
                        }

                        result.append("\n);\n");

                        // Right, we have a table, so we can go and dump it
                        //dumpTable(dbConn, result, tableName);
                    }
                } while (rs.next());
                rs.close();
            }
            dbConn.close();
            return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }

    /** dump this particular table to the string buffer */
    private static void dumpTable(Connection dbConn, StringBuffer result, String tableName) {
        try {
            // First we output the create table stuff
            PreparedStatement stmt = dbConn.prepareStatement("SELECT * FROM "+tableName);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Now we can output the actual data
            result.append("\n\n-- Data for "+tableName+"\n");
            while (rs.next()) {
                result.append("INSERT INTO "+tableName+" VALUES (");
                for (int i=0; i<columnCount; i++) {
                    if (i > 0) {
                        result.append(", ");
                    }
                    Object value = rs.getObject(i+1);
                    if (value == null) {
                        result.append("NULL");
                    } else {
                        String outputValue = value.toString();
                        outputValue = outputValue.replaceAll("'","\\'");
                        result.append("'"+outputValue+"'");
                    }
                }
                result.append(");\n");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Unable to dump table "+tableName+" because: "+e);
        }
    }

    /** Main method takes arguments for connection to JDBC etc. */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("usage: db2sql <property file>");
        }
        // Right so there's one argument, we assume it's a property file
        // so lets open it
        Properties props = new Properties();
        try {
            //InputStream in = db2sql.class.getResourceAsStream("db.properties");
            //String absoluteFilePath = CommonUtils.getFileNamePath("", "db.properties");
            //System.out.println("1");
            //props.load();
            //props.load(new FileInputStream(args[0]));
            System.err.println("============"+dumpDB(props));
            //String s = dumpDB(props);
            //FileOutputStream fos = new FileOutputStream(new File("D:\\table.sql"));
            //fos.write(s.getBytes());
            //fos.flush();
            //fos.close();
        } catch (Exception e) {
            System.err.println("Unable to open property file: "+args[0]+" exception: "+e);
        }

    }
}