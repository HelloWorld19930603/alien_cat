package com.pattern.memento.demo01;


/**
 * ������
 * ���ڴ������ݺͻָ�����
 *
 */
public class Originator {

    /**
     * id
     * �������ڱ��ݵ���Ϣ
     */
    private Integer id;

    /**
     * �汾
     * ������Ҫ���б��ݵ���Ϣ
     */
    private String version;

    /**
     * ��������
     *  ��Ҫע�����
     *  ������Ҫ�����ݶ��󷵻����ڽ��������߽��й���
     * @return
     */
    public Memento createMemento(){
        return new Memento(this.version);
    }


    /**
     * ���ݱ��ݻָ�������Ϣ
     * @param memento
     */
    public void recoverMemento(Memento memento){
        this.version=memento.getVersion();
    }

    /**
     * �����������չʾ
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
