// RoleRepository.java
package rbac.backend.repository;

import rbac.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}