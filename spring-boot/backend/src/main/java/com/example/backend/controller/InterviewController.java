package com.example.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.example.backend.exception.ValidateException;
import com.example.backend.service.ResponseService;
import com.example.backend.viewModel.ResponseFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@CrossOrigin(value = "http://localhost:9000")
@ApiIgnore
@Api(tags = "Interview")
@RequestMapping(path = "/api/v1/interview", produces = { MediaType.APPLICATION_JSON_VALUE })
public class InterviewController {

    @Autowired
    private ResponseService responseService;

    /*************
     *** STACK ***
     *************/

    @GetMapping(value = "q1")
    public ResponseEntity<ResponseFormat> q1(@RequestParam(value = "input", required = true) String input) {
        Stack<Character> stack = new Stack<Character>();

        for (int i = 0; i < input.length(); i++) {
            if (isLeftParentheses(input.charAt(i))) {
                stack.push(input.charAt(i));
            } else if (isRightParentheses(input.charAt(i))) {
                if (stack.size() <= 0) {
                    // 右括號較多
                    throw new ValidateException(HttpStatus.BAD_REQUEST, input + " is KO.", null);
                }
                Character pair = stack.pop();
                Character ch = getParenthesesPair(input.charAt(i));
                if (pair != ch) {
                    // 沒配對到
                    throw new ValidateException(HttpStatus.BAD_REQUEST, input + " is KO.", null);
                }
            }
        }
        if (stack.size() > 0) {
            // 左括號較多
            throw new ValidateException(HttpStatus.BAD_REQUEST, input + " is KO.", null);
        }
        return responseService.basic(HttpStatus.OK, input + " is OK.", null);
    }

    /********************************
     *** Successione di Fibonacci ***
     ********************************/

    @GetMapping(value = "q2")
    public ResponseEntity<ResponseFormat> q2(@RequestParam(value = "input", required = true) Integer input) {
        List<Long> output = new ArrayList<Long>();
        output.add((long) 1);

        if (input <= 0) {
            throw new ValidateException(HttpStatus.BAD_REQUEST, "input <= 0.", null);
        } else if (input == 1) {
            return responseService.basic(HttpStatus.OK, null, output);
        }

        Long theLastTwo = (long) 0;
        Long theLastNum = (long) 1;
        Long temp;

        for (int index = 2; index <= input; index++) {
            temp = theLastTwo + theLastNum;
            theLastTwo = theLastNum;
            theLastNum = temp;
            output.add(temp);
        }
        return responseService.basic(HttpStatus.OK, null, output);
    }

    // Q1
    static boolean isLeftParentheses(Character c) {
        if (c == '[' || c == '{' || c == '(')
            return true;
        return false;
    }

    static boolean isRightParentheses(Character c) {
        if (c == ']' || c == '}' || c == ')')
            return true;
        return false;
    }

    static Character getParenthesesPair(Character c) {
        if (c == ']')
            return '[';
        else if (c == '}')
            return '{';
        else if (c == ')')
            return '(';
        else
            return ' ';
    }

    // Q2
    static int f(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else
            return f(n - 1) + f(n - 2);
    }
}