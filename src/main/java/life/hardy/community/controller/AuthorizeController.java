package life.hardy.community.controller;

import life.hardy.community.domain.User;
import life.hardy.community.dto.AccessTokenDTO;
import life.hardy.community.dto.GithubUser;
import life.hardy.community.dto.LocalUser;
import life.hardy.community.mapper.UserMapper;
import life.hardy.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client_id}")
    private String clientId;

    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.redirect_uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){

        AccessTokenDTO accessTokenDto = new AccessTokenDTO();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        System.out.println(accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser);
        if (githubUser!=null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else {
            return "redirect:/";
        }

    }

    @RequestMapping("/user/login")
    public String loginTest(String username,String password,HttpServletRequest request){
        User user = new User();
        user.setToken(UUID.randomUUID().toString());
        user.setName(username);
        user.setAccountId(password);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        userMapper.insert(user);

        request.getSession().setAttribute("user",user);
        System.out.println(username+":"+password);
        return "redirect:/";
    }
}
