package at.htl.leoquest.boundary;

import at.htl.leoquest.control.AnswerOptionRepository;
import at.htl.leoquest.control.ChosenOptionRepository;
import at.htl.leoquest.control.QuestionRepository;
import at.htl.leoquest.control.TransactionRepository;
import at.htl.leoquest.dtos.DisplayResultDTO;
import at.htl.leoquest.entities.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("leoquest")
public class ChosenOptionEndpoint {
    @Inject
    ChosenOptionRepository chosenOptionRepository;

    @Inject
    AnswerOptionRepository answerOptionRepository;

    @Inject
    TransactionRepository s_transactionRepository;

    @Inject
    QuestionRepository questionRepository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/chosenoptions")
    public Response findAllChosenOptions(){
        final List<ChosenOption> options = chosenOptionRepository.findAllOptions();
        return Response.ok(options).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/chosenoptions/{id}")
    public Response findChosenOptionsByQuestionnaire(@PathParam("id") long id){
        final List<ChosenOption> chosenOptions = chosenOptionRepository.findChosenOptionsByQuestionnaire(id);
        return Response.ok(chosenOptions).build();
    }

    @POST
    @Path("/chosenoptions/add")
    public Response addChosenOption(ChosenOption chosenOption, @Context UriInfo info){
        if(chosenOption.getQuestion().getQ_type().equals("FREETEXT") == false) {
            AnswerOption ao = answerOptionRepository.findAllOptions().get(Math.toIntExact(chosenOption.getAnswerOption().getId() - 1));
            ao.setHow_often(ao.getHow_often() + 1);
            chosenOption.setAnswerOption(ao);
        }
        final ChosenOption savedChosenOption = chosenOptionRepository.save(chosenOption);
        URI uri = info.getAbsolutePathBuilder().path("/leosurvey/chosenoptions/add/" + savedChosenOption.getId()).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/chosenoptions/getDTO/{trid}")
    public Response getDisplayDTOs(@PathParam("trid") long trid){
        List<DisplayResultDTO> displayResultDTOS = new LinkedList<>();
        HashMap<AnswerOption, Integer> hashMap = new HashMap<>();
        Transaction t = s_transactionRepository.findById(trid);
        List<ChosenOption> coList = chosenOptionRepository.getByTrCode(t.getCode());

        for (var item : coList) {
            var ao = answerOptionRepository.findById(item.getAnswerOption().getId());
            var aoCount = 0;
            hashMap.put(ao, aoCount);
            displayResultDTOS.add(new DisplayResultDTO(questionRepository.findById(item.getQuestion().getId()), hashMap));
        }

        return Response.ok().entity(displayResultDTOS).build();
    }
}
