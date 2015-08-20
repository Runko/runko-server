package runkoserver.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToMany;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.persistence.FetchType;
import javax.persistence.Lob;


/**
 * Upper class for all content types.
 */
@Entity
public class Content extends Element implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Person> bookmarkers;

    @Lob
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
    
     public List<Person> getBookmarkers() {
        if (bookmarkers != null) {
            return bookmarkers;
        }
        return new ArrayList<>();
    }
     
    public void setBookmarkers(List<Person> bookmarkers) {
        this.bookmarkers = bookmarkers;
    }
}
