package com.circuit;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.circuit.cb.CircuitBreaker;
import com.circuit.cb.LocalCircuitBreaker;

public class App {

    public static void main(String[] args) throws InterruptedException {
        final int maxNum = 200;
        final CountDownLatch countDownLatch = new CountDownLatch(maxNum);

        final CircuitBreaker circuitBreaker = new LocalCircuitBreaker("5/200", 5, "5/100", 3);

        for (int i=0; i < maxNum; i++){
            new Thread(new Runnable() {
                public void run() {
                    // 模拟随机请求
                    try {
                        Thread.sleep(new Random().nextInt(20) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try{
                        // 过熔断器
                        if (circuitBreaker.canPassCheck()){
                            // do something
                            System.out.println("正常业务逻辑操作");

                            // 模拟后期的服务恢复状态
                            if (countDownLatch.getCount() >= maxNum/2){
                                // 模拟随机失败
                                if (new Random().nextInt(2) == 1){
                                    throw new Exception("mock error");
                                }
                            }
                            countDownLatch.countDown();
                        } else {
                            System.out.println("拦截业务逻辑操作");
                            //模拟自旋
                            Thread.sleep(500);
                            //失败的线程在自旋之后重新获取任务
                            Thread.currentThread().run();
                        }
                    }catch (Exception e){
                        System.out.println("业务执行失败了");
                        // 熔断器计数器
                        circuitBreaker.countFailNum();
                    }
                }
            }).start();

            // 模拟随机请求
            try {
                Thread.sleep(new Random().nextInt(5) * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        countDownLatch.await();
        System.out.println("end");
    }
}
