package darlen.crm.model.result;

/**
 * darlen.crm.model.result
 * Description：ZOHO_CRM
 * Created on  2016/10/06 01：37
 * -------------------------------------------------------------------------
 * 版本     修改时间        作者         修改内容 
 * 1.0.0        01：37   Darlen              create
 * -------------------------------------------------------------------------
 *
 * @author Darlen liu
 */
public abstract  class IModule {
    public String erpID;
    /**客户拥有者*/
    public User user;
    public String creationTime;
    public String latestEditBy;
    public String latestEditTime;

    public String getErpID() {
        return erpID;
    }

    public void setErpID(String erpID) {
        this.erpID = erpID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLatestEditBy() {
        return latestEditBy;
    }

    public void setLatestEditBy(String latestEditBy) {
        this.latestEditBy = latestEditBy;
    }

    public String getLatestEditTime() {
        return latestEditTime;
    }

    public void setLatestEditTime(String latestEditTime) {
        this.latestEditTime = latestEditTime;
    }

    @Override
    public String toString() {
        return "IModule{" +
                "erpID='" + erpID + '\'' +
                ", user=" + user +
                ", creationTime='" + creationTime + '\'' +
                ", latestEditBy='" + latestEditBy + '\'' +
                ", latestEditTime='" + latestEditTime + '\'' +
                '}';
    }
}
