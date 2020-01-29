package com.ws.common.websocket;

import com.alibaba.fastjson.JSON;
import com.ws.common.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * websocket 服务类
 * @author WS-
 */
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {
    private final static Logger log=LoggerFactory.getLogger(WebSocketServer.class);
    //在线人数
    private static  int onlineCount=0;

    /**
     *     concurrent包的线程安全set，存放每个客户端对应的websocket对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet=new CopyOnWriteArraySet<>();


    /**
     *与某个客户端的连接对话，需要通过它来给客户端发送数据
     */
    private Session session;
    private String sid="";

    /**
     * 连接成功调用的方法
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid")String sid){
        this.session=session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新的客户端开始监听:"+sid+",当前在线人数为："+getOnlineCount());
        this.sid=sid;
        try {
            Map<Object, Object> map = ResultUtil.success(null, "连接成功");
            String msg = JSON.toJSON(map).toString();
            sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("websocket IO 异常");
        }
    }

    /**
     * 收到客户端的消息后调用的方法
     * @param msg 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String msg,Session session){
        log.info("收到客户端"+sid+"的消息："+msg);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 连接关闭后调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一个连接关闭了！当前在线人数为："+getOnlineCount());
    }

    @OnError
    public void onError(Session session,Throwable error){
        log.error("发生错误");
        webSocketSet.remove(this);
        error.printStackTrace();
    }




    /**
     * 实现服务器主动推送
     * @param msg 需要推动的信息
     */
    private void sendMessage(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }


    private static synchronized int  getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    private static synchronized void  addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
}
