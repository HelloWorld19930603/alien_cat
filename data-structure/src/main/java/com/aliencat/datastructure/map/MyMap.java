package com.aliencat.datastructure.map;

public interface MyMap<K, V> {

    /**
     * PUT接口
     *
     * @param k
     * @param v
     */
    void put(K k, V v);

    /**
     * GET接口
     *
     * @param k
     * @return
     */
    V get(K k);

    /**
     * 获取map大小接口
     *
     * @return
     */
    int size();

    /**
     * Entry 接口
     *
     * @param <K>
     * @param <V>
     */
    interface Entry<K, V> {
        /**
         * 获取KEY值
         *
         * @return
         */
        K getKey();

        /**
         * 获取Value值
         *
         * @return
         */
        V getValue();
    }

}
