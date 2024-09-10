package com.musma.java_io_nio_nonbloking.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaNIONonBlockingMultiServer {
	private static AtomicInteger count = new AtomicInteger(0);
	private static ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(50);

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
				CompletableFuture.runAsync(() -> {

					try {
						handleRequest(clientSocket);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}

				}, executorService);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@SneakyThrows
	private static void handleRequest(SocketChannel clientSocket) throws IOException {
		ByteBuffer requestByteBuffer = ByteBuffer.allocate(1024);
		while (true) {
			try {
				if (!(clientSocket.read(requestByteBuffer) == 0))
					break;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			System.out.println("Waiting for request...");
		}

		requestByteBuffer.flip();
		String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();

		System.out.println("Request: " + requestBody);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		ByteBuffer responseByteBuffer = ByteBuffer.wrap("This. is server".getBytes());
		clientSocket.write(responseByteBuffer);
		clientSocket.close();
		int c = count.incrementAndGet();
		System.out.println("Request count: " + c);
	}
}
