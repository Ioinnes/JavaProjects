package com.ioinnes.ru.springboot.testtask;

import com.ioinnes.ru.springboot.testtask.auxiliry.StringGenerator;
import com.ioinnes.ru.springboot.testtask.auxiliry.ValidChecker;
import com.ioinnes.ru.springboot.testtask.entity.UserDTO;
import com.ioinnes.ru.springboot.testtask.requests.RequestsCreator;
import com.ioinnes.ru.springboot.testtask.requests.RequestsExecutor;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class TesttaskApplicationTests {
	private static Logger logger = Logger.getLogger(TesttaskApplicationTests.class.getSimpleName());
	@Autowired
	private RequestsCreator requestsCreator;

	@Autowired
	private RequestsExecutor requestsExecutor;

	@Value("${images.default.image}")
	private String defaultImage;

	@Test
	public void getUserByExistingIDRequestTest() {
		UserDTO userDTO = null;

		RequestSpecification requestSpecification = requestsCreator.createGetRequestSpecification(25);

		try {
			userDTO = requestsExecutor.executeExistingIDGetRequest(requestSpecification);
		} catch (Exception ignore) {
		}

		assertThat(userDTO).isNotNull();
		assertThat(userDTO.getUserName()).isEqualTo("Ioinnes");
		assertThat(userDTO.getEmail()).isEqualTo("kekke@kekek.ru");
		assertThat(userDTO.getMobileNumber()).isEqualTo("+123456");
		assertThat(userDTO.getImageURI()).isEqualTo("/home/ubuntu/IdeaProjects/testtask/src/main/resources/images/default-avatar.jpg");
		assertThat(userDTO.getId()).isEqualTo(25);
	}


	@Test
	public void getUserByNotExistingIDRequestTest() {
		String result = null;
		RequestSpecification requestSpecification = requestsCreator.createGetRequestSpecification(0);

		try {
			result = requestsExecutor.executeNotExistingIDGetRequest(requestSpecification);
		} catch (Exception e) {
			logger.log(Level.INFO, "There is a problem with ID");
		}

		assertThat(result).isEqualTo("");
	}


	@Test
	public void postUserRequestTest() {
		String imageURI = null;
		String username = "PPrefer";
		String email = "qwrrffefrwty@qwerty.oo";
		String mobileNumber = null;
		RequestSpecification postRequestSpecification =
				requestsCreator.createPostRequestSpecification(imageURI, username, email, mobileNumber);

		String response = requestsExecutor.executePostRequest(postRequestSpecification);

		int id = 0;
		// get id
		if (response.matches("User with id = \\d+ was added.")) {
			Pattern pattern = Pattern.compile("\\b\\d+\\b");
			Matcher matcher = pattern.matcher(response);
			if (matcher.find())
				try {
					id = Integer.parseInt(response.substring(matcher.start(), matcher.end() + 1).trim());
				} catch (Exception ignore) {
					assertThat(1).isEqualTo(0);
				}
			System.out.println(id);
		} else {
			logger.log(Level.INFO, "Can't post because of duplicates (username, email) or something else");
			assertThat(1).isEqualTo(0);
		}

		RequestSpecification getRequestSpecification = requestsCreator.createGetRequestSpecification(id);

		UserDTO addedUser = requestsExecutor.executeExistingIDGetRequest(getRequestSpecification);

		assertThat(addedUser).isNotNull();

		assertThat(addedUser.getStatus()).isEqualTo("online");
		assertThat(addedUser.getTimestampStatus()).isNotNull();

		assertThat(addedUser.getMobileNumber()).isEqualTo(mobileNumber);
		assertThat(addedUser.getImageURI()).isEqualTo(defaultImage);
		assertThat(addedUser.getUserName()).isEqualTo(username);
		assertThat(addedUser.getEmail()).isEqualTo(email);


		//clear everything!!
		RequestSpecification deleteRequestSpecification = requestsCreator.createDeleteRequestSpecification(id);
		requestsExecutor.executeDeleteRequest(deleteRequestSpecification);


		// check was deleted or not
		// should be :)

		assertThat(requestsExecutor.executeNotExistingIDGetRequest(getRequestSpecification)).isEqualTo("");
	}


	@Test
	public void changeUserStatusRequestTest() {
		RequestSpecification getRequestSpecification = requestsCreator.createGetRequestSpecification(25);
		UserDTO previousUserCondition = requestsExecutor.executeExistingIDGetRequest(getRequestSpecification);

		RequestSpecification putRequestSpecification = requestsCreator.createPutRequestSpecification(25, "offline");
		logger.log(Level.INFO, requestsExecutor.executePutRequest(putRequestSpecification));

		UserDTO currentUserCondition = requestsExecutor.executeExistingIDGetRequest(getRequestSpecification);

		assertThat(previousUserCondition).isNotNull();
		assertThat(currentUserCondition).isNotNull();

		assertThat(previousUserCondition.getStatus()).isNotEqualTo(currentUserCondition.getStatus());
		assertThat(previousUserCondition.getTimestampStatus()).isNotEqualTo(currentUserCondition.getTimestampStatus());

		assertThat(previousUserCondition.getUserName()).isEqualTo(currentUserCondition.getUserName());
		assertThat(previousUserCondition.getId()).isEqualTo(currentUserCondition.getId());
		assertThat(previousUserCondition.getEmail()).isEqualTo(currentUserCondition.getEmail());
		assertThat(previousUserCondition.getImageURI()).isEqualTo(currentUserCondition.getImageURI());
		assertThat(previousUserCondition.getMobileNumber()).isEqualTo(currentUserCondition.getMobileNumber());

		RequestSpecification putRequestSpecification2 = requestsCreator.createPutRequestSpecification(25, "online");
		logger.log(Level.INFO, requestsExecutor.executePutRequest(putRequestSpecification2));


		UserDTO previousConditionWithDifferenceInTimestamp = requestsExecutor.executeExistingIDGetRequest(getRequestSpecification);

		assertThat(previousUserCondition).isNotNull();
		assertThat(currentUserCondition).isNotNull();

		assertThat(previousUserCondition.getStatus()).isEqualTo(previousConditionWithDifferenceInTimestamp.getStatus());
		assertThat(previousUserCondition.getTimestampStatus()).isNotEqualTo(previousConditionWithDifferenceInTimestamp.getTimestampStatus());

		assertThat(previousUserCondition.getUserName()).isEqualTo(previousConditionWithDifferenceInTimestamp.getUserName());
		assertThat(previousUserCondition.getId()).isEqualTo(previousConditionWithDifferenceInTimestamp.getId());
		assertThat(previousUserCondition.getEmail()).isEqualTo(previousConditionWithDifferenceInTimestamp.getEmail());
		assertThat(previousUserCondition.getImageURI()).isEqualTo(previousConditionWithDifferenceInTimestamp.getImageURI());
		assertThat(previousUserCondition.getMobileNumber()).isEqualTo(previousConditionWithDifferenceInTimestamp.getMobileNumber());
	}

	@Test
	public void getWithConditionOnlineRequestTest() {
		// check get with parametr status = "online" works properly
		RequestSpecification requestSpecificationOnline =
				requestsCreator.createGetWithConditionRequestCondition("online", null);

		UserDTO[] onlineResult = requestsExecutor.executeGetWithConditionRequest(requestSpecificationOnline);

		for (int i = 0; i < onlineResult.length; i++) {
			assertThat(onlineResult[i].getStatus()).isEqualTo("online");
			if (i != onlineResult.length - 1)
				assertThat(onlineResult[i].getTimestampStatus())
						.isBeforeOrEqualTo(onlineResult[i + 1].getTimestampStatus());
		}
	}

	@Test
	public void getWithConditionOfflineRequestTest() {
		// same for status = "offline"
		RequestSpecification requestSpecification =
				requestsCreator.createGetWithConditionRequestCondition("offline", null);

		UserDTO[] offlineResult = requestsExecutor.executeGetWithConditionRequest(requestSpecification);


		for (int i = 0; i < offlineResult.length; i++) {
			assertThat(offlineResult[i].getStatus()).isEqualTo("offline");
			if (i != offlineResult.length - 1)
				assertThat(offlineResult[i].getTimestampStatus())
						.isBeforeOrEqualTo(offlineResult[i + 1].getTimestampStatus());
		}
	}

	@Test
	public void getWithConditionTimestampRequestTest() {
		// same for status = "offline"
		RequestSpecification requestSpecification =
				requestsCreator.createGetWithConditionRequestCondition(null, 26);

		UserDTO[] result = requestsExecutor.executeGetWithConditionRequest(requestSpecification);

		for (int i = 0; i < result.length - 1; i++) {
			if (result[i].getStatus().equals(result[i + 1].getStatus()))
				assertThat(result[i].getTimestampStatus()).isBeforeOrEqualTo(result[i + 1].getTimestampStatus());
		}
	}

	@Test
	public void getWithConditionTimestampAndRequestTest() {
		RequestSpecification requestSpecification =
				requestsCreator.createGetWithConditionRequestCondition("online", 26);

		UserDTO[] result = requestsExecutor.executeGetWithConditionRequest(requestSpecification);

		for (int i = 0; i < result.length - 1; i++) {
			assertThat(result[i].getStatus()).isEqualTo("online");
			if (result[i].getStatus().equals(result[i + 1].getStatus()))
				assertThat(result[i].getTimestampStatus()).isBeforeOrEqualTo(result[i + 1].getTimestampStatus());
		}
	}

	@Test
	public void getWithConditionWithoutCondRequestTest() {
		RequestSpecification requestSpecification =
				requestsCreator.createGetWithConditionRequestCondition(null, null);

		UserDTO[] result = requestsExecutor.executeGetWithConditionRequest(requestSpecification);

		// now we will get all and check result
		RequestSpecification requestSpecification1 =
				requestsCreator.createGetAllRequestSpecification();

		UserDTO[] expected = requestsExecutor.executeGetAllRequest(requestSpecification1);

		for (UserDTO userDTO : expected) {
			assertThat(userDTO).isIn(result);
		}
	}


	@Test
	public void postImageRequestTest() throws IOException {
		RequestSpecification requestSpecification =
				requestsCreator.createPostImageRequestSpecification(
						new File("/home/ubuntu/Рабочий стол/testcat.jpg"));


		System.out.println(requestsExecutor.executePostImageRequest(requestSpecification));
	}

}

