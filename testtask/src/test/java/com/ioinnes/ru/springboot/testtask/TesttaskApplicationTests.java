package com.ioinnes.ru.springboot.testtask;

import com.ioinnes.ru.springboot.testtask.entity.UserDTO;
import com.ioinnes.ru.springboot.testtask.requests.RequestsCreator;
import com.ioinnes.ru.springboot.testtask.requests.RequestsExecutor;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
		} catch (Exception ignore) {}

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
		String username = "POP";
		String email = "qwerty@qwerty.oo";
		String mobileNumber = null;
		RequestSpecification postRequestSpecification =
				requestsCreator.createPostRequestSpecification(imageURI, username, email, mobileNumber);

		String response = requestsExecutor.executePostRequest(postRequestSpecification);

		int id = 0;
		// get id
		if (response.matches("User with id = \\d+ was added")) {
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
}
