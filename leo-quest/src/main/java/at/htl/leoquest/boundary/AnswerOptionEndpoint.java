package at.htl.leoquest.boundary;

import at.htl.leoquest.control.AnswerOptionRepository;
import at.htl.leoquest.control.AnswerOptionRepository;
import at.htl.leoquest.entities.AnswerOption;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("leoquest")
public class AnswerOptionEndpoint {
    @Inject
    AnswerOptionRepository answerOptionRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/options")
    public Response findAllOptions(){
        final List<AnswerOption> options = answerOptionRepository.findAllOptions();
        return Response.ok(options).build();
    }


}
