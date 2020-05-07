package com.dean.demo.service.ibmmq;

import com.ibm.mq.*;
import org.springframework.stereotype.Service;

/**
 * @author Dean
 * @version 1.0
 * @date 2020/5/3 12:15
 */
@Service
public class MqDemo {

    //队列信息
    private static MQQueueManager qMgr;
    private static final String MQ_MANAGER = "MQ_LINUX";
    private static final String QUEUE_NAME = "QUEUE_RES";
    //连接信息
    private static final String HOST = "192.168.230.129";
    private static final String CHANNEL = "CONN_CHANNEL";
    private static final Integer PORT = 1414;
    private static final String USER_ID = "mqm";
    private static final String PWD = "hz13554";
    private static Integer CCSID = 1208;

    public static void connect() throws MQException {
        MQEnvironment.hostname = HOST;
        MQEnvironment.channel = CHANNEL;
        MQEnvironment.port = PORT;
        MQEnvironment.userID = USER_ID;
        MQEnvironment.password = PWD;
        MQEnvironment.CCSID = CCSID;
        qMgr = new MQQueueManager(MQ_MANAGER);
    }

    public static void sendMsg(String msgStr) {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            // 建立Q1通道的连接
            queue = qMgr.accessQueue(QUEUE_NAME, openOptions, null, null, null);
            MQMessage msg = new MQMessage();// 要写入队列的消息
            msg.format = MQC.MQFMT_STRING;
            msg.characterSet = CCSID;
            msg.encoding = CCSID;
            // msg.writeObject(msgStr); //将消息写入消息对象中
            msg.writeString(msgStr);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            msg.expiry = -1; // 设置消息用不过期
            queue.put(msg, pmo);// 将消息放入队列
        }catch (MQException ex) {
            System.out.println("An IBM MQ Error occurred : Completion Code " + ex.completionCode
                    + " Reason Code " + ex.reasonCode);
            ex.printStackTrace();
            for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
                System.out.println("... Caused by ");
                t.printStackTrace();
            }

        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            System.out.println("An IOException occurred whilst writing to the message buffer: " + ex);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (queue != null) {
                try {
                    queue.close();
                } catch (MQException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void receiveMsg() {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            queue = qMgr.accessQueue(QUEUE_NAME, openOptions, null, null, null);
            System.out.println("该队列当前的深度为:" + queue.getCurrentDepth());
            System.out.println("===========================");
            int depth = queue.getCurrentDepth();
            // 将队列的里的消息读出来
            while (depth-- > 0) {
                MQMessage msg = new MQMessage();// 要读的队列的消息
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                queue.get(msg, gmo);
                System.out.println("消息的int大小为：" + msg.getDataLength());
                System.out.println("消息的内容：\n" + msg.readStringOfByteLength(msg.getDataLength()));
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (queue != null) {
                try {
                    queue.close();
                } catch (MQException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessageToMq(String msg){
        try {
            connect();
            sendMsg(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
