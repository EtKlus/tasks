package com.getklus.task.repositories;

import com.getklus.task.models.Closing;
import com.getklus.task.models.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Repository
public interface ClosingRepository extends CrudRepository<Closing, Long> {

    @Override
    List<Closing> findAll();

}
