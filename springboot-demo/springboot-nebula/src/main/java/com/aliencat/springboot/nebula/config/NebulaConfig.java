package com.aliencat.springboot.nebula.config;

import com.aliencat.springboot.nebula.mapper.NebulaGraphMapper;
import com.aliencat.springboot.nebula.ocean.session.NebulaPoolSessionManager;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@EnableConfigurationProperties({NebulaProperties.class})
@RequiredArgsConstructor
public class NebulaConfig {

    private final NebulaProperties nebulaProperties;

    private NebulaPool pool;
    private Session session;
    private String spaceName;
    private NebulaGraphMapper nebulaGraphMapper;

    // 服务一启动会执行该方法
//    @PostConstruct
    public void init() {
//        System.out.println(nebulaProperties.getAddress());
//        System.out.println(storagePathConfig.getHBaseUpLoadUrl());
        pool = new NebulaPool();
        session = null;
        spaceName = nebulaProperties.getSpaceName();
        NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
        nebulaPoolConfig.setMaxConnSize(nebulaProperties.getMaxConnSize());
        nebulaPoolConfig.setMinConnSize(nebulaProperties.getMinConnsSize());
        nebulaPoolConfig.setIdleTime(nebulaProperties.getIdleTime());
//        nebulaPoolConfig.setTimeout(nebulaProperties.getTimeout());
//        nebulaPoolConfig.setWaitTime(nebulaProperties.getTimeout());
        try {
            pool.init(assemblyAddress(nebulaProperties.getAddress()), nebulaPoolConfig);
            session = pool.getSession(nebulaProperties.getUserName(), nebulaProperties.getPassworld(), false);
            String sql = useSpaceSql();
            //执行SQL
            execute(session, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("执行init方法，spaceName=" + spaceName);
    }

    @PreDestroy
    public void destroy() {
        if (session != null) {
            session.release();
        }
        pool.close();
    }

//    @Bean
//    @ConditionalOnProperty("nebula.address")
//    public Session session() {
//        try {
//            NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
//            nebulaPoolConfig.setMaxConnSize(nebulaProperties.getMaxConnSize());
//            pool.init(assemblyAddress(nebulaProperties.getAddress()), nebulaPoolConfig);
//            session = pool.getSession(nebulaProperties.getUserName(), nebulaProperties.getPassworld(), false);
//            String sql = useSpaceSql();
//            //执行SQL
//            execute(session, sql);
//        } catch (Exception e) {
//            log.info("address:{},username:{},password:{}", nebulaProperties.getAddress(), nebulaProperties.getUserName(), nebulaProperties.getPassworld());
//            log.error("session异常:" + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return session;
//    }


//    @ConditionalOnProperty("nebula.userName")
    @Bean(name = "nebulaGraphMapper")
    public NebulaGraphMapper getNebulaGraphMapper() {
        try {
            if (Objects.isNull(nebulaGraphMapper)) {
                init();
                nebulaGraphMapper = nebulaGraphMapper(nebulaPoolSessionManager(pool));
            }
        } catch (Exception e) {
            log.info("address:{},username:{},password:{}", nebulaProperties.getAddress(), nebulaProperties.getUserName(), nebulaProperties.getPassworld());
            log.error("session异常:" + e.getMessage());
            e.printStackTrace();
        }

        return nebulaGraphMapper;
    }

    public NebulaPoolSessionManager nebulaPoolSessionManager(NebulaPool nebulaPool) {
        return new NebulaPoolSessionManager(nebulaPool, nebulaProperties.getUserName(), nebulaProperties.getPassworld(), true);
    }

    public NebulaGraphMapper nebulaGraphMapper(
            NebulaPoolSessionManager nebulaPoolSessionManager) {
        return new NebulaGraphMapper(nebulaPoolSessionManager, spaceName);
    }

    /**
     * 使用SPACE
     *
     * @return
     */
    public String useSpace() {
        return "USE " + spaceName + ";";
    }

    /**
     * 使用原始SPACE
     *
     * @return
     */
    public void setInitSpace() {
        nebulaGraphMapper.setSpace(spaceName);
    }

    /**
     * 使用Pagerank SPACE
     *
     * @return
     */
    public void setPagerankSpaceName() {
        nebulaGraphMapper.setSpace(nebulaProperties.getPagerankSpaceName());
    }

    public static ResultSet execute(Session session, String ngql) {
        log.info("执行execute方法，ngql=" + ngql + "session=" + session);
        ResultSet resp = null;
        try {
            resp = session.execute(ngql);
            if (!resp.isSucceeded()) {
                log.error(String.format("Execute: `%s', failed: %s", ngql, resp.getErrorMessage()));
                System.out.println(resp.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            log.error("execute异常");
            log.error(e.getMessage());
        } finally {

        }
        return resp;
    }

    private List<HostAddress> assemblyAddress(String address) {
        String[] split = address.split(",");
        List<HostAddress> addresses = new ArrayList<>(split.length);
        for (int i = 0; i < split.length; i++) {
            String[] host = split[i].split(":");
            addresses.add(new HostAddress(host[0], Integer.valueOf(host[1])));
        }
        return addresses;
    }

    /**
     * 创建数据库
     */
    private void dbCreate() {
        if (StringUtils.isEmpty(spaceName)) {
            log.error("配置文件没有配置SPACE_NAME,退出程序");
            System.out.println("配置文件没有配置SPACE_NAME,退出程序");
            System.exit(1);
        }

        String sql = useSpaceSql();

        //执行SQL
        this.execute(session, sql);
    }

    /**
     * 组装创建SQL
     *
     * @return
     */
    private String useSpaceSql() {
        StringBuilder builder = new StringBuilder();
        builder.append(useSpace());
        return builder.toString();
    }

}