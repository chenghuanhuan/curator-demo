/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.bj58.emc.study.curator.demo;

import com.bj58.emc.study.curator.demo.utils.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;

/**
 * @author chenghuanhuan@qccr.com
 * @since $Revision:1.0.0, $Date: 2016年07月14日 下午8:12 $
 */
public class ApiTest {
    private static CuratorFramework client = ClientFactory.newClient();
    private static final String PATH = "/crud";

    public static void main(String[] args) {
        try {
            client.start();
            List<String> data = client.getChildren().forPath("/czx");
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
