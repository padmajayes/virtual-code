package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info")
public class InfoController {

    @GetMapping
    public ResponseEntity<InfoResponse> getCustomers() {
        InfoResponse infoResponse = new InfoResponse();
        infoResponse.setInfo("You are able to access Order-service");
        return new ResponseEntity<>(infoResponse, HttpStatus.OK);
    }
}
