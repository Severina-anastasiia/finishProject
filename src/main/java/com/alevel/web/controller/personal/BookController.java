package com.alevel.web.controller.personal;

import com.alevel.facade.BookFacade;
import com.alevel.web.data.request.BookData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personal/book")
public class BookController {

    private final BookFacade bookFacade;

    public BookController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @GetMapping("/all")
    public String findAllPost(WebRequest request, Model model) {
        model.addAttribute("pageData", bookFacade.findAll(request));
        return "page/personal/post/post_all";
    }

    @GetMapping("/my")
    public String findMyPost(WebRequest request, Model model) {
        model.addAttribute("pageData", bookFacade.findAll(request));
        return "page/personal/post/post_my";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable Integer id, @RequestParam("reaction") boolean reaction) {
        model.addAttribute("postFullData", bookFacade.findById(id));
        model.addAttribute("reaction", reaction);
        return "page/personal/post/post_details";
    }

    @GetMapping("/new")
    public String redirectToNewPage(Model model) {
        model.addAttribute("postForm", new BookData());
        return "page/personal/post/post_new";
    }

    @PostMapping("/new")
    public String newPost(@ModelAttribute("postForm") BookData data) {
        bookFacade.create(data);
        return "redirect:/personal/post/my";
    }

    @GetMapping("/update/{id}")
    public String redirectToUpdatePage(@PathVariable Integer id, Model model) {
        model.addAttribute("postForm", bookFacade.findById(id));
        return "page/personal/post/post_update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("postForm") BookData data, @RequestParam Integer id) {
        bookFacade.update(data, id);
        return "redirect:/personal/post/my";
    }

    @GetMapping("/like/{id}")
    public String likePost(@PathVariable Integer id) {
        bookFacade.like(id);
        return "redirect:/personal/post/details/" + id + "?reaction=true";
    }

    @GetMapping("/dislike/{id}")
    public String dislikePost(@PathVariable Integer id) {
        bookFacade.dislike(id);
        return "redirect:/personal/post/details/" + id + "?reaction=true";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        bookFacade.delete(id);
        return "redirect:/personal/post/my";
    }

    @PostMapping("/upload/{id}")
    public String uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Integer id, RedirectAttributes attributes) {
        System.out.println("id = " + id);
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/personal/post/details/" + id + "?reaction=false";
        }
        bookFacade.uploadFile(file, id);
        return "redirect:/personal/post/details/" + id + "?reaction=false";
    }
}
