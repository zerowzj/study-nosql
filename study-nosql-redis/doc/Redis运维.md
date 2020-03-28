# 1. 安装

## 1.1 编译

1. 进入/redis5.0.8/src目录，执行下面命令

   ```shell
   make PREFIX=/usr/server/redis5.0.8 install
   ```

2. 编译后指定目录生成以下命令

   - redis-server（服务端）
   - redis-cli（客户端）
   - redis-check-dump（修复dump.rdb文件）
   - redis-check-aof（修复AOF文件）
   - redis-benchmark（用于redis性能测试）
   - redis-sentinel（集群管理）

3. 配置位于目录/redis5.0.8下

   - redis.conf
   - sentinel.conf

## 1.2 启停

1. 启动

   ```shell
   #服务端
   ./redis-server redis.conf --port
   #客户端
   ./redis-cli -h [host] -p port -a [pass]
   ```

2. 停止

   ```shell
   #
   kill pid
   #
   redis-cli shutdown
   ```

## 1.3 注册服务

1. 编辑redis.conf文件

   - daemonize yes（是否作为守护进程运行，默认no，不开启）
   - port 端口号
   - pidfile /var/run/redis_端口号.pid（守护进程是默认pid写入文件）
   - dir /server/redis/data/端口号

2. 复制启动脚本redis_init_script到/etc/rc.d/init.d

3. 修改启动脚本，内容添加

   ```shell
   REDISPORT=6379
   EXEC=/server/redis/bin/redis-server
   CLIEXEC=/server/redis/bin/redis-cli
   PIDFILE=/var/run/redis_${REDISPORT}.pid
   CONF=/server/redis/conf/${REDISPORT}.conf
   ```

4. 启停服务

   - service redis start 或 /etc/init.d/redis start
   - service redis stop 或 /etc/init.d/redis start

5. 开机启动

   - 修改启动脚本，头部添加以下内容

     ```shell
     #chkconfig: 2345 90 10
     #description: Redis is a persistent kv database
     ```

   - 设置开机启动：chkconfig redis on

   - 关闭开机启动：chkconfig redis off

## 1.4 一机多redis

1. 不需要编译多次，也不需要把编译好的文件夹复制多份
2. 复制多个redis配置文件6379.conf、6380.conf
3. 修改端口、dir及pidfile
4. 复制多个/etc/rc.d/init.d/下脚本redis_6379、redis_6380
5. 修改端口、pidfile

# 2. 配置

## 2.1 基本

```shell
#端口
port 6379
#守护进程
daemonize no
#是否开启保护
protected-mode no
#pid文件
pidfile /var/run/redis_6379.pid
#数据库数量
databases 16
```



## 2.1 网络

```shell
#客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
timeout 0
#TCP接收队列长度，受/proc/sys/net/core/somaxconn和tcp_max_syn_backlog这两个内核参数的影响
tcp-backlog 511
#如果非零，则设置SO_KEEPALIVE选项来向空闲连接客户端发送ACK，用来定时向client发送tcp_ack包来探测client是否存活的
tcp-keepalive 300
```



## 2.1 日志

1. 服务器日志

   - 编辑redis.conf，日志输出文件

     ```shell
     #输出目录
     logfile "/server/redis/logs/redis_6379.log"
     
     #日志级别
     #debug: 会打印出很多信息，适用于开发和测试阶段
     #verbose: 冗长的，包含很多不太有用的信息，但比debug要清爽一些
     #notice: 适用于生产模式
     #warning: 警告信息
     loglevel notice
     ```

2. 慢查询日志

   - 编辑redis.conf，设置阈值，单位为微秒

     ```shell
     #等于0会记录所有命令；小于0对于任何命令都不会进行记录
     slowlog-log-slower-than 0
     
     #慢查询日志最多存储多少条
     slowlog-max-len 1000
     ```

   - 获取慢查询

     ```shell
     #
     slowlog get [n]
     #
     slowlog len
     #
     slowlog reset
     ```

# 3. 持久化

## 3.1 RDB快照

1. 配置

   ```shell
   #### rdb开启 ###
   #900秒内，1次写入，则产生快照
   save 900 1
   #300秒内，1000次写入，则产生快照
   save 300 1000
   #60秒内，10000次写入，则产生快照
   save 60 10000
   
   ### rdb配置 ###
   #后台备份进程出错时，主进程是否停止写入
   stop-writes-on-bgsave-error yes
   #导出的rdb文件是否压缩
   rdbcompression yes
   #导入rdb恢复数据时，是否验证rdb的完整性
   rdbchecksum yes
   #rdb文件名
   dbfilename dump.rdb
   #rdb文件存放路径
   dir ./
   ```

2. 关闭RDB

   ```shell
   save ""
   ```

## 3.2 AOF追加

1. 配置

   ```shell
   ### aof开关 ###
   appendonly no
   
   ### aof配置 ###
   #每1个命令,都立即同步到AOF，安全,速度慢
   appendfsync always
   #正在导出rdb快照的过程中，是否停止同步aof
   no-appendfsync-on-rewrite yes
   #aof文件大小比起上次重写时的大小，增长率100%时，重写
   auto-aof-rewrite-percentage 100
   #aof文件，至少超过64M时，重写
   auto-aof-rewrite-min-size 64mb
   ```

   - appendfsync配置
     - always：每个命令都同步到AOF；安全，速度慢
     - everysec：每秒写1次；折衷方案
     - no：写入工作交给操作系统，由操作系统判断缓冲区大小，统一写入到AOF； 同步频率低，速度快



# 1. 主从部署

# 1. 监控

## 1.1 命令监控

### 1.1.1 监控模式

​		使用redis-cli连接成功后，输入monitor，即可进入到 redis 监控模式。

### 1.1.2 client命令

​		使用redis-cle连接成功后，输入client list

获取当前连接到redis server端的所有客户端以及相关状态

### 1.1.3 info命令

​		使用redis-cle连接成功后，执行以下命令：

```shell
info [SECTION]
```

1. server：获取 server 信息，包括 version, OS, port 等信息
2. clients：获取 clients 信息，如客户端连接数等
3. memory：获取 server 的内存信息，包括当前内存消耗、内存使用峰值
4. persistence：获取 server 的持久化配置信息
5. stats：获取 server 的一些基本统计信息，如处理过的连接数量等
6. replication：获取 server 的主从配置信息
7. cpu：获取 server 的 CPU 使用信息
8. keyspace：获取 server 中各个 DB 的 key 的数量
9. cluster：获取集群节点信息，仅在开启集群后可见
10. commandstas：获取每种命令的统计信息

## 1.2 工具监控

### 1.2.1 redis-stat

​		基于Redis的info命令封装而成。通常来说，不会像基于MONITOR命令的监控工具一样，对Redis本身有性能上的影响

### 1.2.2 RedisLive

​		采用python开发的redis的可视化及查询分析工具

### 1.2.3 redis_exporter

​		配合Prometheus以及Grafana的Prometheus Redis插件，可以在Grafana进行可视化及监控

