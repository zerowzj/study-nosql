# 一. 安装

## 1.1 编译

1. 进入 redis5.0.8/src 目录，执行下面命令

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

3. 配置位于目录 redis5.0.8 下

   - redis.conf
   - sentinel.conf

## 1.2 启停

### 1.2.1 手动启停

1. 启动

   ```shell
   #服务端，配置文件必须为第一参数
   ./redis-server redis.conf --port PORT
   #客户端
   ./redis-cli -h [HOST] -p PORT -a [PASS]
   ```

2. 停止

   ```shell
   #
   kill pid
   #
   ./redis-cli shutdown
   ```

### 1.2.2 注册服务

1. 编辑redis.conf文件

   ```shell
   #是否作为守护进程运行，默认no，不开启
   daemonize yes
   port 6379
   #守护进程是默认pid写入文件
   pidfile /var/run/redis_6379.pid
   #rdb文件存放目录
   dir /usr/server/redis5.0.8/data
   #
   dbfilename dump_6379.rdb
   ```

2. 复制启动脚本 /utils/redis_init_script 到 /etc/rc.d/init.d，并重命名redis

3. 修改启动脚本，内容添加

   ```shell
   #服务器监听的端口
   REDISPORT=6379
   #服务端所处位置
   EXEC=/usr/server/redis5.0.8/bin/redis-server
   #客户端位置
   CLIEXEC=/usr/server/redis5.0.8/bin/redis-cli
   #PID文件位置
   PIDFILE=/var/run/redis_${REDISPORT}.pid
   #配置文件位置
   CONF=/usr/server/redis5.0.8/conf/${REDISPORT}.conf
   ```

4. 启停服务

   ```shell
   #
   service redis start
   /etc/init.d/redis start
   
   #
   service redis stop
   /etc/init.d/redis stop
   ```

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

# 二. 配置（redis.cfg）

## 2.1 基本

```shell
#是否以守护进程运行
daemonize no
#pid文件
pidfile /var/run/redis_6379.pid
#日志级别
loglevel notice
#日志文件
logfile "/server/redis/logs/redis_6379.log"
#数据库数量
databases 16
#
always-show-logo yes

#
```



## 2.2 网络

```shell
#
bind 127.0.0.1
#
protected-mode no
#
port 6379
#TCP接收队列长度，受/proc/sys/net/core/somaxconn和tcp_max_syn_backlog这两个内核参数的影响
tcp-backlog 511
#客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
timeout 0
#非零，则设置SO_KEEPALIVE选项来向空闲连接客户端发送ACK，用来定时向client发送tcp_ack包来探测client是否存活
tcp-keepalive 300
```

## 2.3 内存

```shell
#最大内存
maxmemory 1024
#淘汰策略
maxmemory-policy
#
maxmemory-samples
#
replica-ignore-maxmemory yes
```

## 2.4 慢查询

1. ```shell
   #等于0会记录所有命令；小于0对于任何命令都不会进行记录
   slowlog-log-slower-than 0
   
   #慢查询日志最多存储多少条
   slowlog-max-len 1000
   ```

2. 获取慢查询

   ```shell
   #
   slowlog get [n]
   #
   slowlog len
   #
   slowlog reset
   ```


# 三. 持久化

## 3.1 RDB快照

​		RDB 是 Redis 默认的持久化方案。在指定的时间间隔内，执行指定次数的写操作，则会将内存中的数据写入到磁盘中。即在指定目录下生成一个dump.rdb文件。

​		Redis 重启会通过加载dump.rdb文件恢复数据

1. 配置

   RDB 触发机制分为使用指令手动触发和 redis.conf 配置自动触发。

   - 手动触发，执行以下命令

     - save指令：该指令会阻塞当前 Redis 服务器，执行 save 指令期间，Redis 不能处理其他命令，直到 RDB 过程完成为止。
     - bgsave：执行该命令时，Redis 会在后台异步执行快照操作，此时 Redis 仍然可以相应客户端请求。

   - 自动触发，编辑redis.cfg，修改以下配置

     ```shell
     #### rdb开启 ###
     #900秒内，1次写入，则产生快照
     save 900 1
     #300秒内，1000次写入，则产生快照
     save 300 1000
     #60秒内，10000次写入，则产生快照
     save 60 10000
     
     ### rdb关闭 ###
     save ""
     
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

   - 注意

     shutdown和flushall命令都会触发RDB快照，这是一个坑，请大家注意。还有其他命令：

     - keys * 匹配数据库中所有 key
     - save 阻塞触发RDB快照，使其备份数据
     - flushall清空整个 Redis 服务器的数据（几乎不用）
     - shutdown关机走人（很少用）

2. 恢复

​        将dump.rdb 文件拷贝到redis的安装目录的bin目录下，重启redis服务即可。在实际开发中，一般会考虑到物理机硬盘损坏情况，选择备份dump.rdb 。

## 3.2 AOF追加

1.  配置

   - 编辑redis.cfg，修改以下配置

     ```shell
     ### aof开关 ###
     appendonly no
     #文件名
     appendfilename "appendonly.aof"
     
     ### aof配置 ###
     #同步策略
     appendfsync always
     #正在导出rdb快照的过程中，是否停止同步aof
     no-appendfsync-on-rewrite yes
     
     ### 重写配置 ###
     #aof文件大小比起上次重写时的大小，增长率100%时，重写
     auto-aof-rewrite-percentage 100
     #aof文件，至少超过64M时，重写
     auto-aof-rewrite-min-size 64mb
     ```

   - appendfsync配置

     - always：每个命令都同步到AOF；安全，速度慢
     - everysec：每秒写1次；折衷方案
     - no：写入工作交给操作系统，由操作系统判断缓冲区大小，统一写入到AOF； 同步频率低，速度快

2. 恢复

​		正常情况下，将appendonly.aof 文件拷贝到redis的安装目录的bin目录下，重启redis服务即可。但在实际开发中，可能因为某些原因导致appendonly.aof 文件格式异常，从而导致数据还原失败，可以通过命令redis-check-aof --fix appendonly.aof 进行修复



## 3.3 总结

1. Redis 默认开启RDB持久化方式，在指定的时间间隔内，执行指定次数的写操作，则将内存中的数据写入到磁盘中。
2. RDB 持久化适合大规模的数据恢复但它的数据一致性和完整性较差。
3. Redis 需要手动开启AOF持久化方式，默认是每秒将写操作日志追加到AOF文件中。
4. AOF 的数据完整性比RDB高，但记录内容多了，会影响数据恢复的效率。
5. Redis 针对 AOF文件大的问题，提供重写的瘦身机制。
6. 若只打算用Redis 做缓存，可以关闭持久化。
7. 若打算使用Redis 的持久化。建议RDB和AOF都开启。其实RDB更适合做数据的备份，留一后手。AOF出问题了，还有RDB。

# 四. 主从部署

## 4.1 配置

```

```





# 1. 命令

## 1.1 

## 1.2

## 1.3

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