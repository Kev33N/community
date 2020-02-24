package life.hardy.community.controller;

import life.hardy.community.dto.AccessTokenDto;
import life.hardy.community.dto.GithubUser;
import life.hardy.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClientId("b61df0ff427d7a0af9c8");
        accessTokenDto.setClientSecret("b463d40288c7ee468528ef24a178496a7f6d6656");
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirectUri("http://localhost:8887/callback");
        accessTokenDto.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        System.out.println("accessToken:"+accessToken);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user);
        return "index";
    }
}
