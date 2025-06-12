package bo.edu.uagrm.sw1parcial2.controller;

import bo.edu.uagrm.sw1parcial2.dto.ProjectDTO;
import bo.edu.uagrm.sw1parcial2.model.Project;
import bo.edu.uagrm.sw1parcial2.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;

    @PostMapping("/save")
    public ResponseEntity<Void> saveProject(@RequestBody ProjectDTO dto){

        Project project = new Project();

        project.setName(dto.getName());
        project.setCreatedBy(dto.getCreatedBy());
        project.setJson(dto.getJson());

        projectRepository.save(project);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Project project = optionalProject.get();
        ProjectDTO dto = new ProjectDTO();
        dto.setName(project.getName());
        dto.setCreatedBy(project.getCreatedBy());
        dto.setJson(project.getJson());

        return ResponseEntity.ok(dto);
    }
}
