package com.getklus.task.repositories;

import com.getklus.task.models.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends CrudRepository<Folder, Long> {

    @Override
    List<Folder> findAll();
}
