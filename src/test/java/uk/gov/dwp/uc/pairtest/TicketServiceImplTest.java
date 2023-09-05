package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

class TicketServiceImplTest {
	
	private TicketServiceImpl ticketServiceImpl;
	
	private static final Long VALID_ACCOUNT_ID = 1L;
	
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
	
	@Test
	void accountValidationRejectsWhenLessThanOne() {
		TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
		InvalidPurchaseException result = assertThrows(
				InvalidPurchaseException.class,
				() -> ticketServiceImpl.purchaseTickets(null, request)
		);
		assertEquals("Account ID is invalid", result.getMessage());
	}
	
	@Test
	void isNonNegativeTicketCountRejectsWhenNegativeTicketCount() {
		TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -1);
		InvalidPurchaseException result = assertThrows(
				InvalidPurchaseException.class,
				() -> ticketServiceImpl.purchaseTickets(VALID_ACCOUNT_ID, request)
		);
		assertEquals("Ticket requests cannot contain negative values", result.getMessage());
	}
	
	@Test
	void hasMinimumAdultAttendanceRejectsWhenInvalid() {
		TicketTypeRequest request = new TicketTypeRequest(
				TicketTypeRequest.Type.CHILD, TicketServiceImpl.MIN_ADULT_TICKETS - 1);
		InvalidPurchaseException result = assertThrows(
				InvalidPurchaseException.class,
				() -> ticketServiceImpl.purchaseTickets(VALID_ACCOUNT_ID, request)
		);
		assertEquals("At least 1 adult ticket must be purchased", result.getMessage());
	}

}