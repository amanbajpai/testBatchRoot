package root.ackla.com.batchroot.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ankurrawal on 26/4/18
 */
@Entity
public class CallLog {
    @Id
    int id;
    String contactNumber;
    String calltype;
    String callStatus;
    String savedName;
    long timestamp;
    String dateTime;
    boolean updateToServer;

    @Generated(hash = 782584040)
    public CallLog(int id, String contactNumber, String calltype, String callStatus,
            String savedName, long timestamp, String dateTime,
            boolean updateToServer) {
        this.id = id;
        this.contactNumber = contactNumber;
        this.calltype = calltype;
        this.callStatus = callStatus;
        this.savedName = savedName;
        this.timestamp = timestamp;
        this.dateTime = dateTime;
        this.updateToServer = updateToServer;
    }

    @Generated(hash = 242590380)
    public CallLog() {
    }


    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isUpdateToServer() {
        return updateToServer;
    }

    public void setUpdateToServer(boolean updateToServer) {
        this.updateToServer = updateToServer;
    }

    public boolean getUpdateToServer() {
        return this.updateToServer;
    }
}
