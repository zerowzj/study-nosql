# 1. 安装

## 1.1 编译

1. 进入src目录，执行下面命令

   ```shell
   make PREFIX=/server/redis install
   ```

2. 编译后生成以下命令和配置

   - 命令

     1. redis-server（服务端）
  2. redis-cli（客户端）
     3. redis-check-dump（修复dump.rdb文件）
     4. redis-check-aof（修复AOF文件）
     5. redis-benchmark（用于redis性能测试）
     6. redis-sentinel（集群管理）
   - 配置
     1. redis.conf
     2. sentinel.conf

## 1.1 启停

1. 启动

   ```shell
   redis-server --port
   
   redis-cli -h [host] -p port -a [pass]
   ```

2. 停止

   ```shell
   kill pid
   
   redis-cli shutdown
   ```

## 1.1 注册服务

1. 修改redis.conf文件

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

## 1.1 一机多redis

1. 不需要编译多次，也不需要把编译好的文件夹复制多份
2. 复制多个redis配置文件6379.conf、6380.conf
3. 修改端口、dir及pidfile
4. 复制多个/etc/rc.d/init.d/下脚本redis_6379、redis_6380
5. 修改端口、pidfile

# 2. 配置

## 2.1 日志

### 2.1.1 服务器日志

1. 修改redis.conf，日志输出文件

   ```shell
   #输出目录
   logfile "/server/redis/logs/redis_6379.log"
   
   #debug: 会打印出很多信息，适用于开发和测试阶段
   #verbose: 冗长的，包含很多不太有用的信息，但比debug要清爽一些
   #notice: 适用于生产模式
   #warning: 警告信息
   loglevel notice
   ```

   

