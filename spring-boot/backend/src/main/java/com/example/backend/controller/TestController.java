package com.example.backend.controller;

import javax.validation.Valid;

import com.example.backend.entity.TestModel;
import com.example.backend.exception.ValidateException;
import com.example.backend.repo.TestRepository;
import com.example.backend.service.ResponseService;
import com.example.backend.viewModel.ResponseFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@CrossOrigin(origins = "http://localhost:9000")
@Api(tags = "Test")
@RequestMapping(path = "/api/v1/test", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TestController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private TestRepository testRepository;

    /**************
     *** RETURN ***
     **************/

    @GetMapping(value = "response")
    public ResponseEntity<ResponseFormat> response() {
        return responseService.basic(HttpStatus.OK, "response test.", null);
    }

    @GetMapping(value = "exception")
    public ResponseEntity<ResponseFormat> exception() {
        throw new ValidateException(HttpStatus.BAD_REQUEST, "exception test.", null);
    }

    /****************
     *** JPA CRUD ***
     ****************/

    @GetMapping(value = "getdata")
    public ResponseEntity<ResponseFormat> getdata() {
        return responseService.basic(HttpStatus.OK, "getdata test.", testRepository.findAll());
    }

    @PostMapping(value = "postdata")
    public ResponseEntity<ResponseFormat> postdata(@RequestBody TestModel testModel) {
        testRepository.save(testModel);
        return responseService.basic(HttpStatus.OK, "postdata test.", testModel);
    }

    @PutMapping(value = "patchdata")
    public ResponseEntity<ResponseFormat> patchdata(@RequestParam(value = "id", required = true) Integer id,
            @Valid @RequestBody TestModel input) {
        TestModel output = testRepository.findById(id)
                .orElseThrow(() -> new ValidateException(HttpStatus.BAD_REQUEST, "id not exist.", null));
        output.setTitle(input.getTitle());
        output.setSummary(input.getSummary());
        output.setDescription(input.getDescription());
        return responseService.basic(HttpStatus.OK, "patchdata test.", null);
    }

    @DeleteMapping(value = "deletedata")
    public ResponseEntity<ResponseFormat> deletedata(@RequestParam(value = "id") Integer id) {
        testRepository.deleteById(id);
        return responseService.basic(HttpStatus.OK, "deletedata test.", null);
    }
}
