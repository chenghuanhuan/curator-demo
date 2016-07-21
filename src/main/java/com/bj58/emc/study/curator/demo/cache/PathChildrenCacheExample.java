/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.bj58.emc.study.curator.demo.cache;

import com.bj58.emc.study.curator.demo.utils.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author chenghuanhuan@qccr.com
 * @since $Revision:1.0.0, $Date: 2016年07月14日 下午8:47 $
 */
public class PathChildrenCacheExample {
    private static CuratorFramework client = ClientFactory.newClient();

    public static void main(String[] args) {
        String path = "/pathcache";
        try {
            client.start();
            PathChildrenCache cache = new PathChildrenCache(client,path,true);
            cache.getListenable().addListener(new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    System.out.println("-----event------"+pathChildrenCacheEvent.getData());
                }
            });
            cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            System.out.println(cache.getCurrentData());
            client.create().creatingParentsIfNeeded().forPath(path,path.getBytes());
            //client.setData().forPath(path,"pathcache".getBytes());
            client.setData().forPath(path, "sdsd".getBytes());
            for (ChildData data : cache.getCurrentData()) {
                System.out.println(data.getPath() + " = " + new String(data.getData()));
            }

            // 让main程序一直监听控制台输入，不退出
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
