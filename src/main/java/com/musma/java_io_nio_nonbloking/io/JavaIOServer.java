package com.musma.java_io_nio_nonbloking.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaIOServer {
	@SneakyThrows
	public static void main(String[] args) {
		System.out.println("Server started at port 8080");
		while (true) {
			try (ServerSocket serverSocket = new ServerSocket()) {
				serverSocket.bind(new java.net.InetSocketAddress(8080));
				Socket clientSocket = serverSocket.accept();

				byte[] buffer = new byte[1024];
				InputStream inputStream = clientSocket.getInputStream();
				inputStream.read(buffer);
				System.out.println("Request: " + new String(buffer).trim());

				OutputStream outputStream = clientSocket.getOutputStream();
				String responseBody = "Hello, Java IO!";
				outputStream.write(responseBody.getBytes());
				outputStream.flush();
			}catch (Exception e) {
				System.out.println(e);
			}
		}

	}
}
