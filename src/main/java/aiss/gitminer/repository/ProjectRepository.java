package aiss.gitminer.repository;

import aiss.gitminer.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    Page<Project> findById (String id, Pageable pageable);
    Page<Project> findByName(String name, Pageable paging);

}