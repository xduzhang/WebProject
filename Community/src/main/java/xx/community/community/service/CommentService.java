package xx.community.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xx.community.community.dto.CommentDTO;
import xx.community.community.enums.CommentTypeEnum;
import xx.community.community.enums.NotificationEnum;
import xx.community.community.enums.NotificationStatusEnum;
import xx.community.community.mapper.CommentMapper;
import xx.community.community.mapper.NotificationMapper;
import xx.community.community.mapper.QuestionMapper;
import xx.community.community.mapper.UserMapper;
import xx.community.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment,User commentator) {
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            Comment comment1=commentMapper.selectByPrimaryKey(comment.getParentId());
            commentMapper.insert(comment);
            Comment updateComment=new Comment();
            updateComment.setCommentCount(comment1.getCommentCount()+1);
            CommentExample commentExample=new CommentExample();
            commentExample.createCriteria().andIdEqualTo(comment.getParentId());
            commentMapper.updateByExampleSelective(updateComment,commentExample);
//            通知
            Question question = questionMapper.selectByPrimaryKey(comment1.getParentId());
            Notification notification = new Notification();
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setType(NotificationEnum.REPLY_COMMENT.getType());
            Long outerId = question.getId();
            notification.setOuterid(outerId);
            notification.setNotifier(comment.getCommentator());
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification.setReceiver(comment1.getCommentator());
            notification.setNotifierName(commentator.getName());
            notification.setOuterTitle(question.getTitle());
            notificationMapper.insert(notification);


        } else if (comment.getType() != null) {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            commentMapper.insert(comment);
            Question upadteQuestion = new Question();
            upadteQuestion.setCommentCount(question.getCommentCount() + 1);
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(comment.getParentId());
            questionMapper.updateByExampleSelective(upadteQuestion, questionExample);
            Notification notification = new Notification();
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setType(NotificationEnum.REPLY_COMMENT.getType());
            Long outerId = question.getId();
            notification.setOuterid(outerId);
            notification.setNotifier(comment.getCommentator());
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification.setReceiver(question.getCreator());
            notification.setNotifierName(commentator.getName());
            notification.setOuterTitle(question.getTitle());
            notificationMapper.insert(notification);

        }
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }else {
            Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
            List<Long> userIds=new ArrayList<>();
            userIds.addAll(commentators);
            UserExample userExample=new UserExample();
            userExample.createCriteria().andIdIn(userIds);
            List<User> users = userMapper.selectByExample(userExample);
            Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

            List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                BeanUtils.copyProperties(comment,commentDTO);
                commentDTO.setUser(userMap.get(comment.getCommentator()));
                return commentDTO;
            }).collect(Collectors.toList());
            return commentDTOs;
        }
    }
}
