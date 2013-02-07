package hello;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/*
 * The server accepts connections and replies
 */
public class HelloServer implements Runnable {

    private ServerSocketChannel _serverSocketChannel;
    private Selector _selector;

    public HelloServer(int port) throws IOException {
        _selector = Selector.open();
        _serverSocketChannel = ServerSocketChannel.open();
        _serverSocketChannel.configureBlocking(false);
        _serverSocketChannel.socket().bind(new InetSocketAddress(40000));
        _serverSocketChannel.register(_selector, SelectionKey.OP_ACCEPT, ByteBuffer.allocate(1024));
    }

    public void run() {
        while (true) {
            try {
                _selector.select();
                Iterator<SelectionKey> it = _selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    if (key.isAcceptable()) {
                        ServerSocketChannel chan = (ServerSocketChannel) key.channel();
                        SocketChannel sc = chan.accept();
                        String response = "Hello " + sc.socket().getInetAddress() + ":" + sc.socket().getPort()
                                + " # " + new Date();

                        byte[] data = response.getBytes("UTF-8");

                        ByteBuffer bb = ((ByteBuffer) key.attachment()).wrap(data);
                        
                        while (bb.hasRemaining())
                            sc.write(bb);
                        sc.close();
                    }
                    it.remove();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
