package com.aliencat.springboot.elasticsearch.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengcheng
 * @Date 2022-07-11
 **/
public class IndexConstant {

    public static final String SEARCH4MESSAGE = "search4message";
    public static final String SEARCH4CONTACT = "search4contact";

    public static final String TG_CONTACT = "tg_contact";


    public static void main(String[] args) throws UnsupportedEncodingException {
        //System.out.println(SEARCH4MESSAGE_MAPPING);
        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println(URLEncoder.encode("浙江省", "utf-8"));
        System.out.println(URLDecoder.decode("%E6%B5%99%E6%B1%9F%E7%9C%81","UTF-8"));
        String s = "%2525E6%2525B5%252599%2525E6%2525B1%25259F%2525E7%25259C%252581";
        System.out.println(URLDecoder.decode(s,"UTF-8"));
        System.out.println(URLDecoder.decode("fgf13gf","UTF-8"));


        List<String> stringList = new ArrayList<>();
        stringList.stream().forEach(s1 -> {
            System.out.println(s);
        });
    }

    public static final String SEARCH4MESSAGE_MAPPING = "{\n" +
            "    \"properties\" : {\n" +
            "        \"business_action_id\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"contact_account\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"gmt_create\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"id\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"is_group\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"m_l_charCount\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"m_l_informationType\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"m_l_senderType\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"message_content\" : {\n" +
            "          \"type\" : \"text\",\n" +
            "          \"analyzer\": \"ik_max_word\",\n" +
            "          \"fields\" : {\n" +
            "            \"keyword\" : {\n" +
            "              \"type\" : \"keyword\",\n" +
            "              \"ignore_above\" : 256\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"message_time\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"message_type\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +

            "        \"recipient_account\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +

            "        \"sender_account\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +

            "        \"account_number\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"application_id\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"city\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"country\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"province\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        }\n" +
            "      }\n" +
            "  }" +
            "    }";

    public static final String SEARCH4CONTACT_MAPPING =  "{\n" +
            "      \"properties\" : {\n" +
            "        \"business_action_id\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"contact_account\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"contact_id\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"contact_type\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"ex_k_age\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_bioInfo\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_groupType\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_icon\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_lastSeenTime\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_memberNum\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_name\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_nickName\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_offlineTime\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_phone\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_remark\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_sex\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_tmp\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_updateTime\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"ex_k_userName\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"gmt_create\" : {\n" +
            "          \"type\" : \"integer\"\n" +
            "        },\n" +
            "        \"id\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"unique_account\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"account_number\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"application_id\" : {\n" +
            "          \"type\" : \"long\"\n" +
            "        },\n" +
            "        \"city\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"country\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        },\n" +
            "        \"province\" : {\n" +
            "          \"type\" : \"keyword\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";


}
