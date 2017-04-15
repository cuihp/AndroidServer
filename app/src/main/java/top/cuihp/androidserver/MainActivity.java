package top.cuihp.androidserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.mina.core.session.IoSession;

import top.cuihp.serverlibrary.client.ClientConfig;
import top.cuihp.serverlibrary.client.MinaClient;
import top.cuihp.serverlibrary.server.MinaServer;
import top.cuihp.serverlibrary.server.ServerConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private Button button, button2;
    private TextView text;
    private MinaServer minaServer;
    private MinaClient minaClient;
    private IoSession serverSeesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        text = (TextView) findViewById(R.id.text);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);


        initServer();


        initClient();


    }

    private void initServer() {
        //服务器初始化
        ServerConfig serverConfig = new ServerConfig.Builder().setPort(8888).build();
        minaServer = new MinaServer(serverConfig);
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
                text.setText(message.toString());
                Log.d(TAG, "server messageReceived ");
            }

            @Override
            public void messageSent(String message) {
                Log.d(TAG, "server messageSent "+message);

            }
        });
    }

    private void initClient() {
        //客户端初始化
        ClientConfig clientConfig = new ClientConfig.Builder().setIp("127.0.0.1").setPort(8888).build();
        minaClient = new MinaClient(clientConfig);
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
                text.setText(message.toString());
            }

            @Override
            public void messageSent(String message) {
                Log.d(TAG, "client messageSent "+message);

            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                minaClient.sendMessage("hello server "+System.currentTimeMillis());
                break;
            case R.id.button2:
                minaServer.sendMessage("hello client "+System.currentTimeMillis());
                break;
        }
    }


}
