// PermissionRepository.java
package rbac.backend.repository;

import rbac.backend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}