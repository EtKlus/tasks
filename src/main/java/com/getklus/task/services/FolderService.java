package com.getklus.task.services;

import com.getklus.task.models.Folder;
import com.getklus.task.models.Users;
import com.getklus.task.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class FolderService {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    DocumentService documentService;

    private Folder findOrThrow(long id) {
        return folderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Keine Liste mit id " + id + " vorhanden."));
    }

    public void addFolder(Folder folder){
       folder.setCreated(LocalDate.now());

       folderRepository.save(folder);
    }

    public List<Folder> getFoldersByUser(Users user){

        List<Folder> all = folderRepository.findAll();
        List<Folder> res = new ArrayList<>();
        for (Folder e: all
        ) {
            if(e.getAuthor() == user.getId()){
                res.add(e);
            }
        }

        return res;
    }
    public List<Folder> getFoldersByParent(Users user,long p){

        List<Folder> all = getFoldersByUser(user);
        List<Folder> res = new ArrayList<>();
        for (Folder e: all
             ) {
            if(e.getParent() == p){
                res.add(e);
            }
        }

        return res;
    }

    public List<Folder> getFoldersByParentWithoutUser(Folder folder){

        List<Folder> all = folderRepository.findAll();
        List<Folder> res = new ArrayList<>();
        for (Folder e: all
        ) {
            if(e.getParent() == folder.getId()){
                res.add(e);
            }
        }

        return res;
    }

    public void updateFolder(Folder folder){

    }

    public void updateParent(Folder folder){

    }


    public Folder getFolderById(long id){
        Folder folder = findOrThrow(id);

        return folder;
    }

    public long getParent(Folder folder){
        return getFolderById(folder.getParent()).getParent();
    }

    public void deleteFolder(Folder folder){
        folderRepository.delete(folder);
    }

    public List<Folder> getFolderPublics(){
        List<Folder> all = folderRepository.findAll();
        List<Folder> publics = new ArrayList<>();
        for (Folder e: all
             ) {
            if(!e.isPersonal()){
                publics.add(e);
            }
        }

        return publics;
    }

    public int getnFolder(Folder folder){
        return getFoldersByParentWithoutUser(folder).size();
    }

    public int getnDoc(Folder folder){
        return documentService.getDocumentsByParentWithoutUser(folder.getId()).size();
    }

}
