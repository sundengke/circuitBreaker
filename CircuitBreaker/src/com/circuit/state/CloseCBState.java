package com.circuit.state;

import java.util.concurrent.atomic.AtomicInteger;

import com.circuit.cb.AbstractCircuitBreaker;

/**
 * �۶���-�ر�״̬
 */
public class CloseCBState implements CBState {

	 /**
     * ���뵱ǰ״̬�ĳ�ʼ��ʱ��
     */
    private long stateTime = System.currentTimeMillis();

    /**
     * �ر�״̬��ʧ�ܼ��������Լ�ʧ�ܼ�������ʼ��ʱ��
     */
    private AtomicInteger failNum = new AtomicInteger(0);
    private long failNumClearTime = System.currentTimeMillis();

    public String getStateName() {
        // ��ȡ��ǰ״̬����
        return this.getClass().getSimpleName();
    }

    public void checkAndSwitchState(AbstractCircuitBreaker cb) {
        // ��ֵ�жϣ����ʧ�ܵ��﷧ֵ���л�״̬����״̬
        long maxFailNum = Long.valueOf(cb.thresholdFailRateForClose.split("/")[0]);
        if (failNum.get() >= maxFailNum){
            cb.setState(new OpenCBState());
        }
    }

    public boolean canPassCheck(AbstractCircuitBreaker cb) {
        // �ر�״̬������Ӧ������ͨ��
        return true;
    }

    public void countFailNum(AbstractCircuitBreaker cb) {
        // ���������Ƿ�����ˣ��������¼���
        long period = Long.valueOf(cb.thresholdFailRateForClose.split("/")[1]) * 1000;
        long now = System.currentTimeMillis();
        if (failNumClearTime + period <= now){
            failNum.set(0);
        }
        // ʧ�ܼ���
        failNum.incrementAndGet();

        // ����Ƿ��л�״̬
        checkAndSwitchState(cb);
    }
}
