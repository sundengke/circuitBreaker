package com.circuit.state;

import java.util.concurrent.atomic.AtomicInteger;

import com.circuit.cb.AbstractCircuitBreaker;

/**
 * �۶���-�뿪״̬
 */
public class HalfOpenCBState implements CBState {
	/**
     * ���뵱ǰ״̬�ĳ�ʼ��ʱ��
     */
    private long stateTime = System.currentTimeMillis();

    /**
     * �뿪״̬��ʧ�ܼ�����
     */
    private AtomicInteger failNum = new AtomicInteger(0);

    /**
     * �뿪״̬������ͨ���ļ�����
     */
    private AtomicInteger passNum = new AtomicInteger(0);


    public String getStateName() {
        // ��ȡ��ǰ״̬����
        return this.getClass().getSimpleName();
    }

    public void checkAndSwitchState(AbstractCircuitBreaker cb) {
        // �жϰ뿪ʱ���Ƿ����
        long idleTime = Long.valueOf(cb.thresholdPassRateForHalfOpen.split("/")[1]) * 1000L;
        long now = System.currentTimeMillis();
        if (stateTime + idleTime <= now){
            // ����뿪״̬�ѽ�����ʧ�ܴ����Ƿ񳬹��˷�ֵ
            int maxFailNum = cb.thresholdFailNumForHalfOpen;
            if (failNum.get() >= maxFailNum){
                // ʧ�ܳ�����ֵ����Ϊ����û�лָ������½����۶ϴ�״̬
                cb.setState(new OpenCBState());
            }else {
                // û��������Ϊ����ָ��������۶Ϲر�״̬
                cb.setState(new CloseCBState());
            }
        }
    }

    public boolean canPassCheck(AbstractCircuitBreaker cb) {
        // ����Ƿ��л�״̬
        checkAndSwitchState(cb);

        // �����˷�ֵ�����ٷ���
        int maxPassNum = Integer.valueOf(cb.thresholdPassRateForHalfOpen.split("/")[0]);
        if (passNum.get() > maxPassNum){
            return false;
        }
        // ����Ƿ񳬹��˷�ֵ
        if (passNum.incrementAndGet() <= maxPassNum){
            return true;
        }
        return false;
    }

    public void countFailNum(AbstractCircuitBreaker cb) {
        // ʧ�ܼ���
        failNum.incrementAndGet();

        // ����Ƿ��л�״̬
        checkAndSwitchState(cb);
    }
}
