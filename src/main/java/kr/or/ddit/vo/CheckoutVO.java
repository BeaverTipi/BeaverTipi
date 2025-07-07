package kr.or.ddit.vo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"checkout","paymentKey"})
public class CheckoutVO {
	private String checkout;
	private String paymentKey;
	private String url;
}
