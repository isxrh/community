package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CaffeineTests {

    @Autowired
    private DiscussPostService postService;

    @Test
    public void initDataForTest() {
        for (int i = 0; i < 300000; i++) {
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("压力测试");
            post.setContent("使用JMeter工具做压力测试，先在数据库里准备大量数据；在测试方法中比如我初始化30000条数据， 先进行不加缓存测试 双击启动bin目录下的jmeter.bat 添加-》线程-》线程组 填写线程名称，线程数，循环次数，持续时间等 添加-》取样器-》Http请求 填写服务器名称（ip），端口号，http请求方式，路径，编码类型等 添加-》定时器-》统一随机定时器 填写线程延迟属性（ms） 添加-》监听器-》聚合报告 在聚合报告看压力测试结果：主要看吞吐量那一栏（多测试几次，且要保证异常为0%） 然后加缓存进行测试 启用缓存，与上述一样进行压力测试，可以发现有缓存的吞吐量较高");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            postService.addDiscussPost(post);
        }
    }

    @Test
    public void testCache() {
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 0));
    }
}
