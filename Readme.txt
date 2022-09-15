This project is a simple set of applications that can communicate with kafka broker.
The applications are containerised and can started using docker-compose up command. That will also spin up kafka and zookeper instances required.
ApplicationA is written in node and both produced and consumes messages from kafka
ApplicationB is written in java and both produced and consumes messages from kafka
ApplicationAUI is a frontend angular application and is used to trigger ApplicationA to send message to kafka. 
That message is consumed by ApplicationB and it sends a message back to kafka. 
Message from ApplicationB is consumed by ApplicationA.
Currently to observe messages getting back to ApplicationA you should opserve its containers logs
 