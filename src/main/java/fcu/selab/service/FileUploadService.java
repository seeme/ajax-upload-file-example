package fcu.selab.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

@Path("/fileupload")
public class FileUploadService {

  @GET
  @Path("/echo")

  public String echo() {
    return "This is file upload service.";
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFiles(final FormDataMultiPart multiPart) {

    boolean isSave = true;
    List<FormDataBodyPart> bodyParts = multiPart.getFields("csvfile");

    for (FormDataBodyPart aPart : bodyParts) {
      BodyPartEntity bodyPartEntity = (BodyPartEntity) aPart.getEntity();
      String fileName = aPart.getContentDisposition().getFileName();
      File tmpFile = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
      try {
        FileUtils.copyInputStreamToFile(bodyPartEntity.getInputStream(), tmpFile);
        System.out.println("Save upload file in " + tmpFile.getAbsolutePath());
      } catch (IOException e) {
        isSave = false;
        e.printStackTrace();
      }
    }

    Response response = Response.ok().build();
    if (!isSave) {
      response = Response.serverError().status(Status.INTERNAL_SERVER_ERROR).build();
    }
    return response;
  }

}
