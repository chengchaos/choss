

Java 类的说明

| Java 类 | 对应的数据模型 |
| ---- | ---- |
| HBaseConfiguration | HBase 配置类 |
| HBaseAdmin | HBase 管理 Admin 类 |
| Table | HBase Table 操作类 |
| Put | HBase 添加操作数据模型 |
| Get | HBase 单个查询操作数据模型 |
| Scan | HBase Scan 检索操作数据模型 |
| Result | HBase 查询的结果模型 |
| ResultScanner | HBase 检索结果模型哦 |


## 引入 jar 依赖


```xml




<dependency> 
    <groupId>org.apache.hbase</groupId>
    <artifactId>hbase-client</artifactId>
    <version>1.2.11</version>
</dependency>

```