
## Java 操作 HBase 常见类

**HBaseConfiguration**

HBase 配置类，用于设置基本属性，比如  zookeeper 地址，超时时间等。


**HBaseAdmin**

HBase 管理 Admin 类，可以通过这个实例进行表的创建和删除等操作。


**Table**

HBase Table 操作类

**Put**

HBase 添加操作数据模型


Get

HBase 单个查询操作数据模型


Scan

HBase Scan 检索操作数据模型


Result

HBase 查询的结构哦类型

ResultScanner

HBase 检索结果模型



### 依赖

```xml
org.apache.hbase
hbase-client
1.2.4
```