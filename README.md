# DWP Ticket Service Test

Thank you for your consideration for the position of Senior Software Engineer. Included are a few brief notes to give context to my approach, and what additional assumptions I have made based on the provided specification.

## The rules as I've understood them

Three ticket types:

- Infant (£0 - doesn't count as a seat, **but** counts as a ticket, needs at least one adult)
- Child (£10, counts as a seat, needs at least one adult)
- Adult (£20, counts as a seat, at least one must attend, probably the one calling `purchaseTickets`)

A maximum of 20 tickets to be purchased per **call** to `purchaseTickets` rather than per `TicketTypeRequest`.

Account ID must be a positive, non-null `Long`.

An infant sits on an adult's lap, therefore there must be at least as many adults as infants (otherwise that's going to get very crowded...).

I've enforced that `TicketTypeRequest` must have a non-negative number of tickets - zero might be legitimate if at least three are always provided.

## With regards to style

Normally I work with Spring Boot, however I wanted to take as much steer as possible from the challenge/DWP's style. I had a quick peruse through some of the other public DWP repositories and saw a mix of plain Java in some, Spring Boot in others, and fairly atomic methods throughout.

I took the contents of `pom.xml` as a guide and went with no Spring and fairly atomic methods as per the feel I got from those repositories - I did permit myself lombok but in the end it wasn't a deal-breaker given its limited use. In fairness, doing it with Spring would've been largely similar - except with a `@Service` tag and a sprinkling of `@Autowired`, and using the interfaces rather than the implementations directly, letting Spring handle the injection of the implementation.

I noticed the provided dependencies were a little bit out of date and weren't in line with what current DWP repositories are using (there was also a vulnerability in the version of JUnit4 - was this part of the test? If so, very nice), so I decided to upgrade them to the latest versions (including JUnit4 -> JUnit5).

Given the void return of `purchaseTickets` with a thrown exception, I've leaned quite heavily into throwing that exception, imagining that this might be part of a microservice or similar. This also seems like a pattern in some of the repositories I looked through. I amended the exception to allow the addition of a message - this wasn't mentioned as being against the rules, but I apologise if this was a problematic change.

## Assumptions I've _not_ made

I've not assumed configuration is guaranteed to be static. I've called out that the prices and objective minimum number of adults (as opposed to lap-donors for infants) may change, and given the gouging some cinemas do (~~have I assumed it was a cinema? I think I've assumed it was a cinema~~ it was in the repository name...), it's not a guarantee that infants will always go free - so the calculation is performed but with zero as the multiplicand. Similarly, I've not assumed a stationary maximum tickets per sale value. If I was using Spring, I would've brought these in as `@Value`s.

I've not assumed restrictions on multiple `TicketTypeRequest`s of the same `TicketTypeRequest.Type` - I envisioned this being multiple groups booking together and needing to be logically separate for some other reason, as long as they don't exceed the maximum ticket count.

Thank you once more for your consideration, and
