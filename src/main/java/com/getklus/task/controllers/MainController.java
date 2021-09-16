package com.getklus.task.controllers;

import com.getklus.task.models.*;
import com.getklus.task.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    ClosingService closingService;

    @Autowired
    StatisticService statisticService;

    @Autowired
    FolderService folderService;

    @Autowired
    DocumentService documentService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String getHome(Principal principal, Model model) {

        long all = taskService.getAllTask().size();
        long created = taskService.getTaskCreated(userService.getUser(principal.getName()).getId()).size();
        long got = taskService.getTaskGot(userService.getUser(principal.getName()).getId()).size();

        model.addAttribute("got", got);
        model.addAttribute("created", created);
        model.addAttribute("all", all/ 100);
        model.addAttribute("createdSize", created);
        model.addAttribute("gotSize", got);
        model.addAttribute("allSize", all);
        model.addAttribute("taskService", taskService);

        Users user = userService.getUser(principal.getName());

        model.addAttribute("tasks", taskService.getAllNotClosed(user.getId()));
        model.addAttribute("userService", userService);
        model.addAttribute("closing", new Closing());
        model.addAttribute("closingService", closingService);
        model.addAttribute("taskService", taskService);

        model.addAttribute("chartData", statisticService.getChartData(user.getId()));
        model.addAttribute("warned", taskService.getWarned(user.getId()));
        model.addAttribute("userSize", userService.getUsers().size());
        model.addAttribute("allClosed", (taskService.getTaskGot(user.getId()).size() - taskService.getAllNotClosed(user.getId()).size()));
        model.addAttribute("allNotClosed",taskService.getAllNotClosed(user.getId()).size());
        return "home";
    }

    // Account

    @GetMapping("/account")
    public String getAccount(Principal principal, Model model){

        model.addAttribute("user", userService.getUser(principal.getName()));
        return "account";
    }

    @PostMapping("/update/user/data")
    public String updateData(Users user, RedirectAttributes redirectAttributes){
        userService.updateUserData(user);
        redirectAttributes.addFlashAttribute("message", "Modification effectuée avec succès! Vous devez vous reconnecter.");

        return "redirect:/login";
    }

    @PostMapping("/update/user/password")
    public String updatePassword(Users user, RedirectAttributes redirectAttributes){
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "Modification effectuée avec succès! Vous devez vous reconnecter.");

        return "redirect:/login";
    }

    @GetMapping("/parameter")
    public String getParameter(Model model){

        model.addAttribute("users", userService.getUsers());
        model.addAttribute("newUser", new Users());
        return "parameter";
    }

    @PostMapping("/add/user")
    public String addUser(Users user, RedirectAttributes redirectAttributes){

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "Ajout effectué avec succès!");
        return "redirect:/parameter";
    }

    @GetMapping("/enable/user/{id}")
    public String enableUser(@PathVariable("id") long id, RedirectAttributes redirectAttributes){
        userService.enableUser(id);
        redirectAttributes.addFlashAttribute("message", "Modification effectuée avec succès!");
        return "redirect:/parameter";
    }

    @PostMapping("/remove/user/{id}")
    public String removeUser(@PathVariable("id") long id, RedirectAttributes redirectAttributes){
        userService.removeUser(id);

        redirectAttributes.addFlashAttribute("message", "Suppression effectuée avec succès!");
        return "redirect:/parameter";
    }

    // Task

    @GetMapping("/task_created")
    public String getTask(Principal principal, Model model) {

        Users user = userService.getUser(principal.getName());

        model.addAttribute("users", userService.getUsers());
        model.addAttribute("task", new Task());
        model.addAttribute("tasks", taskService.getTaskCreated(user.getId()));
        model.addAttribute("userService", userService);
        model.addAttribute("closingService", closingService);
        model.addAttribute("taskService", taskService);

        return "task_created";
    }

    @PostMapping("/add/task")
    public String saveTask(Principal principal, Task e, RedirectAttributes redirectAttributes) throws Exception {

        Users user = userService.getUser(principal.getName());
        e.setAuthor(user.getId());

        String warning = "";

        if(e.getDebut().isBefore(LocalDate.now())){
            warning = "Incorrect! La date de debut se trouve dans le passe.";
            redirectAttributes.addFlashAttribute("warning", warning);
        }
        else if(e.getEcheance().isBefore(e.getDebut())){
            warning = "Incorrect! La date d'echeance se trouve avant la date de debut.";
            redirectAttributes.addFlashAttribute("warning", warning);
        }
        else {

            taskService.saveTask(e);
            redirectAttributes.addFlashAttribute("message", "Ajout effectué avec succès!");
        }

        return "redirect:/task_created";
    }

    @GetMapping("/update/task/{id}")
    public String updateTask(Principal principal, @PathVariable("id") long id, Model model) {

        Users user = userService.getUser(principal.getName());

        model.addAttribute("users", userService.getUsers());
        model.addAttribute("tasks", taskService.getTaskCreated(user.getId()));
        model.addAttribute("userService", userService);
        model.addAttribute("task", taskService.getTask(id));
        model.addAttribute("closingService", closingService);
        model.addAttribute("taskService", taskService);

        return "task_created";
    }

    @PostMapping("/remove/task/{id}")
    public String removeTask(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {

        taskService.deleteTask(id);

        redirectAttributes.addFlashAttribute("message", "Suppression effectuée avec succès!");
        return "redirect:/task_created";
    }

    // Task_Got
    @GetMapping("/task_got")
    public String getTaskGot(Principal principal, Model model) {

        Users user = userService.getUser(principal.getName());

        model.addAttribute("tasks", taskService.getTaskGot(user.getId()));
        model.addAttribute("userService", userService);
        model.addAttribute("closing", new Closing());
        model.addAttribute("closingService", closingService);
        model.addAttribute("taskService", taskService);

        return "task_got";
    }

    @GetMapping("/close/task/{id}")
    public String closeTask(Principal principal, @PathVariable("id") long id, Model model){

        Closing closing = new Closing();
        closing.setTask(id);
        Users user = userService.getUser(principal.getName());
        Task selected = taskService.getTask(id);
        model.addAttribute("tasks", taskService.getTaskGot(user.getId()));
        model.addAttribute("userService", userService);
        model.addAttribute("closing", closing);
        model.addAttribute("closingService", closingService);
        model.addAttribute("taskService", taskService);
        model.addAttribute("selected", selected );

        return "task_got";
    }

    @PostMapping("/save/closing")
    public String saveClosing(Closing closing, RedirectAttributes redirectAttributes) throws Exception {

        String msg = "";
        String warning = "";
        if(closing.getTask() == 0){

            warning = "Veuillez selectionner une tache, svp.";
            redirectAttributes.addFlashAttribute("warning", warning);

        }
        else if (taskService.getTask(closing.getTask()).getDebut().isAfter(closing.getEnding())){
            warning = "La date de debut se trouve avant la date de demarrage de la tache, Incorrect!";
            redirectAttributes.addFlashAttribute("warning", warning);
        }
        else{
            closing.setIsclosed(true);
            closingService.saveClosing(closing);

            msg = "Ajout effectué avec succès!";

            redirectAttributes.addFlashAttribute("message", msg);
        }

        return "redirect:/task_got";
    }

    // Show Task
    @GetMapping("/show/task/{id}")
    public String showTask(@PathVariable("id") long id, Model model){

        Task task = taskService.getTask(id);

        model.addAttribute("task", task);
        model.addAttribute("closingService", closingService);
        model.addAttribute("userService", userService);
        return "task_show";
    }

    // Statistic

    @GetMapping("/statistic")
    public String getStatistic(Model model){

        model.addAttribute("users", userService.getUsers());
        model.addAttribute("service", statisticService);

        return "task_statistic";
    }

    // Help
    @GetMapping("/help")
    public String getHelp(){
        return "help";
    }

    // Documents

    @GetMapping("/document/{p}")
    public String getDocument(@PathVariable("p") long p,Principal principal, Model model){

        Folder folder = new Folder();
        folder.setParent(p);
        Document document = new Document();
        document.setParent(p);

        // Return to parent
        long older = 0;

        if(p > 0){
            older= folderService.getFolderById(p).getParent();
        }
        // End

        model.addAttribute("folder", folder);
        model.addAttribute("document", document);
        model.addAttribute("folders", folderService.getFoldersByParent(userService.getUser(principal.getName()), p));
        model.addAttribute("documents", documentService.getDocumentsByParent(userService.getUser(principal.getName()), p));
        model.addAttribute("folderService", folderService);
        model.addAttribute("older",older );

        return "document";
    }

    @PostMapping("/add/folder")
    public String addFolder(Principal principal, Folder folder, RedirectAttributes redirectAttributes){

        if(folder.getId() != null){
            documentService.makeAllDocumentOfFolderPublic(folder.getId(), folder.isPersonal());
        }
        folder.setAuthor(userService.getUser(principal.getName()).getId());
        folderService.addFolder(folder);

        redirectAttributes.addFlashAttribute("message", "Ajout effectué avec succès!");
        return "redirect:/document/" + folder.getParent();
    }

    @PostMapping("/add/document/{p}")
    public String addDocument(Principal principal,@PathVariable("p") long p, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();
        Document document = new Document();
        document.setParent(p);
        document.setName(filename);
        document.setUri(uri);
        document.setType(file.getContentType());
        document.setSize(file.getSize());
        document.setFile(file.getBytes());
        document.setAuthor(userService.getUser(principal.getName()).getId());
        document.setPersonal(true);
        documentService.saveDocument(document);


        redirectAttributes.addFlashAttribute("message", "Fichier " +file.getOriginalFilename() + " ajouté avec success!");

        return "redirect:/document/" + document.getParent();
    }

    @GetMapping("/download/{id}")
    public void getFile(@PathVariable("id") long id, HttpServletResponse resp) throws IOException {
        Document document = documentService.getDocumentById(id);
        resp.setContentType(document.getType());
        resp.setHeader("Content-Disposition", "attachment;filename=" + document.getName());
        resp.getOutputStream().write(document.getFile());
    }

    @PostMapping("/update/star/{fid}")
    public String updateStar(@PathVariable("fid") long fid) throws IOException {
        Document document = documentService.getDocumentById(fid);
        document.setStar(!document.isStar());
        documentService.saveDocument(document);
        return "redirect:/document/" + document.getParent();
    }

    @PostMapping("/update/personal/{fid}")
    public String updatePersonal(@PathVariable("fid") long fid) throws IOException {
        Document document = documentService.getDocumentById(fid);
        document.setPersonal(!document.isPersonal());
        documentService.saveDocument(document);
        return "redirect:/document/" + document.getParent();
    }

    @PostMapping("/remove/file/{fid}")
    public String removeFile(@PathVariable("fid") long fid, RedirectAttributes redirectAttributes){
        Document document = documentService.getDocumentById(fid);
        long parent = document.getParent();
        documentService.deleteDocument(document);
        redirectAttributes.addFlashAttribute("message", "Suppression effectuée avec succès!");
        return "redirect:/document/" + parent;
    }

    @GetMapping("/update/folder/{fid}")
    public String updateDocument(@PathVariable("fid") long fid,Principal principal, Model model){

        Folder folder = folderService.getFolderById(fid);
        long p = folder.getParent();
        Document document = new Document();
        document.setParent(p);

        // Return to parent
        long older = 0;

        if(p > 0){
            older= folderService.getFolderById(p).getParent();
        }
        // End

        model.addAttribute("folder", folder);
        model.addAttribute("document", document);
        model.addAttribute("folders", folderService.getFoldersByParent(userService.getUser(principal.getName()), p));
        model.addAttribute("documents", documentService.getDocumentsByParent(userService.getUser(principal.getName()), p));
        model.addAttribute("folderService", folderService);
        model.addAttribute("older",older );
        model.addAttribute("modif", true);

        return "document";
    }

    @PostMapping("/remove/folder/{fid}")
    public String removeFolder(@PathVariable("fid") long fid, RedirectAttributes redirectAttributes){
        Folder folder = folderService.getFolderById(fid);
        long parent = folder.getParent();
        folderService.deleteFolder(folder);
        redirectAttributes.addFlashAttribute("message", "Suppression effectuée avec succès!");
        return "redirect:/document/" + parent;
    }

    // Publics

    @GetMapping("/publics")
    public String getPublics(Model model){

        model.addAttribute("documents", documentService.getDocumentPublics());
        model.addAttribute("userService", userService);

        return "publics";
    }

    //star
    @GetMapping("/star")
    public String getStar(Principal principal, Model model){

        model.addAttribute("documents", documentService.getDocumentStar(userService.getUser(principal.getName())));
        model.addAttribute("userService", userService);

        return "star";
    }
}
