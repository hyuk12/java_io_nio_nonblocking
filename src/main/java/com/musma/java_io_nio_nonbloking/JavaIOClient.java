package com.musma.java_io_nio_nonbloking;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaIOClient {

	@SneakyThrows
	public static void main(String[] args) {
		try (Socket socket = new Socket()) {
			socket.connect(new java.net.InetSocketAddress("localhost", 8080));

			OutputStream outputStream = socket.getOutputStream();
			String requestBody = "Hello, Java IO!";
			outputStream.write(requestBody.getBytes());
			outputStream.flush();

			InputStream inputStream	= socket.getInputStream();
			byte[] buffer = new byte[1024];
			inputStream.read(buffer);
			System.out.println("Response: " + new String(buffer).trim());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
