/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.bj58.emc.study.curator.demo.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * @author chenghuanhuan@qccr.com
 * @since $Revision:1.0.0, $Date: 2016年07月19日 上午10:36 $
 */
public class ZkPathChildrenCacheListener implements PathChildrenCacheListener {
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
        switch (event.getType()) {
            case CHILD_ADDED:
                System.out.println("CHILD_ADDED: " + event.getData().getPath());
                break;
            case CHILD_REMOVED:
                System.out.println("CHILD_REMOVED: " + event.getData().getPath());
                break;
            case CHILD_UPDATED:
                System.out.println("CHILD_UPDATED: " + event.getData().getPath());
                System.out.println("DATA:"+new String(event.getData().getData()));
                break;
            default:
                break;
        }
    }
}
