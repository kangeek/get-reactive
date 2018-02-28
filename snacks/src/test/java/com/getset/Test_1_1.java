package com.getset;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Test_1_1 {

    /**
     * 1. 响应式之道 - 1 什么是响应式编程
     *
     * 使用Java Stream演示第一章购物的例子。
     * Java Stream在处理响应式编程时有其局限性，这里仅做示意。
     */
    @Test
    public void testJavaStream() {
        @Data
        @AllArgsConstructor
        class Product {
            private String name;
            private double price;
        }

        @Data
        @AllArgsConstructor
        class CartItem {
            private Product product;
            private int quantity;
        }

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Product("饼干", 8.5), 4));
        cartItems.add(new CartItem(new Product("干果", 39.9), 3));
        cartItems.add(new CartItem(new Product("长粒香米", 39), 2));
        cartItems.add(new CartItem(new Product("玉米油", 42.9), 1));
        cartItems.add(new CartItem(new Product("饼干", 8.5), 1));
        cartItems.add(new CartItem(new Product("牛肉干", 59), 2));
        cartItems.add(new CartItem(new Product("剃须刀", 259), 1));
        cartItems.add(new CartItem(new Product("干果", 39.9), -1));
        cartItems.add(new CartItem(new Product("牛肉干", 59), -1));

        // 对List进行迭代的处理方法
//        double sum = 0;
//        for (CartItem item : cartItems) {
//            double result = item.getProduct().getPrice() * item.getQuantity();
//            result = (result > 199) ? (result - 40) : result;
//            sum += result;
//        }

        // 直接使用Stream的of方法构造数据流
//        Stream<CartItem> cartItemStream = Stream.of(
//                new CartItem(new Product("饼干", 8.5), 4),
//                new CartItem(new Product("干果", 39.9), 3),
//                new CartItem(new Product("长粒香米", 39), 2),
//                new CartItem(new Product("玉米油", 42.9), 1),
//                new CartItem(new Product("饼干", 8.5), 1),
//                new CartItem(new Product("牛肉干", 59), 2),
//                new CartItem(new Product("剃须刀", 259), 1),
//                new CartItem(new Product("干果", 39.9), -1),
//                new CartItem(new Product("牛肉干", 59), -1)
//        );


        double sum = cartItems.stream()
                // 分别计算商品金额
                .mapToDouble(value -> value.getProduct().getPrice() * value.getQuantity())
                // 计算满减后的商品金额
                .map(operand -> (operand > 199) ? (operand - 40) : operand)
                // 金额累加
                .sum();
        sum = (sum > 500) ? sum : (sum + 50);
        System.out.println("应付款金额：" + sum);
    }


}
