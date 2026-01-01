package com.example.mapYML;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.mapYML.service.yamlService;

@SpringBootTest
@AutoConfigureMockMvc
class MapYmlApplicationTests {

	@Autowired
	private yamlService service;

	@Autowired
	private MockMvc mockMvc;

	// ---------------------------
	// Service-level tests
	// ---------------------------

	@Test
	@DisplayName("Service: getKeyValuePairs should include spring.application.name=mapYML and server.port=8085")
	void service_shouldReturnExpectedPairs() {
		Map<String, String> kv = service.getKeyValuePairs();

		assertNotNull(kv, "Key-value map must not be null");
		assertFalse(kv.isEmpty(), "Key-value map must not be empty");

		assertTrue(kv.containsKey("spring.application.name"), "Expected key spring.application.name to be present");
		assertTrue(kv.containsKey("server.port"), "Expected key server.port to be present");

		assertEquals("mapYML", kv.get("spring.application.name"));
		assertEquals("8085", kv.get("server.port"));
	}

	@Test
	@DisplayName("Service: getKeys should return flattened keys (includes spring.application.name, server.port)")
	void service_shouldReturnOnlyKeys() {
		var keys = service.getKeys();

		assertNotNull(keys);
		assertTrue(keys.contains("spring.application.name"));
		assertTrue(keys.contains("server.port"));
	}

	@Test
	@DisplayName("Service: getValues should return only values (includes mapYML, 8085 as strings)")
	void service_shouldReturnOnlyValues() {
		var values = service.getValues();

		assertNotNull(values);
		assertTrue(values.contains("mapYML"));
		assertTrue(values.contains("8085"));
	}

	// ---------------------------
	// Controller/endpoint tests (flattened map)
	// ---------------------------

	@Test
	@DisplayName("Endpoint /api/yml/keyvalue should return flattened map with spring.application.name=mapYML and server.port=8085")
	void kv_endpoint_shouldReturnFlattenedMap() throws Exception {
		mockMvc.perform(get("/api/yml/keyvalue")).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				// Use bracket notation for keys with dots
				.andExpect(jsonPath("$['spring.application.name']", is("mapYML")))
				.andExpect(jsonPath("$['server.port']", is("8085")));
	}

	@Test
	@DisplayName("Endpoint /api/yml/keys should include flattened keys spring.application.name and server.port")
	void keys_endpoint_shouldReturnOnlyKeys() throws Exception {
		mockMvc.perform(get("/api/yml/keys")).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasItem("spring.application.name")))
				.andExpect(jsonPath("$", hasItem("server.port")));
	}

	@Test
	@DisplayName("Endpoint /api/yml/values should include mapYML and 8085 (as strings)")
	void values_endpoint_shouldReturnOnlyValues() throws Exception {
		mockMvc.perform(get("/api/yml/values")).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasItem("mapYML"))).andExpect(jsonPath("$", hasItem("8085")));
	}
}
