package com.example.mapYML.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class yamlService {

	public Map<String, String> getKeyValuePairs() {
		Map<String, String> out = new LinkedHashMap<>();
		try {
			YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
			List<PropertySource<?>> sources = loader.load("application.yml", new ClassPathResource("application.yml"));

			for (PropertySource<?> ps : sources) {
				Object src = ps.getSource();
				if (src instanceof Map<?, ?> map) {
					for (Map.Entry<?, ?> e : map.entrySet()) {
						String key = String.valueOf(e.getKey());
						Object val = e.getValue();
						if (val != null) {
							out.put(key, String.valueOf(val));
						}
					}
				}
			}
		} catch (Exception e) {

		}
		return out;
	}

	public List<String> getKeys() {
		return new ArrayList<>(getKeyValuePairs().keySet());
	}

	public List<String> getValues() {
		return new ArrayList<>(getKeyValuePairs().values());
	}

}
