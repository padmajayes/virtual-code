package demo;

import java.math.BigDecimal;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@RibbonClient(name="currency-conversion-service")
public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long> {

	ExchangeValue findByFromAndTo(String from, String to);
	
	@Transactional
	@Modifying
	@Query("update ExchangeValue c set c.conversionMultiple = :#{#factor} where c.from = :#{#from}  ")
	int updateConversionFactor(@Param(value = "factor") BigDecimal factor , @Param(value = "from") String from);
	
	@Query("select conversionMultiple from ExchangeValue c where c.from = :#{#from} and c.to = :#{#to}")
    BigDecimal getConversionFactor(@Param(value = "from") String from, @Param(value = "to") String to);
	
}
