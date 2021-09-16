package com.getklus.task.services;

import com.getklus.task.models.Document;
import com.getklus.task.models.Users;
import com.getklus.task.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    private Document findOrThrow(long id) {
        return documentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Keine Liste mit id " + id + " vorhanden."));
    }

    public Document getDocumentById(long id){
        Document document = findOrThrow(id);

        return document;
    }

    public void saveDocument(Document document) throws IOException {
        document.setCreated(LocalDate.now());
        documentRepository.save(document);
    }

    public List<Document> getDocumentByUser(Users user){
        List<Document> all = documentRepository.findAll();
        List<Document> res = new ArrayList<>();
        for (Document e: all
        ) {
            if(e.getAuthor() == user.getId()){
                res.add(e);
            }
        }

        return res;
    }

    public List<Document> getDocumentsByParent(Users user, long p){
        List<Document> all = getDocumentByUser(user);
        List<Document> res = new ArrayList<>();
        for (Document e: all
             ) {
            if(e.getParent() == p){
                res.add(e);
            }
        }

        return res;
    }

    public List<Document> getDocumentsByParentWithoutUser(long p){
        List<Document> all = documentRepository.findAll();
        List<Document> res = new ArrayList<>();
        for (Document e: all
        ) {
            if(e.getParent() == p){
                res.add(e);
            }
        }

        return res;
    }

   public void deleteDocument(Document document){
        documentRepository.delete(document);
   }

   public List<Document> getDocumentStar(Users user){
        List<Document> all = getDocumentByUser(user);
        List<Document> stars = new ArrayList<>();
       for (Document e: all
            ) {
           if(e.isStar()){
               stars.add(e);
           }
       }

       return stars;
   }

    public List<Document> getDocumentPublics(){
        List<Document> all = documentRepository.findAll();
        List<Document> publics = new ArrayList<>();
        for (Document e: all
        ) {
            if(!e.isPersonal()){
                publics.add(e);
            }
        }

        return publics;
    }

    public void makeAllDocumentOfFolderPublic(long p, boolean b){
        List<Document> all = getDocumentsByParentWithoutUser(p);
        for (Document e: all
             ) {
            e.setPersonal(b);
            documentRepository.save(e);
        }
    }


}
