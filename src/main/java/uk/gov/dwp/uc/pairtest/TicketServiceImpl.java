package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
    	isValidAccount(accountId);
    }
    
    private void isValidAccount(Long accountId) throws InvalidPurchaseException {
		if (accountId == null || accountId < 1L) {
			throw new InvalidPurchaseException("Account ID is invalid");
		}
	}

}
