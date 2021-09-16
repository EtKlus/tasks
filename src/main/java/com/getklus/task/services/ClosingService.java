package com.getklus.task.services;

import com.getklus.task.models.Closing;
import com.getklus.task.models.Task;
import com.getklus.task.repositories.ClosingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ClosingService {

    @Autowired
    ClosingRepository closingRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Closing findOrThrow(long id) {
        return closingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "L'entite n'a pas été trouvée dans la base"));
    }

    public Closing getClosing(long id) {
        return findOrThrow(id);
    }

    public void saveClosing(Closing e) throws Exception {

        closingRepository.save(e);
        emailService.sendEmail(userService.getUserById(taskService.getTask(e.getTask()).getAuthor()),
                userService.getUserById(taskService.getTask(e.getTask()).getAuthor()).getUsername() +
                        " a clôturé la tâche "+ taskService.getTask(e.getTask()).getIntitule() +" à " + formatter.format(LocalDateTime.now()) +" . Merci!", "Clôture de tâche");
    }

    public void deleteClosing(long id) {
        Closing closing = findOrThrow(id);

        closingRepository.delete(closing);
    }

    public boolean getStatut(Task task){
        List<Closing> all = closingRepository.findAll();
        boolean statut = false;

        for (Closing e: all
             ) {
            if(e.getTask() == task.getId()){
                statut = e.isIsclosed();
            }
        }
        return statut;
    }

    public Closing getClosingByTask(Task task){

        List<Closing> all = closingRepository.findAll();

        for (Closing e : all
             ) {
            if(e.getTask() == task.getId()){
                return e;
            }
        }

        return  null;
    }

}
