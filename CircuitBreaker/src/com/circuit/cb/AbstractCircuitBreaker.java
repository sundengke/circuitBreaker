package com.circuit.cb;

import com.circuit.state.CBState;
import com.circuit.state.CloseCBState;

/**
 *
 * �����۶���
 */
public abstract class AbstractCircuitBreaker implements CircuitBreaker {
    /**
     * �۶�����ǰ״̬
     */
    private volatile CBState state = new CloseCBState();

    /**
     * ���۶����رյ�����£��ڶ�������ʧ�ܶ��ٴν��룬�۶ϴ�״̬��Ĭ��1�����ڣ�ʧ��10�ν����״̬��
     */
    public String thresholdFailRateForClose = "10/600";

    /**
     * ���۶����򿪵�����£��۶϶��������뿪״̬����Ĭ���۶�3���ӣ�
     */
    public int thresholdIdleTimeForOpen = 18000;

    /**
     * ���۶����뿪�������, �ڶ������ڷŶ��ٴ�����ȥ��̽(Ĭ��1�����ڣ���10������)
     */
    public String thresholdPassRateForHalfOpen = "10/600";

    /**
     * ���۶����뿪�������, ��̽�ڼ䣬����г������ٴ�ʧ�ܵģ����½����۶ϴ�״̬�����߽����۶Ϲر�״̬��
     */
    public int thresholdFailNumForHalfOpen = 1;


    public CBState getState() {
        return state;
    }

    public void setState(CBState state) {
        // ��ǰ״̬�����л�Ϊ��ǰ״̬
        CBState currentState = getState();
        if (currentState.getStateName().equals(state.getStateName())){
            return;
        }

        // ���̻߳�������
        synchronized (this){
            // �����ж�
            currentState = getState();
            if (currentState.getStateName().equals(state.getStateName())){
                return;
            }

            // ����״̬
            this.state = state;
            System.out.println("�۶���״̬ת�ƣ�" + currentState.getStateName() + "->" + state.getStateName());
        }
    }
}