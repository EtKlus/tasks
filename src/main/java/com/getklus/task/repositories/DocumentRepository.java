package com.getklus.task.repositories;

import com.getklus.task.models.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {

    @Override
    List<Document> findAll();
}
