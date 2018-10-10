package com.circuit.cb;

import com.circuit.state.CloseCBState;

/**
 *
 * 本地熔断器(把它当成了工厂了)
 */
public class LocalCircuitBreaker extends AbstractCircuitBreaker {
    public LocalCircuitBreaker(String failRateForClose,
                               int idleTimeForOpen,
                               String passRateForHalfOpen, int failNumForHalfOpen){
        this.thresholdFailRateForClose = failRateForClose;
        this.thresholdIdleTimeForOpen = idleTimeForOpen;
        this.thresholdPassRateForHalfOpen = passRateForHalfOpen;
        this.thresholdFailNumForHalfOpen = failNumForHalfOpen;
    }

    public void reset() {
        this.setState(new CloseCBState());
    }

    public boolean canPassCheck() {
        return getState().canPassCheck(this);
    }

    public void countFailNum() {
        getState().countFailNum(this);
    }
}