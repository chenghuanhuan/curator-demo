/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.bj58.emc.study.curator.demo.watcher;

import com.bj58.emc.study.curator.demo.utils.CuratorUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @author chenghuanhuan@qccr.com
 * @since $Revision:1.0.0, $Date: 2016年07月18日 下午7:22 $
 */
public class ZKCuratorWatcher implements CuratorWatcher {

    private CuratorFramework client;

    public ZKCuratorWatcher(CuratorFramework client){
        this.client = client;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        String path = event.getPath();
        Watcher.Event.KeeperState state = event.getState();
        Watcher.Event.EventType eventType = event.getType();
        System.out.println("==============="+path+"=========watch=======");
        System.out.println("==============="+state+"=========state=======");
        System.out.println("==============="+eventType+"=========eventType=======");
        GetDataBuilder dataBuilder = client.getData();
        System.out.println("---------------"+new String(dataBuilder.forPath(path))+"--------------");
        CuratorUtil.addWatch(path, true, new ZKCuratorWatcher(client), client);
    }
}
