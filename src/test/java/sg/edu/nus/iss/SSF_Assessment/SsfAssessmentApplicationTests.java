package sg.edu.nus.iss.SSF_Assessment;


import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import sg.edu.nus.iss.SSF_Assessment.Services.QuotationService;

@SpringBootTest
@AutoConfigureMockMvc
class SsfAssessmentApplicationTests {

	@Autowired
	private MockMvc mvc;


	@Test
	void testGetQuotations() throws Exception {
		JsonObject body = Json.createObjectBuilder()
				.add("name", "alex")
				.add("address", "alex town")
				.add("email", "alex@gmail.com")
				.add("lineItems",
				Json.createArrayBuilder()
				.add(Json.createObjectBuilder().add("item", "durian").add("quantity", 1))
				.add(Json.createObjectBuilder().add("item", "plum").add("quantity", 1))
				.add(Json.createObjectBuilder().add("item", "pear").add("quantity", 1)))
				.add("navigationId", 1)
				.build();

		RequestBuilder req = MockMvcRequestBuilders.post("/api/po")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body.toString());

		this.mvc.perform(req).andDo(print()).andExpect(status().isNotFound())
			.andExpect(result -> assertFalse(result.getResponse().getContentAsString().contains("plum")));

	}
}
