package com.honey;

import co.paralleluniverse.fibers.Fiber;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Phaser;

public class Jdial {
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("dail").build()
                .defaultHelp(true)
                .description("An example of server ports scanning.");
        parser.addArgument("--hostname")
                .setDefault("")
                .required(true)
                .type(String.class)
                .help("hostname to test");
        parser.addArgument("--start-port")
                .setDefault(80)
                .type(Integer.class)
                .help("the port on which the scanning starts");
        parser.addArgument("--end-port")
                .setDefault(100)
                .type(Integer.class)
                .help("the port from which the scanning ends");
        parser.addArgument("--timeout")
                .setDefault(200)
                .type(Integer.class)
                .help("timeout");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.printHelp();
            System.exit(1);
        }
        List<Integer> ports = new CopyOnWriteArrayList<>();
        Phaser phaser = new Phaser(1);
        String hostname = ns.getString("hostname");
        Integer startPort = ns.getInt("start_port");
        Integer endPort = ns.getInt("end_port");
        Integer timeout = ns.getInt("timeout");
        for (int port = startPort; port < endPort; port++) {
            phaser.register();
            int finalPort = port;
            new Fiber<Void>(() -> {
                boolean opened = isOpen(hostname, finalPort, timeout);
                if (opened) {
                    ports.add(finalPort);
                }
                phaser.arriveAndDeregister();
            }).start();
        }
        phaser.arriveAndAwaitAdvance();
        System.out.println("opened ports: " + ports);
    }

    private static boolean isOpen(String host, int port, int timeout) {
        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return socket.isConnected();
        } catch (IOException e){
            //ignore
            return false;
        }

    }
}
