package com.example.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import com.example.backend.exception.ValidateException;
import com.example.backend.service.ResponseService;
import com.example.backend.viewModel.ResponseFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Upload")
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping(path = "/api/v1/upload", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UploadController {

    @Autowired
    private ResponseService responseService;

    public static final String uploadingDir = System.getProperty("user.dir") + "/workspace/storage/";

    /*******************
     *** FILE UPLOAD ***
     *******************/

    @ApiOperation(value = "upload file.", notes = "")
    @ApiImplicitParam(name = "file", value = "", required = true, paramType = "body")
    @PostMapping(value = "file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseFormat> postdata(@RequestParam("file") MultipartFile file) {
        try {
            File input = new File(uploadingDir + file.getOriginalFilename());
            file.transferTo(input);
            return responseService.basic(HttpStatus.OK, file.getOriginalFilename() + " uploaded.", null);
        } catch (IllegalStateException | IOException e) {
            throw new ValidateException(HttpStatus.BAD_REQUEST, "Exception: " + e.toString(), null);
        }
    }
}
