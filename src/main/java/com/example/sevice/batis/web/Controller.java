package com.example.sevice.batis.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class Controller {

    @GetMapping
    public void trigger(){

    }
}
