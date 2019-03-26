package com.github.common.components.es;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ES工具类
 */
@Slf4j
public class EsAccessor {

    @Getter
    @Setter
    private int esSearchSizeLimit;

    private TransportClient transportClient;

    public EsAccessor(String esServer) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        String[] urlAndCluster = esServer.split("/");
        log.info("esServer is :{}", esServer);
        if (urlAndCluster.length == 2) {
            this.transportClient = createClient(urlAndCluster[0], urlAndCluster[1]);
        } else {
            throw new IllegalArgumentException("illegal argument:" + esServer);
        }
    }

    public EsAccessor(String esServer, String clusterName) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        this.transportClient = createClient(esServer, clusterName);
    }

    public EsAccessor(TransportClient client) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        this.transportClient = client;
    }


    /**
     * 从apollo 加载client配置
     *
     * @return
     */
    private TransportClient createClient(String esCluster, String clusterName) {
        // 集群设置
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        TransportClient client = new PreBuiltTransportClient(settings);

        //添加集群地址和tcp服务端口 IP是由集群的各个node的ip组成的数组
        String[] esClusters = esCluster.split(",");

        for (String ipAndPort : esClusters) {
            String[] ipAndPorts = null;
            try {
                ipAndPorts = ipAndPort.split(":");
                if (ipAndPorts.length == 1) {
                    throw new IllegalArgumentException("please provide es server url and port.");
                }
                int port = Integer.valueOf(ipAndPorts[1]);
                client.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(ipAndPorts[0]), port));
            } catch (UnknownHostException e) {
                log.error("fail to load es config", e);
            }
        }

        return client;
    }

    /**
     * 新增文档
     *
     * @param index  索引
     * @param type   类型
     * @param id     主键
     * @param source 原始内容
     */
    public IndexResponse insert(String index, String type, String id, String source) {
        XContentType xContentType = XContentFactory.xContentType(source);
        return transportClient.prepareIndex(index, type, id)
                .setSource(source, xContentType).get();
    }

    /**
     * 更新文档
     *
     * @param index  索引
     * @param type   类型
     * @param id     主键
     * @param source 原始内容
     */
    public UpdateResponse update(String index, String type, String id, String source) {
        XContentType xContentType = XContentFactory.xContentType(source);
        return transportClient.prepareUpdate(index, type, id)
                .setDoc(source, xContentType).get();
    }

    /**
     * 插入或更新文档
     * 1.不存在就插入
     * 2.存在就修改
     *
     * @param index  索引
     * @param type   类型
     * @param id     主键
     * @param source 原始内容
     */
    public UpdateResponse upsert(String index, String type, String id, String source) {
        XContentType xContentType = XContentFactory.xContentType(source);
        return transportClient.prepareUpdate(index, type, id)
                .setDoc(source, xContentType)
                .setUpsert(source, xContentType)
                .get();
    }


    /**
     * 删除文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    主键
     */
    public DeleteResponse delete(String index, String type, String id) {
        return transportClient.prepareDelete(index, type, id).get();
    }

    /**
     * 批量插入或更新文档
     *
     * @param index       索引
     * @param type        类型
     * @param idSourceMap 主键和原始内容 组成的MAP
     */
    public BulkResponse bulkUpsert(String index, String type, Map<String, String> idSourceMap) {
        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        for (Map.Entry<String, String> idSource : idSourceMap.entrySet()) {
            XContentType xContentType = XContentFactory.xContentType(idSource.getValue());
            IndexRequest indexRequest = new IndexRequest(index, type, idSource.getKey())
                    .source(idSource.getValue(), xContentType);
            UpdateRequest updateRequest = new UpdateRequest(index, type, idSource.getKey())
                    .doc(idSource.getValue(), xContentType)
                    .upsert(indexRequest);
            bulkRequestBuilder.add(updateRequest);
        }
        return bulkRequestBuilder.get();
    }

    /**
     * 根据主键查找文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    主键
     */
    public GetResponse getById(String index, String type, String id) {
        return transportClient.prepareGet(index, type, id).get();
    }

    /**
     * 根据主键查找文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    主键
     * @param clazz 泛型转换类
     */
    public <T> T getById(String index, String type, String id, Class<T> clazz) {
        GetResponse getResponse = getById(index, type, id);
        if (getResponse.isExists()) {
            return JSON.parseObject(getResponse.getSourceAsString(), clazz);
        }
        return null;
    }

    /**
     * BulkRequestBuilder
     * 搜索
     *
     * @param index            索引
     * @param type             类型
     * @param boolQueryBuilder 查询条件
     * @param size             个数
     */
    public SearchHits search(String index, String type, BoolQueryBuilder boolQueryBuilder, int size, List<SortBuilder> sortBuilderList) {
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(index).setTypes(type)
                .setQuery(boolQueryBuilder)
                .setSize(size);
        if (sortBuilderList != null && !sortBuilderList.isEmpty()) {
            for (SortBuilder sortBuilder : sortBuilderList) {
                searchRequestBuilder.addSort(sortBuilder);
            }
        }
        SearchResponse searchResponse = searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH)
                .setExplain(false).get();
        return searchResponse.getHits();
    }

    /**
     * 搜索
     *
     * @param index            索引
     * @param type             类型
     * @param boolQueryBuilder 查询条件
     * @param clazz            泛型转换类
     * @param size             查询个数
     * @param sortBuilderList  排序规则
     */
    public <T> List<T> search(String index, String type, BoolQueryBuilder boolQueryBuilder, Class<T> clazz, int size, List<SortBuilder> sortBuilderList) {
        SearchHits searchHits = search(index, type, boolQueryBuilder, size, sortBuilderList);
        return searchHits2List(searchHits, clazz);
    }

    /**
     * 搜索
     * CanalAdaptResponse response
     * @param index            索引
     * @param type             类型
     * @param boolQueryBuilder 查询条件
     * @param clazz            泛型转换类
     * @param sortBuilderList  排序规则
     */
    public <T> List<T> search(String index, String type, BoolQueryBuilder boolQueryBuilder, Class<T> clazz, List<SortBuilder> sortBuilderList) {
        return search(index, type, boolQueryBuilder, clazz, esSearchSizeLimit, sortBuilderList);
    }

    /**
     * 搜索
     *
     * @param index            索引
     * @param type             类型
     * @param boolQueryBuilder 查询条件
     * @param clazz            泛型转换类
     */
    public <T> List<T> search(String index, String type, BoolQueryBuilder boolQueryBuilder, Class<T> clazz) {
        return search(index, type, boolQueryBuilder, clazz, esSearchSizeLimit, null);
    }

    /**
     * 将SearchHits转换成List
     *
     * @param searchHits es搜索集合
     */
    public <T> List<T> searchHits2List(SearchHits searchHits, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (searchHits != null && searchHits.totalHits > 0) {
            for (SearchHit searchHit : searchHits) {
                result.add(JSON.parseObject(searchHit.getSourceAsString(), clazz));
            }
        }
        return result;
    }

    /**
     * 搜索并分组
     *
     * @param index              索引
     * @param type               类型
     * @param boolQueryBuilder   查询条件
     * @param aggregationBuilder 分组条件
     */
    public SearchResponse search(String index, String type, BoolQueryBuilder boolQueryBuilder, AggregationBuilder aggregationBuilder) {
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(index).setTypes(type);
        searchRequestBuilder.setQuery(boolQueryBuilder)
                .addAggregation(aggregationBuilder);
        return searchRequestBuilder.execute().actionGet();
    }

}
