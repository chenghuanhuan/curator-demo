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

/**
 * @author chenghuanhuan@qccr.com
 * @since $Revision:1.0.0, $Date: 2016年07月19日 下午2:43 $
 */
public class WatchExample {
    public static void main(String[] args) {
        CuratorFramework client = ClientFactory.newClient();
        try {
            client.start();
            List<String> nodes =  CuratorUtil.listChildrenAll("/superconfig",client);
            for (String node:nodes){
                CuratorUtil.addWatch(node, true, new ZKCuratorWatcher(client), client);
            }



            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
