# AndroidServer
  1. 基于Apache MINA 开源框架

  2. 实现Android OS 作为服务器、客户端，进行Socket通讯

  3. 结合[HotWiFi项目](https://github.com/cuihp/HotWiFi)，手机架设热点进行通讯

# How to


* Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
          ...
	  maven { url 'https://jitpack.io' }
	}
}
```
* Step 2. Add the dependency
```
dependencies {
	 compile 'com.github.cuihp:AndroidServer:v1.0.0'
}
```

* Step 3.初始化服务器端

```
          //建造者模式进行相关配置
          ServerConfig serverConfig = new ServerConfig.Builder().setPort(8888).build();
          MinaServer minaServer = new MinaServer(serverConfig);
          //服务器状态进行监听
          minaServer.setServerStateListener(new MinaServer.ServerStateListener() {
                 @Override
                 public void sessionCreated() {
                     Log.d(TAG, "server sessionCreated ");
                 }

                 @Override
                 public void sessionOpened() {
                     Log.d(TAG, "server sessionOpened ");
                 }

                 @Override
                 public void sessionClosed() {
                     Log.d(TAG, "server sessionClosed ");
                 }

                 @Override
                 public void messageReceived(String message) {
                     Log.d(TAG, "server messageReceived ");
                 }

                 @Override
                 public void messageSent(String message) {
                     Log.d(TAG, "server messageSent "+message);

                 }
             });
         }

```

* Step 4.初始化客户端

```
     //建造者模式进行相关配置
     ClientConfig clientConfig = new ClientConfig.Builder().setIp("127.0.0.1").setPort(8888).build();
     MinaClient  minaClient = new MinaClient(clientConfig);
     //状态进行监听
     minaClient.setClientStateListener(new MinaClient.ClientStateListener() {
            @Override
            public void sessionCreated() {
                Log.d(TAG, "client sessionCreated ");
            }

            @Override
            public void sessionOpened() {
                Log.d(TAG, "client sessionOpened ");
            }

            @Override
            public void sessionClosed() {
                Log.d(TAG, "client sessionClosed ");
            }

            @Override
            public void messageReceived(String message) {
                Log.d(TAG, "client messageReceived "+message.toString());
            }

            @Override
            public void messageSent(String message) {
                Log.d(TAG, "client messageSent "+message);

            }
     });

```

* Step 5.消息发送

```
 minaClient.sendMessage("hello server "+System.currentTimeMillis());

 minaServer.sendMessage("hello client "+System.currentTimeMillis());
 ```
