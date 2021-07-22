package com.pattern.memento.demo01;


/**
 * 第三步
 * 用于创建备份和恢复备份
 *
 */
public class Originator {

    /**
     * id
     * 代表不用于备份的信息
     */
    private Integer id;

    /**
     * 版本
     * 代表需要进行备份的信息
     */
    private String version;

    /**
     * 创建备份
     *  需要注意的是
     *  这里需要将备份对象返回用于交给管理者进行管理
     * @return
     */
    public Memento createMemento(){
        return new Memento(this.version);
    }


    /**
     * 根据备份恢复对象信息
     * @param memento
     */
    public void recoverMemento(Memento memento){
        this.version=memento.getVersion();
    }

    /**
     * 便于输出进行展示
     * @return
     */
    public String toString(){
        String str="{id:"+this.id+",version:"+this.version+"}";
        return str;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
