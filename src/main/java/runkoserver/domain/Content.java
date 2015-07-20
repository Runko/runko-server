/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author warva
 */

@Entity
public abstract class Content extends AbstractPersistable<Long> {
    
    @Id
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
            
    @NotNull
    @NotEmpty
    private String name;
    
    @ManyToOne
    private Person owner;
    
    @ManyToMany (mappedBy = "subscriptions")
    private List<Person> subscribers;
      
    public String getName() {
        return this.name;
    }
    

    public Long getId() {
        return id;
    }
    
}
