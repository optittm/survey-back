package com.survey.microservice.domain.ports.api;

import com.survey.microservice.domain.data.ProjectDTO;
import java.util.List;

public interface IProjectService {
    List<ProjectDTO> getProjects();
}
