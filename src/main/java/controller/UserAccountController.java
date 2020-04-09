package controller;

import com.alibaba.fastjson.JSON;
import dao.UserRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.User;

@Controller
public class UserAccountController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    public  AmqpTemplate rabbitTemplate;
    @GetMapping("/login")
    @ResponseBody
    public String userLogin(@RequestParam String username, @RequestParam String password) {
        User one = userRepository.findByUsernameAndPassword(username,password);
        User user = new User();
        user.setNickname("衣服架子");
        user.setUsername("aaa");
        user.setPassword("ddd");
        User addUser = userRepository.saveAndFlush(user);
        return JSON.toJSONString(addUser);

    }


    @ResponseBody
    @GetMapping("/test")
    public String test(){
        rabbitTemplate.convertAndSend("amq.topic", "0000-0000-0000-0000", "hello world");
        return "ok";
    }

}
