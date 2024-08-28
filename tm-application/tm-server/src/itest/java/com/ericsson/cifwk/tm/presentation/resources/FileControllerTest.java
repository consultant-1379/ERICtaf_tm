package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.FileData;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.collect.Sets;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FileControllerTest extends BaseControllerLevelTest {

    public static final String TEST_CASES_URL = "/tm-server/api/test-cases/";
    public static final String FILES_URL = "/tm-server/api/files/";
    private static final String REQUIREMENT_ID1 = "TORF-4200";
    private static final String REQUIREMENT_ID2 = "TORF-4177";

    private static final MediaType[] ACCEPTED = {MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.WILDCARD_TYPE};

    private static final GenericType<List<FileData>> FILE_DATA_LIST = new GenericType<List<FileData>>() {};

    private URL fileResource;
    private URL fileResource2;
    private URL fileResource3;

    @Before
    public void setUp() {
        fileResource = getClass().getClassLoader().getResource("fileStorage/iTest.txt");
        fileResource2 = getClass().getClassLoader().getResource("fileStorage/iTest2.txt");
        fileResource3 = getClass().getClassLoader().getResource("fileStorage/iTest3#.txt");
    }

    @Test
    public void saveFilesToDirectoryAndDelete() throws URISyntaxException {
        TestCaseInfo json = getTestCase();
        String author = "test name";
        String filename = "iTest.txt";
        String filename2 = "iTest2.txt";

        // creating entity
        TestCaseInfo testCaseInfo = postTestCase(json);

        File fileToUpload = new File(fileResource.toURI());
        File fileToUpload2 = new File(fileResource2.toURI());

        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload2, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        multiPart.bodyPart(new FormDataBodyPart("author", author));

        List<FileData> files = postFiles(multiPart, testCaseInfo.getFeature().getProduct().getName(), "test-cases", testCaseInfo.getId());

        FileData result = files.get(0);
        assertThat(result.getAuthor(), equalTo(author));
        assertThat(result.getFilename(), equalTo(filename));

        result = files.get(1);
        assertThat(result.getFilename(), equalTo(filename2));

        Response deleteResponse = app.client()
                .path(FILES_URL + testCaseInfo.getFeature().getProduct().getName() + "/test-cases/" + testCaseInfo.getId())
                .queryParam("filenames", filename + "," + filename2)
                .request()
                .accept(ACCEPTED)
                .delete();

        deleteResponse.close();
        assertStatus(deleteResponse, Response.Status.NO_CONTENT);

    }

    @Test
    public void retrieveFile() throws URISyntaxException {
        TestCaseInfo json = getTestCase();
        String author = "test name";
        String filename = "iTest.txt";

        // creating entity
        TestCaseInfo testCaseInfo = postTestCase(json);

        File fileToUpload = new File(fileResource.toURI());

        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        multiPart.bodyPart(new FormDataBodyPart("author", author));

        postFiles(multiPart, testCaseInfo.getFeature().getProduct().getName(), "test-cases", testCaseInfo.getId());

        Response retrieveResponse = app.client()
                .path(FILES_URL + testCaseInfo.getFeature().getProduct().getName() + "/test-cases/" + testCaseInfo.getId() + "/" + filename)
                .request()
                .accept(ACCEPTED)
                .get();

        assertStatus(retrieveResponse, Response.Status.OK);

        String retrievedFileData = retrieveResponse.readEntity(String.class);
        retrieveResponse.close();
        assertThat(retrievedFileData, equalTo("I live here"));
    }

    @Test
    public void saveFileToDirectoryWithBadTestCase() throws URISyntaxException {
        String author = "test name";
        Long badTestCaseId = 200L;

        File fileToUpload = new File(fileResource.toURI());

        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        multiPart.bodyPart(new FormDataBodyPart("author", author));

        Response createResponse = app.client()
                .path(FILES_URL + "ENM" + "/test-cases/" + badTestCaseId)
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));

        createResponse.close();
        assertStatus(createResponse, Response.Status.BAD_REQUEST);
    }

    @Test
    public void saveFileWithBadFilename() throws URISyntaxException {
        TestCaseInfo json = getTestCase();
        String author = "test name";

        // creating entity
        TestCaseInfo testCaseInfo = postTestCase(json);

        File fileToUpload = new File(fileResource3.toURI());

        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        multiPart.bodyPart(new FormDataBodyPart("author", author));

        String productName = testCaseInfo.getFeature().getProduct().getName();
        String category = "test-cases";
        Response response = app.client()
                .path(FILES_URL + productName + "/" + category + "/" + testCaseInfo.getId())
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    private TestCaseInfo postTestCase(TestCaseInfo testCaseInfo) {
        Response response = app.client()
                .path(TEST_CASES_URL)
                .request()
                .post(Entity.entity(testCaseInfo, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        TestCaseInfo created = response.readEntity(TestCaseInfo.class);
        response.close();

        return created;
    }

    private List<FileData> postFiles(FormDataMultiPart multiPart, String productName, String category, Long entityId) {
        Response createResponse = app.client()
                .path(FILES_URL + productName + "/" + category + "/" + entityId)
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));

        assertStatus(createResponse, Response.Status.CREATED);

        List<FileData> files = createResponse.readEntity(FILE_DATA_LIST);

        return files;
    }

    private TestCaseInfo getTestCase() {
        Product product = fixture().persistProduct("ENM");
        ProductFeature feature = fixture().persistFeature("PM", product);
        TechnicalComponent component = fixture().persistTechnicalComponent("PM_COMPONENT", feature);
        Project project = fixture().persistProject("TORF", "TORF", product);

        fixture().persistRequirement(REQUIREMENT_ID1, project);
        fixture().persistRequirement(REQUIREMENT_ID2, project);
        fixture().persistTestType("FUNCTIONAL", product);

        Scope scope = fixture().persistScope("KGB", product);

        TestCaseInfo testCaseInfo = getTestCaseJson();

        // set appropriate fields with data from entities persisted above
        testCaseInfo.setGroups(Sets.newHashSet(new ReferenceDataItem(scope.getId().toString(), scope.getName())));

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setId(feature.getId());

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        featureInfo.setProduct(productInfo);
        testCaseInfo.setFeature(featureInfo);

        ReferenceDataItem componentInfo = new ReferenceDataItem(component.getId().toString(), component.getName());
        testCaseInfo.addTechnicalComponent(componentInfo);

        return testCaseInfo;
    }

    private TestCaseInfo getTestCaseJson() {
        Gson json = new Gson();
        InputStream is = getTestCaseAsInputStream();
        Reader ioReader = new InputStreamReader(is);
        return json.fromJson(ioReader, TestCaseInfo.class);
    }

    private InputStream getTestCaseAsInputStream() {
        return getClass().getClassLoader().getResourceAsStream("fileStorage/testCase.json");
    }
}
