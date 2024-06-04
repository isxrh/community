package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String keywords;
            while ((keywords = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeywords(keywords);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败" + e.getMessage());
        }

    }

    // 将一个敏感词添加到前缀树中
    private void addKeywords(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i=0; i<keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                // 初始化字节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            tempNode = subNode;

            // 设置结束标志
            if (i == keyword.length()-1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 需要过滤的文本
     * @return 过滤后的文本
     */
    public String filter (String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针1：指向树
        TrieNode tempNode = rootNode;
        // 指针1：首
        int begin = 0;
        // 指针1：尾
        int position = 0;
        // 结果
        StringBuilder stringBuilder = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1指向根节点，将此符号计入结果，让指针2向下走一步
                if (tempNode == rootNode) {
                    stringBuilder.append(c);
                    begin++;
                }
                // 无论符号在哪里，指针三都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                stringBuilder.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词
                stringBuilder.append(REPLACEMENT);
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                position++;
            }
        }
        // 将最后一批字符计入结果
        stringBuilder.append(text.substring(begin));
        return stringBuilder.toString();
    }

    // 判断是否为符号
    private boolean isSymbol (Character c) {
        //  0x2E80 ~ 0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode {
        // 关键词结束标志
        private boolean isKeywordEnd = false;

        // 子节点(key：下级节点字符， value：下级节点）
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public  void addSubNode (Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
