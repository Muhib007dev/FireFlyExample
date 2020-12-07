package com.example.server;

import java.util.concurrent.Phaser;

import com.firefly.server.http2.HTTP2ServerBuilder;
import com.firefly.server.http2.HTTPServerRequest;
import com.firefly.$;

import com.firefly.*;
public class Exampl1 {

	public static void main(String[] args) throws Exception{
		Phaser phaser = new Phaser(2);

        HTTP2ServerBuilder httpServer = $.httpServer();
        httpServer.router().get("/").handler(ctx -> ctx.write("hello world! ").next())
                  .router().get("/").handler(ctx -> ctx.end("end message"))
                  .listen("localhost", 8080);

        $.httpClient().get("http://localhost:8080/").submit()
         .thenAccept(res -> System.out.println(res.getStringBody()))
         .thenAccept(res -> phaser.arrive());

        phaser.arriveAndAwaitAdvance();
        httpServer.stop();
        $.httpClient().stop();
	}

}
