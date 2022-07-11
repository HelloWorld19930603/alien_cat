package com.aliencat.springboot.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;

import java.io.IOException;
import java.util.Iterator;

public class SolrTest {

    static String baseSolrUrl = "http://localhost:8983/solr/new_core";

    public static void main(String[] args) {
        for(int i = 1;i<10;i++) {
            String key = Long.toString(System.nanoTime()+i);
            update(key,"新增数据-"+key);
        }
        query();
        delete();
    }

    public static void query() {

        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl).build();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setSort("id", SolrQuery.ORDER.desc);
        solrQuery.setQuery("content:solr");
        QueryResponse response;
        try {
            response = client.query(solrQuery);
            System.out.println(response.getResults());
            //响应头
            NamedList<Object> namedList = response.getHeader();
            Iterator iterator = namedList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
            SolrDocumentList documentList = response.getResults();
            System.out.println("总计数据行数" + documentList.getNumFound());
            for (SolrDocument solrDocument : documentList) {

                System.out.print("id = " + solrDocument.get("id"));
                System.out.println("; content = " + solrDocument.get("content"));
            }
            //
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete() {
        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl).build();
        try {
            client.deleteById("001");
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                client.rollback();
            } catch (SolrServerException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        try {
            client.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //包括新增和更新。 主键一致-更新。主键不存在-新增
    public static void update(String id,String content) {
        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl).build();
        //新增或更新。新增文档类型都是SolrInputDocument
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", id);
        doc.addField("content", content);
        try {
            UpdateResponse response = client.add(doc);
            System.out.println(String.format("status = %s ; QTime = %s", response.getStatus(), response.getQTime()));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.rollback();
            } catch (SolrServerException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        //在Solr服务中，数据的写操作也是有事务的，WEB管理平台默认一次操作一次提交。
        try {
            client.commit();
            client.close();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
