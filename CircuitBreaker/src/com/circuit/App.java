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
                    // ģ���������
                    try {
                        Thread.sleep(new Random().nextInt(20) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try{
                        // ���۶���
                        if (circuitBreaker.canPassCheck()){
                            // do something
                            System.out.println("����ҵ���߼�����");

                            // ģ����ڵķ���ָ�״̬
                            if (countDownLatch.getCount() >= maxNum/2){
                                // ģ�����ʧ��
                                if (new Random().nextInt(2) == 1){
                                    throw new Exception("mock error");
                                }
                            }
                            countDownLatch.countDown();
                        } else {
                            System.out.println("����ҵ���߼�����");
                            Thread.sleep(500);
                            
                        }
                    }catch (Exception e){
                        System.out.println("ҵ��ִ��ʧ����");
                        // �۶���������
                        circuitBreaker.countFailNum();
                    }
                }
            }).start();

            // ģ���������
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