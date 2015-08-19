/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import runkoserver.domain.Area;
import runkoserver.domain.Element;
import runkoserver.domain.Person;

/**
 *
 * @author evly
 */
public interface ElementRepository extends JpaRepository<Element, Long> {

    @Query
    public Element findByName(String name);

    @Query
    public List<Element> findByOwner(Person person);
}
