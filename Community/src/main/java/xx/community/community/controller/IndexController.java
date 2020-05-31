package xx.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xx.community.community.dto.PaginationDTO;
import xx.community.community.dto.QuestionDTO;
import xx.community.community.mapper.QuestionMapper;
import xx.community.community.mapper.UserMapper;
import xx.community.community.model.Question;
import xx.community.community.model.User;
import xx.community.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public  String index(HttpServletRequest request,
                         Model model,
                          @RequestParam(name="page",defaultValue="1") Integer page,
                         @RequestParam(name="size",defaultValue = "5") Integer size){


        PaginationDTO pagination=questionService.list(page,size);
         model.addAttribute("pagination",pagination);

        return "index";


    }
}
