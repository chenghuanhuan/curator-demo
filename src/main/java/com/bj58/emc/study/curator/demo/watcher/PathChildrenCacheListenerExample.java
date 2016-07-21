/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.bj58.emc.study.curator.demo.watcher;

import com.bj58.emc.study.curator.demo.utils.ClientFactory;
import com.bj58.emc.study.curator.demo.utils.CuratorUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenghuanhuan@qccr.com
 * @since $Revision:1.0.0, $Date: 2016年07月18日 下午7:41 $
 */
public class PathChildrenCacheListenerExample {

    public static void main(String[] args){
        CuratorFramework client = ClientFactory.newClient();
        try {
            client.start();

            //CuratorUtil.addWatch("/superconfig", true, new ZKCuratorWatcher(), client);
            /**
             * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
             */
            ExecutorService pool = Executors.newFixedThreadPool(2);
            List<String> nodes =  CuratorUtil.listChildrenAll("/superconfig",client);
            for (String node:nodes){
                CuratorUtil.addPathChildrenCacheListener(client,node,true);
            }

            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }

    }
}
