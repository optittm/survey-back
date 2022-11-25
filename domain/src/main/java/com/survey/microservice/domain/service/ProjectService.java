package com.survey.microservice.domain.service;

import java.util.List;

import com.survey.microservice.domain.data.ProjectDTO;
import com.survey.microservice.domain.ports.api.IProjectService;
import com.survey.microservice.domain.ports.spi.IOttmRepository;

public class ProjectService implements IProjectService {

    IOttmRepository ottmApiRepository;

    public ProjectService(IOttmRepository repository){
        this.ottmApiRepository = repository;
    }

    public List<ProjectDTO> getProjects(){
        return ottmApiRepository.getProjects();
    }
}
