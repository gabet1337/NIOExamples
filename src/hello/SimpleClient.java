package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SimpleClient {
    
    public static void main(String[] args) throws IOException {
        HelloServer hs = new HelloServer(40000);
        Thread t = new Thread(hs);
        t.start();
        Socket s = new Socket("localhost", 40000);
        Socket s1 = new Socket("localhost", 40000);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedReader br1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
        
        
        System.out.println(br.readLine());
        System.out.println(br1.readLine());
        
    }

}
