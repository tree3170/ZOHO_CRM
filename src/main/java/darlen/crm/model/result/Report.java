/** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    ProjectName ZOHO_CRM
 *    File Name   Report.java 
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 *    Copyright (c) 2016 Darlen . All Rights Reserved. 
 *    注意： 本内容仅限于XXX公司内部使用，禁止转发
 * ** ** ** ** ** ** ** **** ** ** ** ** ** ** **** ** ** ** ** ** ** **
 * */
package darlen.crm.model.result;

/**
 * darlen.crm.model.result
 * Description：ZOHO_CRM
 * Created on  2016/10/15 16：31
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        16：31   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public class Report {
    private String REPORTID;
    private String START_TIME;
    private String END_TIME;
    private String INS_FAILED;
    private String UPD_FAILED;
    private String DEL_FAILED;
    private String WHOLEFAIL;

    public String getWHOLEFAIL() {
        return WHOLEFAIL;
    }

    public void setWHOLEFAIL(String WHOLEFAIL) {
        this.WHOLEFAIL = WHOLEFAIL;
    }

    public String getREPORTID() {
        return REPORTID;
    }

    public void setREPORTID(String REPORTID) {
        this.REPORTID = REPORTID;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getINS_FAILED() {
        return INS_FAILED;
    }

    public void setINS_FAILED(String INS_FAILED) {
        this.INS_FAILED = INS_FAILED;
    }

    public String getUPD_FAILED() {
        return UPD_FAILED;
    }

    public void setUPD_FAILED(String UPD_FAILED) {
        this.UPD_FAILED = UPD_FAILED;
    }

    public String getDEL_FAILED() {
        return DEL_FAILED;
    }

    public void setDEL_FAILED(String DEL_FAILED) {
        this.DEL_FAILED = DEL_FAILED;
    }

    @Override
    public String toString() {
        return "Report{" +
                "REPORTID='" + REPORTID + '\'' +
                ", START_TIME='" + START_TIME + '\'' +
                ", END_TIME='" + END_TIME + '\'' +
                ", INS_FAILED='" + INS_FAILED + '\'' +
                ", UPD_FAILED='" + UPD_FAILED + '\'' +
                ", DEL_FAILED='" + DEL_FAILED + '\'' +
                ", WHOLEFAIL='" + WHOLEFAIL + '\'' +
                '}';
    }
}
