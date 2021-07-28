sudo docker run -d \
--name rabbitmq \
--hostname myRabbitMQ_server  \
-p 15672:15672 \
-p 5672:5672 \
-e RABBITMQ_DEFAULT_USER=root \
-e RABBITMQ_DEFAULT_PASS=admin \
-v ~/workspace/rbq/data:/var/lib/rabbitmq \
rabbitmq:management
