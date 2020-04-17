package de.aservo.atlassian.jira.confapi.rest;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.DirectoryBean;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.rest.api.DirectoryResource;
import de.aservo.atlassian.confapi.service.api.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path(ConfAPI.DIRECTORIES)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class DirectoryResourceImpl implements DirectoryResource {

    private static final Logger log = LoggerFactory.getLogger(DirectoryResourceImpl.class);

    private final DirectoryService directoryService;

    @Inject
    public DirectoryResourceImpl(DirectoryService directoryService) {
        this.directoryService = checkNotNull(directoryService);
    }

    @GET
    @Override
    public Response getDirectories() {
        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            List<DirectoryBean> directories = directoryService.getDirectories();
            return Response.ok(directories).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

    @PUT
    @Override
    public Response addDirectory(
            @QueryParam("testConnection") boolean testConnection,
            @Nonnull final DirectoryBean directory) {

        final ErrorCollection errorCollection = new ErrorCollection();
        try {
            DirectoryBean addDirectory = directoryService.addDirectory(directory, testConnection);
            return Response.ok(addDirectory).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorCollection.addErrorMessage(e.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errorCollection).build();
    }

}
