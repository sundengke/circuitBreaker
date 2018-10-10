package com.circuit.state;

import com.circuit.cb.AbstractCircuitBreaker;

/**
 * �۶���״̬
 */
public interface CBState {
	 /**
     * ��ȡ��ǰ״̬����
     */
    String getStateName();

    /**
     * ����Լ�У�鵱ǰ״̬�Ƿ���ҪŤת
     */
    void checkAndSwitchState(AbstractCircuitBreaker cb);

    /**
     * �Ƿ�����ͨ���۶���
     */
    boolean canPassCheck(AbstractCircuitBreaker cb);

    /**
     * ͳ��ʧ�ܴ���
     */
    void countFailNum(AbstractCircuitBreaker cb);
}
