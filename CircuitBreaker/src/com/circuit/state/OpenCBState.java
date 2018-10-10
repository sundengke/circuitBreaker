package com.circuit.state;

import com.circuit.cb.AbstractCircuitBreaker;

/**
 * �۶���-��״̬
 */
public class OpenCBState implements CBState {
	/**
     * ���뵱ǰ״̬�ĳ�ʼ��ʱ��
     */
    private long stateTime = System.currentTimeMillis();

    public String getStateName() {
        // ��ȡ��ǰ״̬����
        return this.getClass().getSimpleName();
    }

    public void checkAndSwitchState(AbstractCircuitBreaker cb) {
        // ��״̬�����ȴ�ʱ���Ƿ��ѵ���������˾��л����뿪״̬
        long now = System.currentTimeMillis();
        long idleTime = cb.thresholdIdleTimeForOpen * 1000L;
        if (stateTime + idleTime <= now){
            cb.setState(new HalfOpenCBState());
        }
    }

    public boolean canPassCheck(AbstractCircuitBreaker cb) {
        // ���״̬
        checkAndSwitchState(cb);
        return false;
    }

    public void countFailNum(AbstractCircuitBreaker cb) {
        // nothing
    }
}
