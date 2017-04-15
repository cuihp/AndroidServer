package top.cuihp.serverlibrary.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cuihp on 2017/4/15.
 */

public class MinaServer {
    private final static String MESSAGE = "message";
    private static final String TAG=MinaServer.class.getSimpleName();
    private ServerConfig mConfig;
    private ServerStateListener serverState;
    private ConnectThread mConnectThread;
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(1);
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    serverState.sessionCreated();
                    break;
                case 2:
                    serverState.sessionOpened();
                    break;
                case 3:
                    serverState.sessionClosed();
                    break;
                case 4:
                    serverState.messageReceived(msg.getData().getString(MESSAGE));
                    break;
                case 5:
                    serverState.messageSent(msg.getData().getString(MESSAGE));
                    break;

            }
        }
    };

    public MinaServer(ServerConfig mConfig) {
        this.mConfig = mConfig;
        mConnectThread = new ConnectThread();
        mThreadPool.execute(mConnectThread);
    }

    public void setServerStateListener(ServerStateListener serverState) {
        this.serverState = serverState;
    }
    /**
     * 发送消息
     * @param data
     */
    public void sendMessage(String data) {
        mConnectThread.sendMsg(data);
    }

    private class ConnectThread implements Runnable{
        IoAcceptor acceptor;
        IoSession mSession;
        @Override
        public void run() {
             acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
            acceptor.setHandler(new MinaServerHandler());
            acceptor.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
            //10秒无操作释放Session
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            try {
                acceptor.bind(new InetSocketAddress(mConfig.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG,acceptor.getLocalAddress().toString());
        }

        private class MinaServerHandler extends IoHandlerAdapter {
            @Override
            public void sessionCreated(IoSession session) throws Exception {
                Message message = new Message();
                message.arg1 = 1;
                mHandler.sendMessage(message);
            }

            @Override
            public void sessionOpened(IoSession session) throws Exception {
                mSession=session;
                Message message = new Message();
                message.arg1 = 2;
                mHandler.sendMessage(message);
            }

            @Override
            public void sessionClosed(IoSession session) throws Exception {
                Message message = new Message();
                message.arg1 = 3;
                mHandler.sendMessage(message);

            }

            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                mSession=session;
                Message msg = new Message();
                msg.arg1 = 4;
                Bundle bundle = new Bundle();
                bundle.putString(MESSAGE, message.toString());
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

            @Override
            public void messageSent(IoSession session, Object message) throws Exception {
                Message msg = new Message();
                msg.arg1 = 5;
                Bundle bundle = new Bundle();
                bundle.putString(MESSAGE, message.toString());
                msg.setData(bundle);
                mHandler.sendMessage(msg);

            }
        }
        /**
         * 发送消息
         *
         * @param data
         */
        public void sendMsg(String data) {
            if (mSession != null && mSession.isConnected()) {
                mSession.write(data);
            }
        }
        /**
         * 断开连接
         */
        public void disConnect() {
            acceptor.unbind();
        }
    }
    /**
     * 断开连接
     */
    public void disConnect() {
        mConnectThread.disConnect();
    }


    public interface ServerStateListener {
        void sessionCreated();

        void sessionOpened();

        void sessionClosed();

        void messageReceived(String message);

        void messageSent(String message);
    }
}
