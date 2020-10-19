package org.acme.sns;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.acme.sns.model.Quark;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Path("/async/cannon")
public class QuarksCannonAsyncResource {

    private static final Logger LOGGER = Logger.getLogger(QuarksCannonAsyncResource.class);

    @Inject
    SnsAsyncClient sns;

    @ConfigProperty(name = "topic.arn")
    String topicArn;

    static ObjectWriter QUARK_WRITER = new ObjectMapper().writerFor(Quark.class);

    @POST
    @Path("/shoot")
    public Uni<Response> publish(Quark quark) throws Exception {
        String message = QUARK_WRITER.writeValueAsString(quark);
        return Uni.createFrom()
            .completionStage(sns.publish(p -> p.topicArn(topicArn).message(message)))
            .onItem().invoke(item -> LOGGER.infov("Fired Quark[{0}, {1}}]", quark.getFlavor(), quark.getSpin()))
            .onItem().transform(PublishResponse::messageId)
            .onItem().transform(id -> Response.ok().entity(id).build());
    }
}