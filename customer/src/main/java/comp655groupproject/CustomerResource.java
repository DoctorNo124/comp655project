
/*RESTful resource class that defines HTTP/REST interface for interacting with customer data.
Uses @Path, @Get, @POST to map HTTP request to Java methods*/
package comp655groupproject;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import java.net.URI;

@Path("/api/customers") // Base path for all methods in this class
@Produces("application/json") // Specifies that methods produce JSON output
@Consumes("application/json")  // Specifies that methods accept JSON input
public class CustomerResource {

    /* Creates a new customer in the database.*/
    @POST // Maps to HTTP POST method
    @Transactional(Transactional.TxType.REQUIRED) // Specifies transaction type
    public Response createCustomer(@Valid Customer customer) {
        Customer.persistCustomer(customer);
        URI uri = UriBuilder.fromResource(CustomerResource.class)
                .path(Long.toString(customer.id))
                .build();
        return Response.created(uri).entity(customer).build(); // Returns HTTP 201 Created with the customer details
    }

    /* Retrieves a customer by ID. Returns 404 Not Found if the customer does not exist. */
    @GET  // Maps to HTTP GET method
    @Path("{id}") // Path parameter for customer ID
    @Transactional(Transactional.TxType.REQUIRED) // Specifies transaction type
    public Response getCustomerById(@PathParam("id") Long id) {
        Customer customer = Customer.findCustomerById(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(customer).build(); // If found, returns HTTP 200 OK with customer details
    }

    /* Retrieves a customer by  name. Returns 404 Not Found if the customer does not exist. */
    @GET // Maps to HTTP GET method
    @Path("/name/{name}")
    @Transactional(Transactional.TxType.REQUIRED) // Specifies transaction type
    public Response getCustomerByName(@PathParam("name") String name) {
        Customer customer = Customer.findCustomerByName(name);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(customer).build();
    }

    /* Retrieves a list of all customers in the database. */
    @GET // Maps to HTTP GET method
    @Transactional // Specifies transaction type
    public Response getAllCustomers() {
        return Response.ok(Customer.findAllCustomers()).build();
    }

    /* Retrieves a random customer from the database. */
    @GET // Maps to HTTP GET method
    @Path("/random")
    @Transactional // Specifies transaction type
    public Response getRandomCustomer() {
        Customer customer = Customer.findRandomCustomer();
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(customer).build();
    }

    /* Updates the details of an existing customer identified by ID.
    Returns 404 Not Found if the customer does not exist. */
    @PUT // Maps to HTTP PUT method
    @Path("{id}") // Path parameter for customer ID
    @Transactional // Specifies transaction type
    @Operation(summary = "Update a customer by their ID")
    @APIResponse(responseCode = "200", description = "Customer updated successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response updateCustomer(@PathParam("id") Long id, @Valid @RequestBody Customer update) {
        Customer customer = Customer.findCustomerById(id);
        if (customer != null) {
            // Call the static update method on Customer, passing in the updated information
            return Response.ok(Customer.updateCustomer(update)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /* Deletes a customer from the database by ID.
    Returns 204 No Content on success, 404 Not Found if the customer does not exist. */
    @DELETE // Maps to HTTP DELETE method
    @Path("{id}") // Path parameter for customer ID
    @Transactional // Specifies transaction type
    public Response deleteCustomer(@PathParam("id") Long id) {
        boolean deleted = Customer.deleteCustomer(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /* Updates the balance of a customer.*/
    @PATCH  // PATCH is for partial updates
    @Path("{id}/balance") // Path parameter for customer ID and balance
    @Transactional // Specifies transaction type
    public Response updateCustomerBalance(@PathParam("id") Long id, @QueryParam("newBalance") double newBalance) {
        Customer customer = Customer.updateCustomerBalance(id, newBalance);
        if (customer != null) {
            return Response.ok(customer).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
