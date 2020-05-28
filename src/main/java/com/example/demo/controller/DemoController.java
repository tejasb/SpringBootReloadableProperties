package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Splitter;

@RestController(value = "/demo")
public class DemoController {

	@Autowired
	PropertiesConfiguration propertiesConfiguraion;

	@GetMapping(value = "/hi")
	public String sayHi() {

		Iterator<String> keyIterator = propertiesConfiguraion.getKeys("stg.client");
		Map<String, Map<String, String>> clientInfoMap = new HashMap<String, Map<String, String>>();
		while (keyIterator.hasNext()) {
			System.out.println();
			String clientKeys = keyIterator.next();
			String clientName = org.apache.commons.lang.StringUtils.substringAfterLast(clientKeys, ".");
			Object[] arr = ((ArrayList<String>) propertiesConfiguraion.getProperty(clientKeys)).toArray();
			String clientProperty = StringUtils.arrayToCommaDelimitedString(arr);
			Map<String, String> clientInfo = Splitter.on(",").withKeyValueSeparator("=").split(clientProperty);
			clientInfoMap.put(clientName, clientInfo);
		}

		return new String("No of clients: " + clientInfoMap.size());
	}
}
