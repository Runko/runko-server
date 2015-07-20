/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Timo
 */
@Entity
public class Person extends AbstractPersistable<Long> {

    @Id
    private Long id;

    @NotNull
    @NotEmpty
    private String name;
    
    @NotNull
    @NotEmpty
    private String kuksaId;

  

    public String getKuksaId() {
        return kuksaId;
    }

    public void setKuksaId(String kuksaId) {
        this.kuksaId = kuksaId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
