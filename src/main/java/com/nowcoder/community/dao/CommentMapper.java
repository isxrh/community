package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    /**
     * 查询用户所有的对帖子的评论
     */
    List<Comment> selectCommentByUserId(int userId, int offset, int limit);

    /**
     * 查询用户所有的对帖子的评论的数量
     */
    int selectCountByUserId(int userId);

    Comment selectCommentById(int id);
}
