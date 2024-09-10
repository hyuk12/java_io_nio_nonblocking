package com.musma.java_io_nio_nonbloking.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaIOMultiClient {
	private static ExecutorService executorService = Executors.newFixedThreadPool(50);

	@SneakyThrows
	public static void main(String[] args) {

		List<CompletableFuture> futures = new ArrayList<>();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			var future = CompletableFuture.runAsync(() -> {
				try (Socket socket = new Socket()) {

					socket.connect(new InetSocketAddress("localhost", 8080));

					OutputStream outputStream = socket.getOutputStream();
					String requestBody = "This is client";
					outputStream.write(requestBody.getBytes());
					outputStream.flush();

					InputStream inputStream = socket.getInputStream();
					byte[] buffer = new byte[1024];
					inputStream.read(buffer);
					System.out.println("Response: " + new String(buffer).trim());
				} catch (Exception e) {
					System.out.println(e);
				}
			}, executorService);

			futures.add(future);
		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		executorService.shutdown();
		long end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start) + "ms");
	}
}
