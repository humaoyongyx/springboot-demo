package issac.study.mbp.test;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestZk {
    //zookeeper地址。集群时使用逗号分隔
    private static final String zkServers = "192.168.6.129:2181";
    //创建会话，超时时间设置长一点否则可能会连接不上
    static ZkClient zkClient = new ZkClient(zkServers, 5000);
    static Map<String, Boolean> registers = new HashMap<>();

    static String rootPath = "/zkTest";

    public static void main(String[] args) {

        // 如果不设置为报错：
        //org.I0Itec.zkclient.exception.ZkMarshallingError: java.io.EOFException
        //可以使用默认的 SerializableSerializer，但是会有乱码
        zkClient.setZkSerializer(new MyZkSerializer());

        if (!zkClient.exists(rootPath)) {
            //创建持久节点
            zkClient.createPersistent(rootPath);
        }

        //列出根下所有节点
        List<String> list = zkClient.getChildren(rootPath);

        for (String node : list) {
            String childNode = rootPath + "/" + node;
            String data = zkClient.readData(childNode);
            System.out.println(childNode + "==>" + data);
            handChange(rootPath, node);

        }


        //节点变化了之后，会回调函数
        zkClient.subscribeChildChanges(rootPath, new IZkChildListener() {
            /**
             *
             * @param root rootPath
             * @param list 它下面的节点列表
             * @throws Exception
             */
            @Override
            public void handleChildChange(String root, List<String> list) throws Exception {

                for (String node : list) {
                    handChange(root, node);
                }
            }


        });

        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static void handChange(String root, String node) {
        String childNode = root + "/" + node;
        // 这里有一个问题，如果上面没有注册监听，那么这里只有节点新增了之后，才能监听到已有节点的变化
        // 而且zk，允许同一个节点被多个listener监听
        if (registers.get(childNode) != null) {
            return;
        } else {
            registers.put(childNode, true);
        }
        zkClient.subscribeDataChanges(childNode, new IZkDataListener() {
            /**
             *
             * @param dataPath 全路径
             * @param data  变更后的数据
             * @throws Exception
             */
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(dataPath + "---changed" + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath + "---deleted");
                //取消订阅
                zkClient.unsubscribeDataChanges(childNode, this);
                registers.remove(childNode);
            }
        });
    }
    public static void close() {
        //关闭客户端连接
        if (zkClient != null) {
            zkClient.close();
        }
    }


}
