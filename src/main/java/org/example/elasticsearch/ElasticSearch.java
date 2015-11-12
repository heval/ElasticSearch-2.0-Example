package org.example.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by heval on 11/12/15.
 */
public class ElasticSearch {

    public SearchHit[] searchDocument(Client client, String field, Object value) {
        SearchResponse response = client.prepareSearch("site") //varargs
                .setTypes("user") //varargs
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.matchQuery(field, value))
                .execute()
                .actionGet();
        return response.getHits().getHits();
    }

    public Map<String, Object> putJsonDocument(String name, String surname, String password, Date date) {
        Map<String, Object> jsonDocument = new HashMap<String, Object>();

        jsonDocument.put("name", name);
        jsonDocument.put("surname", surname);
        jsonDocument.put("password", password);
        jsonDocument.put("date", date);

        return jsonDocument;
    }

    public void addDocument(Client client, String index, String type, String id, Map<String, Object> map) {
        client.prepareIndex(index, type, id).setSource(map).get();
    }

    public Map<String, Object> getDocument(Client client, String index, String type, String id) {
        GetResponse response = client.prepareGet(index, type, id).get();
        return response.getSource();
    }

    public SearchHit[] getAllDocument(Client client) {
        SearchResponse response = client.prepareSearch().execute().actionGet();
        return response.getHits().getHits();
    }

    public void updateDocument(Client client, String index, String type, String id, String field, String value) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index);
        updateRequest.type(type);
        updateRequest.id(id);
        try {
            updateRequest.doc(jsonBuilder()
                    .startObject()
                    .field(field, value)
                    .endObject());
            client.update(updateRequest).get();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void deleteDocument(Client client, String index, String type, String id) {
        client.prepareDelete(index, type, id).get();
    }
}
