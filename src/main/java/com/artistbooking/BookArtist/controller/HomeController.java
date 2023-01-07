package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.model.Post;
import com.artistbooking.BookArtist.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class HomeController {

    private final PostService postService;

    @Autowired
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/home")
    public String displayAllPosts(Model model) {

        Collection<Post> posts = this.postService.getAll();
        model.addAttribute("posts", posts);

        return "home";
    }

}
