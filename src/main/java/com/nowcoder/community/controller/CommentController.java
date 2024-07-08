package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        return "redirect:/discuss/detail/" + discussPostId;
    }

    @RequestMapping(path = "/myReply/{userId}", method = RequestMethod.GET)
    public String getUserReplyPage(@PathVariable("userId") int userId, Model model, Page page) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }

        model.addAttribute("user", user);

        int commentCount = commentService.findCommentCountByUserId(user.getId());
        model.addAttribute("commentCount", commentCount);

        page.setLimit(5);
        page.setPath("/user/myReply/" + user.getId());
        page.setRows(commentCount);

        List<Comment> list = commentService.findCommentByUserId(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> comments  = new ArrayList<>();
        if (list != null) {
            for (Comment comment: list) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);

                DiscussPost post = discussPostService.findDiscussPostById(comment.getEntityId());
                map.put("post", post);

                comments.add(map);
            }
        }
        model.addAttribute("comments", comments);
        return "/site/my-reply";
    }
}




































