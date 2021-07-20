package com.map;

import java.util.*;

/**
 * 用继承HashMap的方式实现一个带有过期时间的Map
 * 可以用来实现本地服务端的项目用户锁
 * <p>
 * 原理：
 *
 * @param <K>
 * @param <V>
 */
public class ExpiryMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 1L;

    /**
     * 默认失效时间为2分钟
     */
    private Long defaultExpiryTime = 1000 * 60 * 2L;

    //记录当前存入Map中key的到期时间，当前时间大于过期时间既当前Map中key已过期
    private Map<K, Long> expiryTimeMap = new HashMap<K, Long>();

    /**
     * 默认构造方法创建为HashMap
     */
    public ExpiryMap() {
        super();
    }

    /**
     * 默认map大小为2的4次方，和自定义过期时间
     *
     * @param defaultExpiryTime
     */
    public ExpiryMap(long defaultExpiryTime) {
        this(1 << 4, defaultExpiryTime);
    }

    /**
     * 带有map初始化大小和过期时间的构造
     *
     * @param initialCapacity
     * @param defaultExpiryTime
     */
    public ExpiryMap(int initialCapacity, long defaultExpiryTime) {
        super(initialCapacity);
        this.defaultExpiryTime = defaultExpiryTime;
    }

    /**
     * sec 过期时间 毫秒
     *
     * @param k
     * @param v
     * @param ms
     * @return
     */
    public V put(K k, V v, int ms) {
        long currentTime = System.currentTimeMillis();
        expiryTimeMap.put(k, currentTime + ms);
        return super.put(k, v);
    }

    @Override
    public V put(K k, V v) {
        expiryTimeMap.put(k, System.currentTimeMillis() + defaultExpiryTime);
        return super.put(k, v);
    }

    public boolean containKey(Object key) {
        return !checkExpiry(key, true) && super.containsKey(key);
    }

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return entrySet().size() == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            return Boolean.FALSE;
        }
        Set<Entry<K, V>> set = super.entrySet();
        Iterator<Entry<K, V>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (value.equals(entry.getValue())) {
                if (checkExpiry(entry.getKey(), false)) {
                    iterator.remove();
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 验证key是否过期
     *
     * @param key
     * @param isRemoveSuper
     * @return
     */
    private boolean checkExpiry(Object key, boolean isRemoveSuper) {
        if (!expiryTimeMap.containsKey(key)) {
            return false;
        }
        Long aLong = expiryTimeMap.get(key);//key过期时间
        boolean flag = System.currentTimeMillis() > aLong;
        if (flag) {
            if (isRemoveSuper) {
                super.remove(key);
                expiryTimeMap.remove(key);
            }
        }
        return flag;
    }

    @Override
    public Collection<V> values() {

        Collection<V> values = super.values();

        if
        (values == null || values.size() < 1) {
            return values;
        }

        Iterator<V> iterator = values.iterator();

        while (iterator.hasNext()) {
            V next = iterator.next();
            if (!containsValue(next)) {
                iterator.remove();
            }
        }
        return values;
    }

    @Override
    public V get(Object key) {
        if (key == null) {
            return null;
        }
        if (checkExpiry(key, true)) {
            return null;
        }
        return super.get(key);
    }

    /**
     * @param key
     * @return null:不存在或key为null -1:过期  存在且没过期返回value 因为过期的不是实时删除，所以稍微有点作用
     * @Description: 是否过期
     */
    public Object isInvalid(Object key) {
        if (key == null) {
            return null;
        }
        if (!expiryTimeMap.containsKey(key)) {
            return null;
        }
        long expiryTime = expiryTimeMap.get(key);

        boolean flag = System.currentTimeMillis() > expiryTime;

        if (flag) {
            super.remove(key);
            expiryTimeMap.remove(key);
            return -1;
        }
        return super.get(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            expiryTimeMap.put(e.getKey(), System.currentTimeMillis() + defaultExpiryTime);
        }
        super.putAll(m);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = super.entrySet();
        Iterator<Entry<K, V>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (checkExpiry(entry.getKey(), false)) {
                iterator.remove();
            }
        }

        return set;
    }

}
