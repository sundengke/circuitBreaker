package com.circuit.cb;
/**
 * �۶����ӿ�
 */
public interface CircuitBreaker {
	
	/**
     * �����۶���
     */
    void reset();

    /**
     * �Ƿ�����ͨ���۶���
     */
    boolean canPassCheck();

    /**
     * ͳ��ʧ�ܴ���
     */
    void countFailNum();
}
