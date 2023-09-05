package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

class TicketServiceImplTest {
	
	private TicketServiceImpl ticketServiceImpl;
	
	@BeforeEach
	public void setup() {
		ticketServiceImpl = new TicketServiceImpl();
	}
	
	@Test
	void accountValidationRejectsWhenNull() { 
		TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
		InvalidPurchaseException result = assertThrows(
				InvalidPurchaseException.class,
				() -> ticketServiceImpl.purchaseTickets(0L, request)
		);
		assertEquals("Account ID is invalid", result.getMessage());
	}

}
