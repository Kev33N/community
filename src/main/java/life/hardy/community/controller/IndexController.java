package life.hardy.community.controller;

import life.hardy.community.domain.User;
import life.hardy.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies){
            System.out.println(cookie.getName()+"==============="+cookie.getValue());
            if (cookie.getName().equals("token")){
                token = cookie.getValue();
                System.out.println(token+"-----------------");
                break;
            }
        }
        System.out.println(token+"========");
        if (token!=null){
            User user = userMapper.findByToken(token);
            if (user!=null)
                request.getSession().setAttribute("user",user);
        }
        return "index";
    }
}
