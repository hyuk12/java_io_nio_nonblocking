package com.musma.java_io_nio_nonbloking.nio;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaNIOBlockingServer {
	@SneakyThrows
	public static void main(String[] args) {
		System.out.println("Server started at port 8080");
		while (true) {
			try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
				serverSocket.bind(new java.net.InetSocketAddress(8080));
				SocketChannel clientSocket = serverSocket.accept();

				ByteBuffer requestByteBuffer = ByteBuffer.allocate(1024);
				clientSocket.read(requestByteBuffer);
				requestByteBuffer.flip();
				String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();

				System.out.println("Request: " + requestBody);

				ByteBuffer responseByteBuffer = ByteBuffer.wrap("This. is server".getBytes());
				clientSocket.write(responseByteBuffer);
				clientSocket.close();
			}catch (Exception e) {
				System.out.println(e);
			}
		}

	}
}
