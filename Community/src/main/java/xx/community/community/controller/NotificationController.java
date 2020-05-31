package xx.community.community.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xx.community.community.dto.NotificationDTO;
import xx.community.community.dto.PaginationDTO;
import xx.community.community.enums.NotificationEnum;
import xx.community.community.model.User;
import xx.community.community.service.NotificationService;

import javax.servlet.http.HttpServletRequest;





@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,HttpServletRequest request
    ) {


        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO=notificationService.read(id,user);


        if (NotificationEnum.REPLY_COMMENT.getType()==notificationDTO.getType()||NotificationEnum.REPLY_QUESTION.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "redirect:/";
        }





    }
}
