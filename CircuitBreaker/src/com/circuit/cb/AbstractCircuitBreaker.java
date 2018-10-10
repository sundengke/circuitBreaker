package com.circuit.cb;

import com.circuit.state.CBState;
import com.circuit.state.CloseCBState;

/**
 *
 * 基础熔断器
 */
public abstract class AbstractCircuitBreaker implements CircuitBreaker {
    /**
     * 熔断器当前状态
     */
    private volatile CBState state = new CloseCBState();

    /**
     * 在熔断器关闭的情况下，在多少秒内失败多少次进入，熔断打开状态（默认1分钟内，失败10次进入打开状态）
     */
    public String thresholdFailRateForClose = "10/600";

    /**
     * 在熔断器打开的情况下，熔断多少秒进入半开状态，（默认熔断3分钟）
     */
    public int thresholdIdleTimeForOpen = 18000;

    /**
     * 在熔断器半开的情况下, 在多少秒内放多少次请求，去试探(默认1分钟内，放10次请求)
     */
    public String thresholdPassRateForHalfOpen = "10/600";

    /**
     * 在熔断器半开的情况下, 试探期间，如果有超过多少次失败的，重新进入熔断打开状态，否者进入熔断关闭状态。
     */
    public int thresholdFailNumForHalfOpen = 1;


    public CBState getState() {
        return state;
    }

    public void setState(CBState state) {
        // 当前状态不能切换为当前状态
        CBState currentState = getState();
        if (currentState.getStateName().equals(state.getStateName())){
            return;
        }

        // 多线程环境加锁
        synchronized (this){
            // 二次判断
            currentState = getState();
            if (currentState.getStateName().equals(state.getStateName())){
                return;
            }

            // 更新状态
            this.state = state;
            System.out.println("熔断器状态转移：" + currentState.getStateName() + "->" + state.getStateName());
        }
    }
}