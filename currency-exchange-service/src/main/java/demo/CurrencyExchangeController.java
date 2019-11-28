package demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class CurrencyExchangeController {

	@Autowired
	private Environment environment;

	@Autowired
	private ExchangeValueRepository repository;

	/*
	 * @GetMapping("/currency-exchange/from/{from}/to/{to}") public ExchangeValue
	 * retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
	 * 
	 * ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
	 * 
	 * exchangeValue.setPort(Integer.parseInt(environment.getProperty(
	 * "local.server.port")));
	 * 
	 * return exchangeValue; }
	 */

	@GetMapping(value = "/currency-exchange/from/{from}/to/{to}", produces = { "application/json" })
	@HystrixCommand(fallbackMethod = "fallbackretrieveExchangeValue")
	public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {

		if (new Random().nextBoolean()) {
			ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);

			exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));

			return exchangeValue;

		} else {
			throw new RuntimeException();
		}

	}

	public ExchangeValue fallbackretrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
		ExchangeValue exchangeValue = new ExchangeValue();
		exchangeValue.setFallback("Fallback Enabled");
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		exchangeValue.setConversionMultiple(new BigDecimal("75"));
		return exchangeValue;
	}

	@PostMapping(value = "addConverter/from/{from}/to/{to}/factor/{factor}",  produces = { "application/json" })

	public void addConverter

	(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal factor) {

		List<ExchangeValue> list = new ArrayList<>();

		list.add(new ExchangeValue(10004l, from, to, factor));

		repository.saveAll(list);
	}

	@PutMapping(value = "/updateConverter/from/{from}/factor/{factor}",  produces = { "application/json" })

	public void updateConverter

	(@PathVariable String from, @PathVariable String factor) {

		repository.updateConversionFactor(new BigDecimal(factor), from);
	}

	@GetMapping(value ="/getData",  produces = { "application/json" })
	public Iterable<ExchangeValue> getConverter() {
		return repository.findAll();
	}

	@GetMapping("/getCurrency/from/{from}/to/{to}")

	public BigDecimal converter

	(@PathVariable String from, @PathVariable String to) {

		BigDecimal converterValue =

				repository.getConversionFactor(from, to);

		return converterValue;

	}

}

/*
 * @RestController public class ForexController {
 * 
 * @Autowired ExchangeValueRepository converterRepository;
 * 
 * @Autowired
 * 
 * private Environment environment;
 * 
 * @PostMapping("addConverter/from/{from}/to/{to}/factor/{factor}")
 * 
 * public void addConverter
 * 
 * (@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal
 * factor) {
 * 
 * List<ExchangeValue> list = new ArrayList<>();
 * 
 * list.add(new ExchangeValue(from, to, factor));
 * 
 * converterRepository.saveAll(list); }
 */

/*
 * @PutMapping("/updateConverter/from/{from}/factor/{factor}")
 * 
 * public void updateConverter
 * 
 * (@PathVariable String from, @PathVariable String factor) {
 * 
 * converterRepository.updateConversionFactor(new BigDecimal(factor), from); }
 * 
 * @GetMapping("/getData") public Iterable<ExchangeValue> getConverter() {
 * return converterRepository.findAll(); }
 * 
 * @GetMapping("/getCurrency/from/{from}/to/{to}")
 * 
 * public BigDecimal converter
 * 
 * (@PathVariable String from, @PathVariable String to) {
 * 
 * BigDecimal converterValue =
 * 
 * converterRepository.getConversionFactor(from, to);
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
 * BigDecimal converterValue =
 * 
 * converterRepository.getConversionFactor(from, to);
 * 
 * int converterdAmount = converterValue.intValue() * Integer.valueOf(amount);
 * 
 * return converterdAmount;
 * 
 * } }
 */
