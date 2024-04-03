package com.bear.reseeding.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StreamExample {
    public static void main(String[] args) {
        // 创建一个包含一些元素的列表
        List<String> words = Arrays.asList("apple", "banana", "cat", "dog", "elephant");

        // 筛选出长度大于等于4的单词
        List<String> filteredWords = words.stream()
                .filter(word -> word.length() >= 4)
                .collect(Collectors.toList());
        System.out.println("筛选后的单词列表：" + filteredWords);

        // 将单词转换为大写
        List<String> uppercaseWords = words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("转换为大写后的单词列表：" + uppercaseWords);

        // 对单词按长度进行排序
        List<String> sortedWords = words.stream()
                .sorted((word1, word2) -> word1.length() - word2.length())
                .collect(Collectors.toList());
        System.out.println("按长度排序后的单词列表：" + sortedWords);

        // 获取单词列表中所有单词的总长度
        int totalLength = words.stream()
                .mapToInt(String::length)
                .sum();
        System.out.println("单词列表中所有单词的总长度：" + totalLength);

        System.out.println();
        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);
    }
}
