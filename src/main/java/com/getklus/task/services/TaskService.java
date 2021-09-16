package com.getklus.task.services;

import com.getklus.task.models.Task;
import com.getklus.task.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ClosingService closingService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    private Task findOrThrow(long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "La tache n'a pas été trouvée dans la base"));
    }

    public Task getTask(long id) {
        return findOrThrow(id);
    }

    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    public List<Task> getTaskCreated(long id) {
        List<Task> all = taskRepository.findAll();
        List<Task> allByAuthor = new ArrayList<>();

        for (Task e : all
        ) {
            if (e.getAuthor() == id) {
                allByAuthor.add(e);
            }
        }

        return allByAuthor;
    }

    public void saveTask(Task e) throws Exception {

        taskRepository.save(e);
        emailService.sendEmail(userService.getUserById(e.getResponsable()), "Vous venez de recevoir une nouvelle tâche intitulé  "+e.getIntitule()+". Merci!", "Nouvelle de tâche pour vous");
    }

    public void deleteTask(long id) {
        Task task = findOrThrow(id);

        taskRepository.delete(task);
    }

    // Got
    public List<Task> getTaskGot(long id) {
        List<Task> all = taskRepository.findAll();
        List<Task> allByResponsable = new ArrayList<>();

        for (Task e : all
        ) {
            if (e.getResponsable() == id) {
                allByResponsable.add(e);
            }
        }

        return allByResponsable;
    }

    public boolean getWarning(Task task) {
        return task.getEcheance().isBefore(LocalDate.now()) || task.getEcheance().isEqual(LocalDate.now());
    }

    public List<Task> getAllClosed() {
        List<Task> all = getAllTask();
        List<Task> closed = new ArrayList<>();

        for (Task task : all
        ) {
            if (closingService.getStatut(task)) {
                closed.add(task);
            }
        }

        return closed;
    }

    public List<Task> getAllNotClosed(long id) {
        List<Task> all = getTaskGot(id);
        List<Task> notClosed = new ArrayList<>();

        for (Task task : all
        ) {
            if (!closingService.getStatut(task)) {
                notClosed.add(task);
            }
        }

        return notClosed;
    }

    public int getWarned(long id) {
        int n = 0;
        List<Task> all = getAllNotClosed(id);
        for (Task e : all
        ) {
            if (getWarning(e)) {
                n++;
            }
        }
        return n;
    }

    // Check Not closed task and send mail
    public List<Task> getnclosed() {
        List<Task> all = taskRepository.findAll();
        List<Task> notClosed = new ArrayList<>();

        for (Task task : all
        ) {
            if (!closingService.getStatut(task)) {
                notClosed.add(task);
            }
        }

        return notClosed;
    }

    public void sendMail() throws Exception {
        List<Task> all = getnclosed();
        for (Task e : all
             ) {
            if(e.getEcheance().isEqual(LocalDate.now()) || e.getEcheance().isBefore(LocalDate.now())){
                emailService.sendEmail(userService.getUserById(e.getResponsable()),
                        "La tâche " + e.getIntitule() + " est peut être en retard!",
                        "Urgent : Date d'échéance atteinte ou depassée");

                emailService.sendEmail(userService.getUserById(e.getAuthor()),
                        "La tâche " + e.getIntitule() + " est peut être en retard!",
                        "Urgent : Date d'échéance atteinte ou depassée");
            }
        }
    }
}
