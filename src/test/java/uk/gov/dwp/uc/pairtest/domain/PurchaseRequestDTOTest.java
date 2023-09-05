package uk.gov.dwp.uc.pairtest.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PurchaseRequestDTOTest {
	
	private static PurchaseRequestDTO dto;
	
	@BeforeAll
	public static void setup() {
		dto = new PurchaseRequestDTO();
		TicketTypeRequest[] requests = {
				new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3),
				new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
				new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
		};
		for(TicketTypeRequest request : requests) {
			dto.incrementTicketCount(request);
		}
	}
	
	@Test
	void purchaseRequestIncrementsTickets() {		
		assertEquals(3, dto.getAdultTickets());
		assertEquals(2, dto.getChildTickets());
		assertEquals(1, dto.getInfantTickets());
	}
	
	@Test
	void purchaseRequestIncludesInfantsInTotalTickets() {
		assertEquals(6, dto.getTotalTickets());
	}

}
