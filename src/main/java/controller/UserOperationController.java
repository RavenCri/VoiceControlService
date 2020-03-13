package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import server.UserOperationServer;

/**
 * @Author raven
 * @Description
 * @Date  19:17
 * @Param
 * @return
 **/
@RestController
public class UserOperationController {
    @Autowired
    private UserOperationServer userOperationServer;

    @ResponseBody
    @GetMapping("/replay")
    public String getReturnWord(@RequestParam(value = "word",required = false) String word) throws InterruptedException {

        if(word == null ||word.trim().equals("") ){
            return "您什么也没有说";
        }
        return  userOperationServer.getWord(word);

    }




}