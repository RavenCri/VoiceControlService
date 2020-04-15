package com.web.controller;


import com.auth0.jwt.JWT;
import com.web.jwt.annotation.UserLoginToken;
import com.web.result.Result;
import com.web.service.UserOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author raven
 * @Description
 * @Date  19:17
 * @Param
 * @return
 **/
@RestController
@RequestMapping("roobot")
@Api(value = "UserOperationController|智能回复接口，用来控制硬件")
public class UserOperationController {
    @Autowired
    private UserOperationService userOperationService;
    @UserLoginToken
    @ApiOperation("用于机器人智能回复，可以控制硬件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header", name = "token", value = "登录后的token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "word", value = "用户说的话", required = true, dataType = "String"),
    })

    @GetMapping("replay")
    /**
    * @Description: 用来获取智能回复内容，并可以发送控制指令
    * @Param: [token, word]
    * @return: java.lang.String
    * @Author: raven
    * @Date: 2020/4/10
    */
    public Result getReturnWord(@RequestHeader("token") String token,
                                @RequestParam(value = "word",required = false) String word)   {
        String userId = JWT.decode(token).getAudience().get(0);
        if(word == null ||word.trim().equals("") ){
            Result result = new Result();
            result.setCode(200);
            result.setMsg("传参不正确！");

            return result;
        }
        return  userOperationService.getWord(userId,word);
    }
}