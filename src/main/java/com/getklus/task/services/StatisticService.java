package com.getklus.task.services;

import com.getklus.task.models.Task;
import com.getklus.task.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    ClosingService closingService;

    public List<Task> getTaskClosed(Users user){
        List<Task> allGot = taskService.getTaskGot(user.getId());
        List<Task> allClosed = new ArrayList<>();

        for (Task task : allGot
        ) {
            if(closingService.getStatut(task)){
                allClosed.add(task);
            }
        }
        return allClosed;
    }

    public int getCreated(Users user){
     return    taskService.getTaskCreated(user.getId()).size();
    }

    public int getGot(Users user){
        return taskService.getTaskGot(user.getId()).size();
    }

    public int getClosed(Users user){
        return getTaskClosed(user).size();
    }

    public int getBeforeEnding(Users user){
        List<Task> all = getTaskClosed(user);
        int n = 0;

        for (Task task: all
             ) {
            if(task.getEcheance().isAfter(closingService.getClosingByTask(task).getEnding())){
                n++;
            }
        }
        return n;
    }

    public int getAfterEnding(Users user){
        List<Task> all = getTaskClosed(user);
        int n = 0;

        for (Task task: all
        ) {
            if(closingService.getClosingByTask(task).getEnding().isAfter(task.getEcheance())){
                n++;
            }
        }
        return n;
    }

    //Graph
    public List<List<Object>> getChartData(long id) {
        return List.of(
                List.of("Tâches non clôturées", taskService.getAllNotClosed(id).size()),
                List.of("Tâches clôturées", (taskService.getTaskGot(id).size() - taskService.getAllNotClosed(id).size()))

        );
    }
}



