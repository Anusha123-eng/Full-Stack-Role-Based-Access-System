// ResourceRepository.java
package rbac.backend.repository;

import rbac.backend.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}