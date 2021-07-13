package com.aliencat.application.webchat.socket;

import com.aliencat.application.common.utils.JsonUtils;
import com.aliencat.application.webchat.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@ServerEndpoint(value = "/websocket")
@Slf4j
public class Socket {
    public static Map<String, Session> sessionMap = new HashMap<String, Session>();
    @Autowired
    UserService userService;
    private Session session;
    @Resource
    private RedisTemplate redisTemplate;

    @OnOpen
    public void startSocket(Session session) {
        this.session = session;
        log.debug("链接成功");
        if (sessionMap.size() == 0) {
            return;
        }
    }

    @OnMessage
    public void getMessgae(Session session, String str, boolean last) {
        if (session.isOpen()) {
            try {
                log.debug(str);
                Message msg = JsonUtils.jsonToPojo(str, Message.class);
                Message toMessage = msg;
                toMessage.setFrom(msg.getId());
                toMessage.setTo(msg.getTo());

                //开启socket链接时msg的值是newUser
                if (msg.getMsg().equals("newUser")) {
                    //如果存在这个用户
                    if (sessionMap.containsKey(msg.getId())) {
                        //删除掉防止重复(如果更换了电脑或者浏览器。这个操作能保证session与id是唯一对应的且session是最新的)
                        sessionMap.remove(msg.getId());
                    }
                    //将用户id放进去
                    sessionMap.put(msg.getId(), session);
                    //发送在线人数
                    this.pubMessage(session);
                } else {
                    Session toSession = sessionMap.get(msg.getTo());
                    if (toSession != null && toSession.isOpen()) {
                        toSession.getBasicRemote().sendText(JsonUtils.objectToJson(toMessage).toString(), last);
                        //发送在线人数
                        this.pubMessage(toSession);
                    } else {
                        toMessage.setMsg("用户不存在");
                        toMessage.setFrom("系统");
                        session.getBasicRemote().sendText(JsonUtils.objectToJson(toMessage).toString(), last);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            log.debug("session is closed");
        }
    }


    private void pubMessage(Session session) throws IOException {
        Set userIds = sessionMap.keySet();
        StringBuffer sBuffer = new StringBuffer();
        for (Object str1 : userIds) {
            sBuffer.append(str1.toString() + ",");
        }
        Message message = new Message();
        message.setLive(sBuffer.toString());
        session.getBasicRemote().sendText(JsonUtils.objectToJson(message), true);

        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("com.aliencat", 111);
        operations.set("com.aliencat.application", 1, 1, TimeUnit.SECONDS);
    }

}