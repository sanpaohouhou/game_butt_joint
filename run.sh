mvn clean install -DskipTests

ps -ef |grep java |grep ROOT  |grep -v 'grep'|awk '{print $2}'  | xargs kill -9

export spring_datasource_url='jdbc:mysql://development.ceezehox1xag.ap-northeast-1.rds.amazonaws.com:3306/baccarat?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai'
export spring_datasource_username='admin'
export spring_datasource_password='tianli123456TL'
export spring_redis_host='development.hej4hw.ng.0001.apne1.cache.amazonaws.com'
export spring_redis_database='9'


nohup java -jar target/ROOT.jar &
