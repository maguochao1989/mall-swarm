## nacos-net
```bash
docker network create nacos-net
docker network connect nacos-net mysql5.7-01
```

## nacos
```bash
docker restart nacos-01 && docker rm -f nacos-01 && docker run -d \
--name nacos-01 \
--network nacos-net \
-p 8848:8848 \
-p 9848:9848 \
-p 9849:9849 \
--privileged=true \
--restart=always \
-e JAVA_OPTS="-Xms1g -Xmx1g" \
-e MODE=standalone \
-v /home/mark/work/java/mall-swarm/mall-db/nacos/conf:/home/nacos/conf \
-v /home/mark/work/java/mall-swarm/mall-db/nacos/logs:/home/nacos/logs \
-v /home/mark/work/java/mall-swarm/mall-db/nacos/data:/home/nacos/data \
docker.1ms.run/nacos/nacos-server:v2.5.1

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);
INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
```


## redis
```bash
docker run  -d \
--name redis-01 \
--privileged=true \
--restart=always \
-p 6379:6379 \
-v /home/mark/work/java/mall-swarm/mall-db/redis/data:/data \
-v /home/mark/work/java/mall-swarm/mall-db/redis/conf/redis.conf:/etc/redis/redis.conf \
docker.1ms.run/library/redis:6.2.16
```


## sentinel
```bash
docker run -d \
--name sentinel-01 \
--network nacos-net \
--privileged=true \
--restart=always \
-p 8858:8858 \
docker.1ms.run/bladex/sentinel-dashboard:1.8.8
```


## seata
```bash
docker rm -f seata-01 && docker run -d \
--name seata-01 \
-p 8091:8091 \
-p 7091:7091 \
-e SEATA_IP=127.0.0.1 \
--network nacos-net \
--privileged=true \
--restart=always \
-v /home/mark/work/java/mall-swarm/mall-db/seata/conf/application.yml:/seata-server/resources/application.yml  \
docker.1ms.run/seataio/seata-server:1.8.0.2

docker cp seata-01:/seata-server/resources /home/mark/work/java/mall-swarm/mall-db/seata/
sh nacos-config.sh -h 127.0.0.1 -p 8848 -g SEATA_GROUP -t b7c78ae1-c52f-4046-a549-6d1ddaaf0773
```

## skywalking
```bash
docker run -d \
--name oap-01 \
--cpu-period=50000 --cpu-quota=5000 \
--network nacos-net \
--privileged=true \
--restart=always \
-p 11800:11800 \
-p 12800:12800 \
-e TZ=Asia/Shanghai \
-v /etc/localtime:/etc/localtime:ro \
docker.1ms.run/apache/skywalking-oap-server:9.7.0



docker rm -f oap-ui-01 && \
docker run -d \
--name oap-ui-01 \
--network nacos-net \
--privileged=true \
--restart=always \
-p 13800:8080 \
-e SW_OAP_ADDRESS=http://oap-01:12800 \
-e TZ=Asia/Shanghai \
-v /etc/localtime:/etc/localtime:ro \
docker.1ms.run/apache/skywalking-ui:9.7.0 

/home/mark/work/java/mall-swarm/mall-db/skywalking/skywalking-agent/skywalking-agent.jar
java -javaagent:/home/mark/work/java/mall-swarm/mall-db/skywalking/skywalking-agent/skywalking-agent.jar=agent.service_name=mall-admin    -Dskywalking.collector.backend_service=127.0.0.1:11800  -Dskywalking.agent.service_name=mall-admin  -Dskywalking.logging.file_name=mall-admin-api.log -jar target/mall-admin-1.0-SNAPSHOT.jar
```


## zookeeper
docker run -d \
--name zoo-01 \
--network nacos-net \
--privileged=true \
--restart=always \
-p 2181:2181 \
-v /home/mark/work/java/mall-swarm/mall-db/zookeeper/data:/data \
-v /home/mark/work/java/mall-swarm/mall-db/zookeeper/conf/zoo.cfg:/conf/zoo.cfg \
-v /home/mark/work/java/mall-swarm/mall-db/zookeeper/datalog:/datalog \
docker.1ms.run/zookeeper:3.6.4-temurin



## elastic-job-lite-console
docker run -d \
--name=ejlc-01 \
--network nacos-net \
--privileged=true \
--restart=always \
-p 8899:8899 \
docker.1ms.run/shine10076/elastic-job-lite-console:2.1.5


docker run -d \
--name zkui-01 \
--network nacos-net \
--privileged=true \
--restart=always \
-p 9090:9090 \
-e ZKUI_ZK_SERVER=zoo-01:2181 \
docker.1ms.run/fansys/zkui:latest
