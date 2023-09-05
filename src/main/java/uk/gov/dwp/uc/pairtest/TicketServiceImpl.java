package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.PurchaseRequestDTO;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
	
	private final TicketPaymentServiceImpl ticketPaymentService;
	private final SeatReservationServiceImpl seatReservationService;
	
	public TicketServiceImpl(TicketPaymentServiceImpl ticketPaymentService,
			SeatReservationServiceImpl seatReservationService) {
		this.ticketPaymentService = ticketPaymentService;
		this.seatReservationService = seatReservationService;
	}
	
	//Candidates for externalisation into app props or similar if using Spring
	//Package-level visibility for easier testing
	static final int MIN_ADULT_TICKETS = 1;
	static final int ADULT_TICKET_PRICE = 20;
	static final int CHILD_TICKET_PRICE = 10;
	static final int INFANT_TICKET_PRICE = 0;
	static final int MAX_TICKETS_PER_TRANSACTION = 20;

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
    	isValidAccount(accountId);
    	PurchaseRequestDTO ticketRequestDto = collateTicketQuantities(ticketTypeRequests);
    	var totalPrice = getTotalPrice(ticketRequestDto);
    	ticketPaymentService.makePayment(accountId, totalPrice);
    	seatReservationService.reserveSeat(accountId, ticketRequestDto.getTotalSeats());
    }
    
    private void isValidAccount(Long accountId) throws InvalidPurchaseException {
		if (accountId == null || accountId < 1L) {
			throw new InvalidPurchaseException("Account ID is invalid");
		}
	}
    
    private PurchaseRequestDTO collateTicketQuantities(TicketTypeRequest... ticketTypeRequests) {
		PurchaseRequestDTO ticketRequestDto = new PurchaseRequestDTO();
		for (TicketTypeRequest request : ticketTypeRequests) {
			isNonNegativeTicketCount(request.getNoOfTickets());
			ticketRequestDto.incrementTicketCount(request);
		}
		checkTicketQuantityValidity(ticketRequestDto);
		return ticketRequestDto;
	}
    
    private void isNonNegativeTicketCount(int noOfTickets) {
		if (noOfTickets < 0) {
			throw new InvalidPurchaseException("Ticket requests cannot contain negative values");
		}
	}
    
    private void checkTicketQuantityValidity(PurchaseRequestDTO ticketRequestDto) {
		hasMinimumAdultAttendance(ticketRequestDto.getAdultTickets());
		hasEnoughAdultsForInfants(ticketRequestDto.getAdultTickets(), 
				ticketRequestDto.getInfantTickets());
		isInsideTicketLimit(ticketRequestDto.getTotalTickets());
	}
    
    private void hasMinimumAdultAttendance(int noOfAdults) {
		if (noOfAdults < MIN_ADULT_TICKETS) {
			throw new InvalidPurchaseException("At least " + MIN_ADULT_TICKETS + " adult ticket must be purchased");
		}
	}
    
    private void hasEnoughAdultsForInfants(int noOfAdults, int noOfInfants) {
		if (noOfAdults < noOfInfants) {
			throw new InvalidPurchaseException("There must be at least 1 adult "
					+ "per infant for seating");
		}
	}
    
    private void isInsideTicketLimit(int totalTickets) {
		if (totalTickets > MAX_TICKETS_PER_TRANSACTION) {
			throw new InvalidPurchaseException("You may only buy " 
					+ MAX_TICKETS_PER_TRANSACTION + " tickets at once");
		}		
	}
    
    private int getTotalPrice(PurchaseRequestDTO ticketRequestDto) {
		return ticketRequestDto.getAdultTickets() * ADULT_TICKET_PRICE
				+ ticketRequestDto.getAdultTickets() * CHILD_TICKET_PRICE
				+ ticketRequestDto.getAdultTickets() * INFANT_TICKET_PRICE;
	}

}
