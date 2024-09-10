package com.BaseProject.utils.controller;

import com.BaseProject.utils.request.BaseRequest;
import com.BaseProject.utils.restExceptionHanding.AbstractRestHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class BaseController extends AbstractRestHandler {

    @GetMapping()
    public ResponseEntity<?> getAll(){
        return null;
    };
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        return null;
    }
    @PostMapping()
    public ResponseEntity<?> addNewItem(@RequestBody BaseRequest request){
        return null;
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable int id, @RequestBody BaseRequest request){
        return null;
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id){
        return null;
    }
}
