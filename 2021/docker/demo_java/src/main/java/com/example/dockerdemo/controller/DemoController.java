package com.example.dockerdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ethan Lau
 * @date 2021/11/13 22:28
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @RequestMapping("normal")
    public String normal(){

        return "Success";
    }
}
