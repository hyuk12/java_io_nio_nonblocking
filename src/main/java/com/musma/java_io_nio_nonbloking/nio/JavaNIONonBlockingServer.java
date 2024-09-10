package com.musma.java_io_nio_nonbloking.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaNIONonBlockingServer {
	@SneakyThrows
	public static void main(String[] args) {
		System.out.println("Server started at port 8080");
		try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
			serverSocket.bind(new java.net.InetSocketAddress(8080));
			serverSocket.configureBlocking(false); // Non-blocking mode
			while (true) {
				SocketChannel clientSocket = serverSocket.accept();
				if (clientSocket == null) {
					Thread.sleep(1000);
					continue;
				}
				handleRequest(clientSocket);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void handleRequest(SocketChannel clientSocket) throws InterruptedException, IOException {
		ByteBuffer requestByteBuffer = ByteBuffer.allocate(1024);
		while (clientSocket.read(requestByteBuffer) == 0) {
			System.out.println("Waiting for request...");
		}

		requestByteBuffer.flip();
		String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();

		System.out.println("Request: " + requestBody);
		Thread.sleep(10);
		ByteBuffer responseByteBuffer = ByteBuffer.wrap("This. is server".getBytes());
		clientSocket.write(responseByteBuffer);
		clientSocket.close();


	}
}
