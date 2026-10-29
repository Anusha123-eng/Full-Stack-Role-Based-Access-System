
package rbac.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resource;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "resourceId")
    private Resource resourceDetail;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    // Default constructor required by JPA
    public Permission() {
    }

    // Parameterized constructor used by DataSeeder
    public Permission(
            String name,
            String action,
            String resource,
            String description,
            Resource resourceDetail) {

        this.name = name;
        this.action = action;
        this.resource = resource;
        this.description = description;
        this.resourceDetail = resourceDetail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Resource getResourceDetail() {
        return resourceDetail;
    }

    public void setResourceDetail(Resource resourceDetail) {
        this.resourceDetail = resourceDetail;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
