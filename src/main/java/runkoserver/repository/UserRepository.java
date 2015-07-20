/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import runkoserver.domain.User;

/**
 *
 * @author Timo
 */
public interface UserRepository extends JpaRepository<User, Long>{
    
}
