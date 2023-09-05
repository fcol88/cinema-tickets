package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.PurchaseRequestDTO;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
    	isValidAccount(accountId);
    	PurchaseRequestDTO ticketRequestDto = collateTicketQuantities(ticketTypeRequests);
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
		return ticketRequestDto;
	}
    
    private void isNonNegativeTicketCount(int noOfTickets) {
		if (noOfTickets < 0) {
			throw new InvalidPurchaseException("Ticket requests cannot contain negative values");
		}
	}

}
