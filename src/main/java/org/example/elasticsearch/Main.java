package org.example.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;

import java.util.Date;
import java.util.Map;

/**
 * Created by heval on 11/12/15.
 */
public class Main {
    /**
     * @param args
     * All methods are not used in ElasticSearch
     */
    public static void main(String[] args) {
        ElasticSearch elasticSearch = new ElasticSearch();
        Node node = new NodeBuilder().clusterName("customer")
        .settings(Settings.builder()
        .put("path.home", "/usr/share/elasticsearch")).node();
        Client client = node.client();

        elasticSearch.addDocument(client, "site", "user", "1", 
        elasticSearch.putJsonDocument("Heval Berk", "Nevruz", "123456", new Date()));

        Map<String, Object> map = elasticSearch.getDocument(client, "site", "user", "1");
        System.out.println(map);

        SearchHit[] searchHits = elasticSearch.searchDocument(client, "name", "Heval");

        for (SearchHit s : searchHits) {
            System.out.println(s.getSource());
        }
    }
}
