package demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CurrencyExchangeServiceProxy proxy;

	@GetMapping(value= "/currency-converter/from/{from}/to/{to}/quantity/{quantity}", produces = { "application/json" })
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8003/currency-exchange/currency-exchange/from/{from}/to/{to}",
				CurrencyConversionBean.class, uriVariables);

		CurrencyConversionBean response = responseEntity.getBody();

		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), 8003);
	}

	@GetMapping(value = "/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}" , produces = { "application/json" })
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);

		logger.info("{}", response);

		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()), response.getPort());
	}

	/*
	 * @PutMapping("/updateConverter/from/{from}/factor/{factor}")
	 * 
	 * public void updateConverter
	 * 
	 * (@PathVariable String from, @PathVariable String factor) {
	 * 
	 * proxy.updateConversionFactor(Integer.valueOf(factor), from); }
	 * 
	 * @GetMapping("/getData") public Iterable<CurrencyConversionBean>
	 * getConverter() { return proxy.findAll(); }
	 * 
	 * @GetMapping("/getCurrency/from/{from}/to/{to}")
	 * 
	 * public int converter
	 * 
	 * (@PathVariable String from, @PathVariable String to) {
	 * 
	 * int converterValue =
	 * 
	 * proxy.getConversionFactor(from, to);
	 * 
	 * return converterValue;
	 * 
	 * }
	 * 
	 * @GetMapping("/convertCurrency/from/{from}/to/{to}/amount/{amount}")
	 * 
	 * public int convertCurrency
	 * 
	 * (@PathVariable String from, @PathVariable String to, @PathVariable String
	 * amount) {
	 * 
	 * int converterValue =
	 * 
	 * proxy.getConversionFactor(from, to); int converterdAmount = converterValue *
	 * Integer.valueOf(amount);
	 * 
	 * return converterdAmount;
	 * 
	 * }
	 */
}
