package org.acme.ses;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.ses.model.Email;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Path("/async")
@Produces(MediaType.TEXT_PLAIN)
public class QuarkusSesAsyncResource {

    @Inject
    SesAsyncClient ses;

    @POST
    @Path("/email")
    public Uni<String> encrypt(Email data) {
        return Uni.createFrom()
            .completionStage(
                ses.sendEmail(req -> req
                    .source(data.getFrom())
                    .destination(d -> d.toAddresses(data.getTo()))
                    .message(msg -> msg
                        .subject(sub -> sub.data(data.getSubject()))
                        .body(b -> b.text(txt -> txt.data(data.getBody()))))))
            .onItem().transform(SendEmailResponse::messageId);
    }
}