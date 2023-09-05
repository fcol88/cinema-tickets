package uk.gov.dwp.uc.pairtest.domain;

import lombok.Getter;

@Getter
public class PurchaseRequestDTO {
	
	private int adultTickets = 0;
	private int childTickets = 0;
	private int infantTickets = 0;
	
	public void incrementTicketCount(TicketTypeRequest ticketTypeRequest) {
		switch(ticketTypeRequest.getTicketType()) {
		case ADULT:
			this.adultTickets += ticketTypeRequest.getNoOfTickets();
			break;
		case CHILD:
			this.childTickets += ticketTypeRequest.getNoOfTickets();
			break;
		case INFANT:
			this.infantTickets += ticketTypeRequest.getNoOfTickets();
			break;
		default:
			throw new IllegalStateException("Unexpected ticket type");
		}
	}
	
	public int getTotalTickets() {
		return adultTickets + childTickets + infantTickets;
	}
	
	public int getTotalSeats() {
		return adultTickets + childTickets;
	}

}
