/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.bj58.emc.study.curator.demo.utils;

import com.bj58.emc.study.curator.demo.watcher.ZkPathChildrenCacheListener;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * zk工具
 * @author chenghuanhuan@qccr.com
 * @since $Revision:1.0.0, $Date: 2016年07月18日 下午5:45 $
 */
public class CuratorUtil {

    private static Logger logger = LoggerFactory.getLogger(CuratorUtil.class);

    private final static ExecutorService pool = Executors.newFixedThreadPool(10);

    private CuratorUtil(){

    }


    /**
     * 删除节点
     *
     * @param nodeName
     */
    public static void deleteNode(String nodeName, CuratorFramework client) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(nodeName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
/*

    *//**
     * 删除节点(带事务)
     *
     * @param nodeName
     *//*
    public static CuratorOp deleteNode(String nodeName,  TransactionOp transaction) throws Exception {
           CuratorOp curatorOp =  transaction.delete().forPath(nodeName);
            return curatorOp;
    }*/

    /**
     * 找到指定节点下所有子节点的名称与值
     *
     * @param node
     * @return
     */
    public static Map<String, String> listChildrenDetail(String node,CuratorFramework client) {
        Map<String, String> map = Maps.newHashMap();
        try {
            GetChildrenBuilder childrenBuilder = client.getChildren();
            List<String> children = childrenBuilder.forPath(node);
            GetDataBuilder dataBuilder = client.getData();
            if (children != null) {
                for (String child : children) {
                    String propPath = ZKPaths.makePath(node, child);
                    map.put(child, new String(dataBuilder.forPath(propPath), Charsets.UTF_8));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 找到指定节点下所有子节点的名称与值,包含子节点的子节点
     *
     * @param node
     * @return
     */
    public static Map<String, String> listChildrenDetailAll(String node,CuratorFramework client) {
        Map<String, String> map = Maps.newHashMap();
        try {
            GetChildrenBuilder childrenBuilder = client.getChildren();
            List<String> children = childrenBuilder.forPath(node);
            GetDataBuilder dataBuilder = client.getData();
            if (children != null) {
                for (String child : children) {
                    String propPath = ZKPaths.makePath(node, child);
                    map.put(propPath, new String(dataBuilder.forPath(propPath), Charsets.UTF_8));
                    map.putAll(listChildrenDetailAll(propPath,client));

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 列出子节点的名称
     *
     * @param node
     * @return
     */
    public static List<String> listChildren(String node,CuratorFramework client) {
        List<String> children = Lists.newArrayList();
        try {
            GetChildrenBuilder childrenBuilder = client.getChildren();
            children = childrenBuilder.forPath(node);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }

    /**
     * 列出子节点的名称
     *
     * @param node
     * @return
     */
    public static List<String> listChildrenAll(String node,CuratorFramework client) {
        List<String> children = Lists.newArrayList();
        List<String> childrenList = Lists.newArrayList();
        try {
            GetChildrenBuilder childrenBuilder = client.getChildren();
            children = childrenBuilder.forPath(node);
            if (children  != null && children.size()>0){
                for (String child:children){
                    String propPath = ZKPaths.makePath(node, child);
                    childrenList.add(propPath);
                    childrenList.addAll(listChildrenAll(propPath,client));
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return childrenList;
    }


    /**
     * 增加监听
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @throws Exception
     */
    public static void addWatch(String node, boolean isSelf,CuratorFramework client) throws Exception {
        if (isSelf) {
            client.getData().watched().forPath(node);
        }
        else {
            client.getChildren().watched().forPath(node);
        }
    }


    /**
     * 增加监听
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @param watcher
     * @throws Exception
     */
    public static void addWatch(String node, boolean isSelf, Watcher watcher, CuratorFramework client) throws Exception {
        if (isSelf) {
            client.getData().usingWatcher(watcher).forPath(node);
        }
        else {
            client.getChildren().usingWatcher(watcher).forPath(node);
        }
    }


    /**
     * 增加监听
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @param watcher
     * @throws Exception
     */
    public static void addWatch(String node, boolean isSelf, CuratorWatcher watcher, CuratorFramework client) throws Exception {
        if (isSelf) {
            client.getData().usingWatcher(watcher).forPath(node);
        }
        else {
            client.getChildren().usingWatcher(watcher).forPath(node);
        }
    }

    /**
     * 添加或者更新节点
     * @param path
     * @param value
     * @param transaction
     * @return
     * @throws Exception
     */
/*
    public static CuratorOp createNode(String path, String value, TransactionOp transaction,CuratorFramework client) throws Exception {
        CuratorOp curatorOp = null;
        // 存在则更新
        */
/*if (value == null) {
            value = "";
        }*//*

        if(client.checkExists().forPath(path) != null){
            if (value == null) {
                curatorOp =  transaction.setData().forPath(path,"".getBytes());
            }else {
                curatorOp = transaction.setData().forPath(path, value.getBytes());
            }

        }else {
            // 不存在则创建
            if(value == null){
                curatorOp = transaction.create().forPath(path,"".getBytes());
            }else {
                curatorOp = transaction.create().forPath(path, value.getBytes());
            }
        }

        return curatorOp;
    }
*/


    /**
     * 给节点添加事件
     * @param client
     * @param path
     * @param cacheData
     */
    public static void addPathChildrenCacheListener(CuratorFramework client,String path,boolean cacheData){
        PathChildrenCache childrenCache = new PathChildrenCache(client,path,cacheData);
        try {
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            childrenCache.getListenable().addListener(new ZkPathChildrenCacheListener(), pool);
        } catch (Exception e) {
            CloseableUtils.closeQuietly(childrenCache);
            logger.error("addPathChildrenCacheListener error! path={}",path,e);
        }

    }
}
