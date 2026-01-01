package com.example.mapYML.contoller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mapYML.service.yamlService;

@RestController
@RequestMapping("api/yml")
public class ymlController {

	private final yamlService ys;

	public ymlController(yamlService ys) {
		this.ys = ys;
	}

	@GetMapping("/keyvalue")
	public Map<String, String> getKeyValuePairs() {
		return ys.getKeyValuePairs();
	}

	@GetMapping("/keys")
	public List<String> getKeys() {
		return ys.getKeys();
	}

	@GetMapping("/values")
	public List<String> getValues() {
		return ys.getValues();
	}

}
