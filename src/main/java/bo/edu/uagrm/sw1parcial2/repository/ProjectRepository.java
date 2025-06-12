package bo.edu.uagrm.sw1parcial2.repository;

import bo.edu.uagrm.sw1parcial2.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository  extends JpaRepository<Project, Long> {
}
