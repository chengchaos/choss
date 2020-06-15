
## What is CHOSS

CHOSS is the Choss HBase Object Storage Service



### Development Env configuration:

**HDFS**


hdfs-site.xml


```xml
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>

        <property>
            <name>dfs.namenode.name.dir</name>
            <value>file:///works/data/hadoop/dfs/name</value>
        </property>

        <property>
            <name>dfs.datanode.data.dir</name>
            <value>file:///works/data/hadoop/dfs/data</value>
        </property>
    </configuration>
```

core-site.xml

```xml
    <configuration>

        <property>
            <name>hadoop.tmp.dir</name>
            <value>file:///works/data/hadoop</value>
        </property>

        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://0.0.0.0:9000</value>
        </property>
    </configuration>
```


```bash
$ bin/hdfs namenode -format
$ sbin/start-dfs.sh 
```


**HBase**



先复制 hadoop 的两个 xml 文件到 HBase 的 conf 目录


```bash
$ cp /works/hadoop/etc/hadoop/core-site.xml .
$ cp /works/hadoop/etc/hadoop/hdfs-site.xml .
```

编辑 hbase-env.sh

```
export JAVA_HOME=/usr/java/default
# Tell HBase whether it should manage it's own instance of Zookeeper or not.
# export HBASE_MANAGES_ZK=true


```


编辑 hbase-site.xml 


```xml
    <configuration>

        <property>
            <name>hbase.rootdir</name>
            <value>hdfs://localhost:9000/hbase</value>
        </property>

        <property>
            <name>hbase.zookeeper.property.dataDir</name>
            <value>/works/data/hadoop/zookeeper</value>
        </property>

        <property>
            <name>hbase.cluster.distributed</name>
            <value>true</value>
        </property>
		
    </configuration>

```

启动 HBase

```bash
$ bin/start-hbase.sh
$ hbase shell
2020-02-16 13:46:22,608 WARN  [main] util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/works/hbase-1.2.11/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/works/hadoop-2.6.0-cdh5.7.0/share/hadoop/common/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
HBase Shell; enter 'help<RETURN>' for list of supported commands.
Type "exit<RETURN>" to leave the HBase Shell
Version 1.2.11, rca53d58f5b7abde0c189c9f78baf4246bddffac3, Fri Feb 15 18:12:16 CST 2019

hbase(main):001:0> status
1 active master, 0 backup masters, 1 servers, 0 dead, 1.0000 average load



```


## DB

user_info

| field | type | description | 
| ---- | ---- | ---- |
| user_id | bigint| primary key |
| user_name | varchar(50) | user name union |
| password | varch4(255) | password  |
| detail_info | varchar(255) | description |
| create_time | timestamp | as the title |


token_info

| field | type | description | 
| ---- | ---- | ---- |
| token | varchar(32) | primary key |
| expire_itme | int(11) | expire time, default 7 day |
| refresh_time | timestamp | .. |
| active | tinyint | .. |
| creator | bigint | user_id |
| create_time | timestamp | create time |

auth:

| field | type | description | 
| ---- | ---- | ---- |
| bucket_name | varchar(32) | bucket name |
| target_token | varchar(32) | ... |
| auth_time | timestamp | auth time |


bucket_info :

| field | type | description | 
| ---- | ---- | ---- |
| bucket_id | varchar(32) | primary key |
| bucket_name | varchar(32) | bukket name |
| detail | varchar(255) | description |
| creator | varchar(32) | creator name |
| create_time | timestamp | create time |

### 

```sql

CREATE DATABASE `choss` /*!40100 COLLATE 'utf8mb4_general_ci' */

use `choss`;

CREATE TABLE `user_info` (
	`user_id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_name` VARCHAR(50) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`detail` VARCHAR(255) NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`user_id`),
	UNIQUE INDEX `user_name` (`user_name`)
)
COMMENT='user_info'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `token_info` (
	`token` VARCHAR(50) NOT NULL,
	`is_active` INT NOT NULL,
	`expire_time` BIGINT NOT NULL,
	`user_id` BIGINT NOT NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`refresh_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`token`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `bucket_info` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NOT NULL,
	`bucket_name` VARCHAR(50) NOT NULL,
	`detail` VARCHAR(50) NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)
COMMENT='bucket_info'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `auth_info` (
	`bucket_id` BIGINT NOT NULL,
	`token` VARCHAR(50) NOT NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`bucket_id`, `token`)
)
COMMENT='auth_info'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

```


- 对某各时间段进行快速查询检索
- 快速找到某一份想要的文件，并且其他人不能看到。（快速随机读写/权限管理）


## 模块分类

- 用户管理及权限管理
- 文件管理
- 接口和 SDK


## 用户及权限管理

### 用户管理

- 添加用户
- 获取用户信息
- 修改用户信息
- 删除用户

### 权限管理

- 添加 Token
- 刷新 Token
- 更新 Token 信息
- Token 授权
- 取消 Token 授权
- 删除 Token

```

[用户]

  [ 用户 ]         [RESTful API / SDK]      [权限管理]     [文件管理]
   |  --用户数据检索-->    |  --是否拥有权限 --> |
   |                      | <-- 返回信息 ------ |
   |                      | -- 查询数据 ------------------>  |
   |                      | <-- 返回数据  -----------------  |
   |  <-- 返回请求结果     |

   
```

## 文件管理

### Bucket 管理

每个 Bucket 都有自己的目录表和文件表


### 目录管理

- 创建目录
- 删除目录
- 目录检索

### 文件管理

- 上传文件
- 下载文件
- 删除文件
- 查看文件信息
- 过滤文件
  - 前缀过滤
  - 起止文件名过滤
  - 类型过滤
- 文件预览


#### 第一种表设计（关系型数据库）

id 主键
name 目录名称（唯一索引）
parent_id 父目录 id, 跟目录的父目录 id 为 0
is_file true 为文件,false 为目录
bucked_id 所属的 bucket 

关系型数据库不能支持很多的文件和目录.


#### 第二种设计 (使用 HBase)

首先套用关系型数据库的设计:

RowKey : 
object_id

Column : 
cf:name
cf:parentId
cf:isFile
cf:bucketId

> 无法做到随机读取.

##### 设计思路

- 目录和文件分开存储比较合理
- RowKey 以文件路径比较合适，但是需要考虑热点问题和检索效率。
- 还要考虑下载文件/过滤文件等功能



**目录表结构：**

RowKey:
/dir1

Column: Qualifier
sub:dir2=1
sub:dir3=1
scf:creator=z3
cf:squid=0001


RowKey:
/dir1/dir2

Column: Qualifier
sub:dir4=1
cf:creator=z3
cf:seqid=0002

RowKey:
/dir1/dir2/dir4

Column: Qualifier
sub:dir5=1
cf:creator=z3



特点： RowKey 是文件的全路径名

有两个列族： sub 和 cf 

sub 中包含当前目录的子目录
cf 为当前目录的其他信息。

查找： 找到一个目录， 可以通过这个目录的 sub 列族找到它的全部子目录
创建： 创建一个目录，需要同时在他的父目录中插入一条 sub 列族的信息。
删除：


**文件表结构**

RowKey:

dir_seqid_file_name ( 0001_file1)

Column: Qualifier

c:content=bytes
cf:creator=zs
cf:size
cf:type


RowKey 为文件所在目录的 squid + 下划线 + 文件名

包含两个列族 c 列族和 cf 列族

c 列族为文件内容
cf 列族为文件新详细信息。

