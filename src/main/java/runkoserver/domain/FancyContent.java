package runkoserver.domain;

import java.io.Serializable;
import java.util.HashMap;
import javax.persistence.Entity;

@Entity
public class FancyContent extends Content implements Serializable {

    //avaimena integer joka kertoo monesko elementti contentissa? tai sit tää 
    //vois olla vaan lista, jossa täytyy pitää huoli että on oikeassa järjestyksessä
    private HashMap<Integer, Element> elements;
    
    
    
}
