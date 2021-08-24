package com.orange.server

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject

@Controller
class GRpcServerController(@Inject val grpcEmbeddedServer: GrpcEmbeddedServer) {

    @Get("/grpc-server/stop")
    fun stop() : HttpResponse<Any> {
        grpcEmbeddedServer.stop()
        return HttpResponse.ok("is-running?: ${grpcEmbeddedServer.isRunning}")
    }

}