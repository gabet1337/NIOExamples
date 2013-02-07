package hello;

import java.io.*;
import java.net.SocketException;
import java.nio.channels.*;
import java.nio.*;
import java.util.Iterator;

/*
 * The client creates a connection to the server and re
 */
public class HelloClient implements Runnable {

    private SocketChannel _socketChannel;
    private Selector _selector;

    public HelloClient() throws IOException {
        _selector = Selector.open();
        _socketChannel = SocketChannel.open();
        _socketChannel.configureBlocking(false);
        _socketChannel.register(_selector, SelectionKey.OP_CONNECT, ByteBuffer.allocate(1024));
    }


    public void addClient() {
        try {
            _socketChannel = SocketChannel.open();
            _socketChannel.configureBlocking(false);
            _socketChannel.register(_selector, SelectionKey.OP_CONNECT, ByteBuffer.allocate(1024));
        } catch (IOException e) {
            System.err.println("Couldn't register the client");
            System.err.println(e);
        }
    }

    public void run() {

        while (true) {
            try {

                _selector.select();

                Iterator<SelectionKey> it = _selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();

                    if (key.isConnectable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        if (sc.finishConnect()) {
                            key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        }
                    } else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        int numRead = sc.read((ByteBuffer) key.attachment());
                        if (numRead == -1) {
                            key.channel().close();
                            key.cancel();
                        } else {
                            handleRead(key);
                        }
                    } else if (key.isWritable()) {
                        
                    }

                }

            } catch (IOException e) {
                
            }
        }

    }
    
    private void handleRead(SelectionKey key) {
        try {
            ByteBuffer data = (ByteBuffer) key.attachment();
            BufferedReader in = new BufferedReader(new CharArrayReader(data.asCharBuffer().array()));
            String tmp = in.readLine();
            while (tmp != null) {
                System.out.println(tmp);
                tmp = in.readLine();
            }
        } catch (SocketException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
