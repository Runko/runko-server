/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.domain;


import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 *
 * @author Timo
 */
@Entity
public class Person extends AbstractPersistable<Long> {
   private String name;

   public String getName(){
   return this.name;
   }
   public void setName(String name){
   this.name = name;
   
   }
}
