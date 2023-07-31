/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import tallerM2.tallerM2.model.ERole;
import tallerM2.tallerM2.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}

