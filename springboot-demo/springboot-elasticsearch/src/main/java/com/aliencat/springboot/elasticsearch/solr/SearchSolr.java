package com.aliencat.springboot.elasticsearch.solr;

import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.apache.solr.common.util.NamedList;
import org.noggit.JSONUtil;

import java.io.IOException;
import java.util.*;

/**
 * @Author chengcheng
 * @Date 2022-07-01
 **/
public class SearchSolr {

    //static String baseSolrUrl = "http://60.190.244.226:22481/solr/";
    static String baseSolrUrl = "http://solr2.aim:8080/solr/";

    public static Map<String,SolrDocument> jointestMap = new HashMap<>(1024 * 16);

    public static void main(String[] args) {

        //queryJointestByCursor();
        System.out.println(jointestMap.size());
        query("search4contact");
        //queryJointest("(737340389 695126338 1387257514 1373813858 1477372891 833279028 918655908 664131718 850582516 733527669 712876958 815102799 815385940 676597967 708211694 863056900 833279028 815102799 733527669 664131718 833574646)");
    }

    public static void query(String index) {

        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl + index).build();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setSort("contact_id", SolrQuery.ORDER.asc);
        solrQuery.setQuery("*:*");
        solrQuery.setRows(10000);
        solrQuery.setQuery("contact_id:[0 TO *]");
        solrQuery.setFields("unique_account_id,unique_account,contact_type,contact_account_id,contact_account,id,contact_id,");
        //solrQuery.setFilterQueries(" {!join fromIndex=search4jointest toIndex=search4contact from=account_number  to=contact_account}");
        QueryResponse response;
        try {
            response = client.query(solrQuery);
            //响应头
            NamedList<Object> namedList = response.getHeader();
            Iterator iterator = namedList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
            SolrDocumentList documentList = response.getResults();
            System.out.println("总计数据行数" + documentList.getNumFound());
            System.out.println("当前行数：" + documentList.size());
            Set<String> set = new HashSet<>();
            for (SolrDocument solrDocument : documentList) {
                System.out.println(JSONUtil.toJSON(solrDocument));

                if(set.size() > 1000){
                    break;
                }
            }

            //
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void queryJointest(String number) {
        long start = System.currentTimeMillis();
        String index = "search4jointest";
        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl + index).build();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setRows(10000);
        solrQuery.setSort("account_number", SolrQuery.ORDER.asc);
        solrQuery.setQuery(String.format("account_number:[%s TO *]",number));
        //solrQuery.setQuery("account_number:"+account_number);
        //solrQuery.setFilterQueries(" {!join fromIndex=search4jointest toIndex=search4contact from=account_number  to=contact_account}");
        QueryResponse response;
        try {
            response = client.query(solrQuery);
/*            //响应头
            NamedList<Object> namedList = response.getHeader();
            Iterator iterator = namedList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }*/
            SolrDocumentList documentList = response.getResults();
            documentList.forEach(entries -> {
                jointestMap.put(entries.get("account_number").toString(),entries);
            });
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            long end = System.currentTimeMillis();
            System.out.println(jointestMap.size() + " - " + (end - start) / 1000);
        }
    }

    /**
     * 分页获取所有数据
     */
    public static void queryJointestByCursor() {
        if(!jointestMap.isEmpty()){
            return;
        }
        long start = System.currentTimeMillis();
        String index = "search4jointest";
        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl + index).build();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setRows(200000);
        solrQuery.setSort("id", SolrQuery.ORDER.asc);
        //solrQuery.setQuery("account_number:"+account_number);
        //solrQuery.setFilterQueries(" {!join fromIndex=search4jointest toIndex=search4contact from=account_number  to=contact_account}");
        QueryResponse response;
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        try {
            boolean done = false;
            while (!done) {
                solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);//变化游标条件
                response = client.query(solrQuery);//执行多次查询读取
                String nextCursorMark = response.getNextCursorMark();//获取下次游标
                //如果两次游标一样，说明数据拉取完毕，可以结束循环了
                if (cursorMark.equals(nextCursorMark)) {
                    done = true;
                }
                cursorMark = nextCursorMark;
                SolrDocumentList documentList = response.getResults();
                documentList.forEach(entries -> {
                    jointestMap.put(entries.get("account_number").toString(),entries);
                });
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            long end = System.currentTimeMillis();
            System.out.println("search4jointest处理完毕: " + jointestMap.size() + " - " + (end - start) / 1000);
        }
    }

    private static HttpSolrClient message_client;
    private static HttpSolrClient contact_client;

    static {
        message_client = new HttpSolrClient.Builder(baseSolrUrl + IndexConstant.SEARCH4MESSAGE).build();
        contact_client = new HttpSolrClient.Builder(baseSolrUrl + IndexConstant.SEARCH4CONTACT).build();
    }
    public static HttpSolrClient getMessageClient(){

        return message_client;
    }

    public static HttpSolrClient getContactClient(){

        return contact_client;
    }
    /**
     * 获取联系人
     * @param cursorMark
     * @return
     */
    public static QueryResponse queryByCursor(HttpSolrClient client, String cursorMark) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setRows(200000);
        solrQuery.setSort("id", SolrQuery.ORDER.desc);
        //solrQuery.setQuery("account_number:"+account_number);
        //solrQuery.setFilterQueries(" {!join fromIndex=search4jointest toIndex=search4contact from=account_number  to=contact_account}");
        QueryResponse response = null;
        //String cursorMark = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        try {
            solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);//变化游标条件
            response = client.query(solrQuery);//执行多次查询读取
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生异常，2s后重试，当前游标：" + cursorMark);
        }
        return response;
    }

    public static QueryResponse queryByCursor2(HttpSolrClient client, String cursorMark) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setRows(200000);
        solrQuery.setSort("id", SolrQuery.ORDER.asc);
        //solrQuery.setQuery("account_number:"+account_number);
        //solrQuery.setFilterQueries(" {!join fromIndex=search4jointest toIndex=search4contact from=account_number  to=contact_account}");
        QueryResponse response = null;
        //String cursorMark = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        try {
            solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);//变化游标条件
            response = client.query(solrQuery);//执行多次查询读取
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生异常，当前游标：" + cursorMark);
        }
        return response;
    }

    /**
     * 获取消息
     * @param cursorMark
     * @return
     */
    public static QueryResponse queryMessageByCursor(String cursorMark) {
        String index = "search4message";
        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl + index).build();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        solrQuery.setRows(10000);
        solrQuery.setSort("id", SolrQuery.ORDER.asc);
        //solrQuery.setQuery("account_number:"+account_number);
        //solrQuery.setFilterQueries(" {!join fromIndex=search4jointest toIndex=search4contact from=account_number  to=contact_account}");
        QueryResponse response = null;
        //String cursorMark = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        try {
            solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);//变化游标条件
            response = client.query(solrQuery);//执行多次查询读取
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Long queryJointest() {
        long start = System.currentTimeMillis();
        String index = "search4jointest";
        HttpSolrClient client = new HttpSolrClient.Builder(baseSolrUrl + index).build();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        QueryResponse response;
        try {
            response = client.query(solrQuery);
            return response.getResults().getNumFound();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("search4jointest处理完毕,耗时：" + (System.currentTimeMillis() - start) / 1000);
        }
        return 0L;
    }
}
