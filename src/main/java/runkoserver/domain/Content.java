package runkoserver.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Upper class for all content types.
 */
@Entity
public class Content extends Element implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    private String textArea;

    public Content() {
    }

    public String getCreationTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(creationTime);
    }

    public void setCreationTime() {
        this.creationTime = new Date();
        this.modifyTime = new Date();
    }

    public String getModifyTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(modifyTime);
    }

    public void setModifyTime() {
        this.modifyTime = new Date();
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }

    public String getTextArea() {
        return this.textArea;
    }
}
