# spring-integration-project
Educational Spring-project.  
Contains different examples from [spring-integration guide](https://docs.spring.io/spring-integration/reference/htmlsingle/).  
## Build
* Mac OS ```./gradlew build```
* Windows ```gradlew build ```
## Run
* Mac OS ```./gradlew bootRun```
* Windows ```gradlew bootRun ```  
## Active MQ
1. Go to [ActiveMQ admin console](http://localhost:8161/).  
2. Click at **Manage ActiveMQ broker**.  
3. Click **Send** tab:  
  3.1. Choose **Destination** = ```ip.queue```  
  3.2. **Message body** = ${ipAddress} (like '208.80.152.201')   
  3.3. Click 'Send' button    
4. Click **Queues** tab and find message with your geolocation by ip address in ```location.queue```  
